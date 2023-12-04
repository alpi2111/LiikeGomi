package com.liike.liikegomi.main.ui.fragments

import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.liike.liikegomi.base.ui.BaseFragment
import com.liike.liikegomi.databinding.FragmentProductMainBinding
import com.liike.liikegomi.main.ui.adapters.ItemProductAdapter
import com.liike.liikegomi.main.view_model.MainViewModel
import com.liike.liikegomi.main.view_model.MainViewModelFactory

class MainProductFragment(private val idCategory: Int) : BaseFragment<FragmentProductMainBinding>() {

    private lateinit var mProductsAdapter: ItemProductAdapter
    private lateinit var mViewModel: MainViewModel
    private var mIsFirstProductsInitialization = true

    override fun inflateBinding(): FragmentProductMainBinding {
        return FragmentProductMainBinding.inflate(layoutInflater)
    }

    override fun onCreateView() {
        mViewModel = try {
            ViewModelProvider(this)[MainViewModel::class.java]
        } catch (e: Exception) {
            e.printStackTrace()
            ViewModelProvider(this, MainViewModelFactory())[MainViewModel::class.java]
        }

        mProductsAdapter = ItemProductAdapter()

        mViewModel.mProductsList.observe(this.viewLifecycleOwner) {
            if (it.isEmpty()) {
                mBinding.recyclerViewProducts.isVisible = false
                mBinding.progressBar.isVisible = false
                mBinding.noProductsText.isVisible = true
                return@observe
            }
            mBinding.noProductsText.isVisible = false
            mBinding.recyclerViewProducts.isVisible = true
            mIsFirstProductsInitialization = false
            mProductsAdapter.setProducts(it)
            mBinding.progressBar.isVisible = false
        }

        mBinding.recyclerViewProducts.adapter = mProductsAdapter

        mViewModel.getProductsByCategory(idCategory)
    }

}