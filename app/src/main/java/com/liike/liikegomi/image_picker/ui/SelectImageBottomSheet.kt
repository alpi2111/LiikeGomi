package com.liike.liikegomi.image_picker.ui

import android.app.Activity
import android.content.ContentUris
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.liike.liikegomi.databinding.BottomSheetSelectImageBinding
import com.liike.liikegomi.image_picker.background.interfaces.ImageSelectionCallback
import java.io.File

class SelectImageBottomSheet private constructor(val callback: ImageSelectionCallback) : BottomSheetDialogFragment() {
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

    private lateinit var mBinding: BottomSheetSelectImageBinding
    private val startGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val data = result.data
        if (result.resultCode != Activity.RESULT_OK || data == null) {
            callback.onImageSelected(null)
            dismiss()
            return@registerForActivityResult
        }

        val uri: Uri? = data.data
        var filePath: String? = null
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
        filePath.let { path ->
            if (path.isNullOrBlank()) {
                callback.onImageSelected(null)
                dismiss()
            } else {
                val file = File(path)
                callback.onImageSelected(file.readBytes())
                dismiss()
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = BottomSheetSelectImageBinding.inflate(inflater, null, false)

        mBinding.btnTakePhoto.setOnClickListener {

        }

        mBinding.btnSelectPhoto.setOnClickListener { openGallery() }

        return mBinding.root
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


}