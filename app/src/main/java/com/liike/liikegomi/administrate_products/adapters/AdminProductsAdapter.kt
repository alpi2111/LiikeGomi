package com.liike.liikegomi.administrate_products.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.Blob
import com.liike.liikegomi.R
import com.liike.liikegomi.add_product.adapters.SpinnerAdapter
import com.liike.liikegomi.administrate_products.view_model.AdminProductsViewModel
import com.liike.liikegomi.background.firebase_db.entities.Categoria
import com.liike.liikegomi.background.firebase_db.entities.Productos
import com.liike.liikegomi.background.utils.MessageUtils
import com.liike.liikegomi.base.ui.BaseActivity
import com.liike.liikegomi.databinding.ItemAdminProductBinding
import com.liike.liikegomi.image_picker.background.interfaces.ImageSelectionCallback
import com.liike.liikegomi.image_picker.ui.SelectImageBottomSheet
import com.liike.liikegomi.text

class AdminProductsAdapter(private val viewModel: AdminProductsViewModel) : RecyclerView.Adapter<AdminProductsAdapter.ViewHolder>() {

    private val mProductsList: MutableList<Productos> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.createViewHolder(parent, viewModel)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mProductsList[position])
    }

    override fun getItemCount(): Int = mProductsList.size

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemViewType(position: Int): Int = position

    fun setData(products: List<Productos>) {
        val currentSize = itemCount
        mProductsList.clear()
        mProductsList.addAll(products)
        notifyItemRangeRemoved(0, currentSize)
        notifyItemRangeInserted(0, itemCount)
    }

    fun deleteItem(position: Int) {
        if (position in 0..itemCount) {
            mProductsList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    class ViewHolder private constructor(private val mBinding: ItemAdminProductBinding, private val viewModel: AdminProductsViewModel): RecyclerView.ViewHolder(mBinding.root) {
        private val mActivity: BaseActivity<*, *>? by lazy { itemView.context as? BaseActivity<*, *> }
        private val defaultImagePadding: Int by lazy { mBinding.btnTakePhoto.paddingTop }
        private val spinnerAdapter: SpinnerAdapter<Categoria> by lazy {
            SpinnerAdapter(itemView.context, Categoria("Elige una categoría", true, -1, ""), viewModel.mCategoriesList.value!!)
        }

        fun bind(product: Productos) {
            mBinding.run {
                etProductName.setText(product.productName)
                ilProductName.helperText = "ID: ${product.productId}"
                etProductDescription.setText(product.productDescription)
                etPrice.setText(product.productPrice.toString())
                etStock.setText(product.productStock.toString())
                switchIsVisible.isChecked = product.isVisible
                spinnerCategory.adapter = spinnerAdapter
                for (i in 0 until spinnerCategory.adapter.count) {
                    val category = spinnerCategory.adapter.getItem(i) as Categoria
                    if (product.idCategoria == category.idCategory) {
                        spinnerCategory.setSelection(i, false)
                        break
                    }
                }

                val bytes = product.productImage?.toBytes()
                if (bytes != null) {
                    btnTakePhoto.setPadding(0, 0, 0, 0)
                    btnTakePhoto.imageTintList = null
                    Glide.with(itemView.context).load(bytes).centerCrop().into(btnTakePhoto)
                }

                btnTakePhoto.setOnClickListener {
                    mActivity.let { activity ->
                        if (activity == null)
                            return@let
                        SelectImageBottomSheet.show(activity.supportFragmentManager, object : ImageSelectionCallback {
                            override fun onImageSelected(byteArray: ByteArray?) {
                                if (byteArray != null) {
                                    btnTakePhoto.setPadding(0, 0, 0, 0)
                                    btnTakePhoto.imageTintList = null
                                    Glide.with(itemView.context).load(byteArray).centerCrop().into(btnTakePhoto)
                                } else {
                                    btnTakePhoto.setPadding(0, defaultImagePadding, 0, defaultImagePadding)
                                    btnTakePhoto.imageTintList = ContextCompat.getColorStateList(itemView.context, R.color.red_light)
                                    btnTakePhoto.background = AppCompatResources.getDrawable(itemView.context, R.drawable.bg_photo_taker_red)
                                    Glide.with(itemView.context).load(R.drawable.ic_add_a_photo).into(btnTakePhoto)
                                }
                                viewModel.notifyImageBytesWithIndex(byteArray, adapterPosition)
                            }
                        })
                    }
                }

                btnUpdateName.setOnClickListener {
                    product.productName = etProductName.text()
                    product.productDescription = etProductDescription.text()
                    product.productStock = etStock.text().toInt()
                    product.productPrice = etPrice.text().toDouble()
                    product.isVisible = switchIsVisible.isChecked
                    product.idCategoria = (spinnerCategory.selectedItem as Categoria).idCategory
                    val imageBytes = viewModel.getImageBytesByPosition(adapterPosition)
                    if (imageBytes != null)
                        product.productImage = Blob.fromBytes(imageBytes)
                    viewModel.updateProduct(product)
                }

                btnDelete.setOnClickListener {
                    MessageUtils.dialog(
                        context = itemView.context,
                        title = "Advertencia",
                        message = "¿Realmente desas borrar el producto ${product.productName}?",
                        okButton = "Sí",
                        cancelButton = "No",
                        onOkAction = {
                            viewModel.deleteProduct(product, adapterPosition)
                        },
                        onCancelAction = {}
                    )
                }
            }
        }

        companion object {
            fun createViewHolder(parent: ViewGroup, viewModel: AdminProductsViewModel): ViewHolder {
                val binding = ItemAdminProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ViewHolder(binding, viewModel)
            }
        }
    }
}