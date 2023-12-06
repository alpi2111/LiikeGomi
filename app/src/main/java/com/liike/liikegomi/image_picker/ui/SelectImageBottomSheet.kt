package com.liike.liikegomi.image_picker.ui

import android.app.Activity
import android.content.ContentUris
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.liike.liikegomi.background.utils.ImageUtils
import com.liike.liikegomi.background.utils.MessageUtils
import com.liike.liikegomi.databinding.BottomSheetSelectImageBinding
import com.liike.liikegomi.image_picker.background.interfaces.ImageSelectionCallback
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.launch
import java.io.File

class SelectImageBottomSheet private constructor(private val callback: ImageSelectionCallback) : BottomSheetDialogFragment() {
    companion object {
        private const val TAG = "SelectImageBottomSheet"
        fun show(fragmentManager: FragmentManager, callback: ImageSelectionCallback) {
            findAndCloseImageBottomSheets(fragmentManager)
            SelectImageBottomSheet(callback).show(fragmentManager, TAG)
        }

        private fun findAndCloseImageBottomSheets(fragmentManager: FragmentManager) {
            val currentBottomSheet = fragmentManager.findFragmentByTag(TAG) as? SelectImageBottomSheet
            if (currentBottomSheet != null && currentBottomSheet.isVisible) {
                try {
                    currentBottomSheet.dismissNow()
                } catch (e: Exception) {
                    Log.e("closingImageBottomSheet", e.message.orEmpty())
                    currentBottomSheet.dismiss()
                }
            }
        }
    }

    private var mFileTemp: File? = null
    private lateinit var mBinding: BottomSheetSelectImageBinding
    private val startGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        evaluateIntentDataForGettingGalleryImage(result)
    }

    private val startCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        evaluateIntentDataForTakingPhoto(result)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = BottomSheetSelectImageBinding.inflate(inflater, null, false)
        mBinding.btnTakePhoto.setOnClickListener { dispatchTakePictureIntent() }
        mBinding.btnSelectPhoto.setOnClickListener { openGallery() }
        return mBinding.root
    }

    override fun onDismiss(dialog: DialogInterface) {
        mFileTemp?.delete()
        super.onDismiss(dialog)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startGallery.launch(intent)
    }

    private fun getImagePath(uri: Uri?, selection: String?): String? {
        try {
            var path = ""
            val cursor = uri?.let { this.requireActivity().contentResolver.query(it, null, selection, null, null) }
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    val column = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                    if (column >= 0) {
                        path = cursor.getString(column)
                    }
                }
                cursor.close()
            }
            return path
        } catch (e: Exception) {
            Log.e("gettingImagePathFails", e.stackTraceToString())
            return null
        }
    }

    private fun dispatchTakePictureIntent() {
        try {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val tempFile = createTempImageFile()
            if (tempFile != null) {
                val imageUri = FileProvider.getUriForFile(
                    requireContext(),
                    requireContext().applicationContext.packageName.plus(".provider"),
                    tempFile
                )
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                startCamera.launch(intent)
            } else {
                MessageUtils.toast(requireContext(), "No se pudo iniciar la cámara")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            MessageUtils.toast(requireContext(), "Error not catch, ${e.message}")
            dismiss()
        }
    }

    private fun evaluateIntentDataForGettingGalleryImage(result: ActivityResult) {
        val data = result.data
        if (result.resultCode != Activity.RESULT_OK || data == null) {
            callback.onImageSelected(null)
            dismiss()
            return
        }

        val uri: Uri? = data.data
        var filePath: String? = null
        try {
            if (DocumentsContract.isDocumentUri(this.requireContext(), uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                if ("com.android.providers.media.documents" == uri?.authority) {
                    val id = docId.split(":")[1]
                    val selection = MediaStore.Images.Media._ID + "=" + id
                    filePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection)
                } else if ("com.android.providers.downloads.documents" == uri?.authority) {
                    val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(docId))
                    filePath = getImagePath(contentUri, null)
                }
            } else if ("content".equals(uri?.scheme, ignoreCase = true)) {
                filePath = getImagePath(uri, null)
            } else if ("file".equals(uri?.scheme, ignoreCase = true)) {
                filePath = uri?.path
            }
        } catch (e: Exception) {
            e.printStackTrace()
            MessageUtils.toast(requireContext(), "Error not catch, due to ${e.message}")
        } finally {
            requireActivity().lifecycleScope.launch {
                filePath.let { path ->
                    if (path.isNullOrBlank()) {
                        callback.onImageSelected(null)
                    } else {
                        val file = File(path)
                        val fileCompressed = compressTo1Mb(file)
                        val fileBytes = fileCompressed.readBytes()
                        callback.onImageSelected(fileBytes)
                    }
                    dismiss()
                }
            }
        }
    }

    private fun evaluateIntentDataForTakingPhoto(result: ActivityResult) {
        val fileTemp = mFileTemp
        if (result.resultCode != Activity.RESULT_OK || fileTemp == null) {
            callback.onImageSelected(null)
            dismiss()
            return
        }

        val imageBytes = fileTemp.readBytes()
        if (!fileTemp.exists() || imageBytes.isEmpty()) {
            callback.onImageSelected(null)
            dismiss()
            return
        }

        try {
            requireActivity().lifecycleScope.launch {
                val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                val rotatedBitmap = ImageUtils.rotateImage(bitmap, fileTemp.absolutePath)
                val newFile = File(fileTemp.toURI())
                newFile.outputStream().use {
                    rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, it)
                    it.flush()
                }
                val bytes = compressTo1Mb(newFile)
                callback.onImageSelected(bytes.readBytes())
                dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            MessageUtils.toast(requireContext(), "Error not catch camera, ${e.message}")
        } finally {
            if (this.isVisible)
                dismiss()
        }
    }

    private fun createTempImageFile(): File? {
        val timestampMillis = System.currentTimeMillis()
        val imageTempName = "Temp_$timestampMillis"
        val storageDir = requireActivity().cacheDir
        mFileTemp = File.createTempFile(imageTempName, ".jpg", storageDir)
        return mFileTemp
    }

    private suspend fun compressTo1Mb(file: File): File {
        return Compressor.compress(requireContext(), file) {
            resolution(800, 600)
            quality(80)
            format(Bitmap.CompressFormat.JPEG)
            size(1048400L)
        }
    }
}