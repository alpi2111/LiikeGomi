package com.liike.liikegomi.administrate_products.ui.fragments

import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.liike.liikegomi.administrate_products.adapters.AdminProductsAdapter
import com.liike.liikegomi.administrate_products.adapters.AdminProductsCallback
import com.liike.liikegomi.administrate_products.view_model.AdminProductsViewModel
import com.liike.liikegomi.administrate_products.view_model.AdminProductsViewModelFactory
import com.liike.liikegomi.background.firebase_db.entities.Productos
import com.liike.liikegomi.base.ui.BaseFragment
import com.liike.liikegomi.databinding.FragmentAdminProductBinding

class AdminProductFragment(private val idCategory: Int, private val mViewModel: AdminProductsViewModel) : BaseFragment<FragmentAdminProductBinding>() {

    private lateinit var mProductsAdapter: AdminProductsAdapter

    override fun inflateBinding(): FragmentAdminProductBinding {
        return FragmentAdminProductBinding.inflate(layoutInflater)
    }

    override fun onCreateView() {

        val viewModel = try {
            ViewModelProvider(this)[AdminProductsViewModel::class.java]
        } catch (e: Exception) {
            e.printStackTrace()
            ViewModelProvider(this, AdminProductsViewModelFactory())[AdminProductsViewModel::class.java]
        }

        mProductsAdapter = AdminProductsAdapter(mViewModel, object : AdminProductsCallback {
            override fun update(product: Productos) {
                viewModel.updateProduct(product)
            }

            override fun delete(product: Productos, position: Int) {
                viewModel.deleteProduct(product)
            }
        })

        viewModel.mProductsList.observe(this.viewLifecycleOwner) {
            if (it.isEmpty()) {
                mBinding.recyclerViewProducts.isVisible = false
                mBinding.progressBar.isVisible = false
                mBinding.noProductsText.isVisible = true
                return@observe
            }
            mBinding.noProductsText.isVisible = false
            mBinding.recyclerViewProducts.isVisible = true
            mProductsAdapter.setData(it)
            mBinding.progressBar.isVisible = false
        }

        mViewModel.mIndexProductDeleted.observe(this.viewLifecycleOwner) {
            mProductsAdapter.deleteItem(it)
        }

        mBinding.recyclerViewProducts.adapter = mProductsAdapter

        viewModel.getProductsByCategory(idCategory)
    }
}