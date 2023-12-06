package com.liike.liikegomi.administrate_products.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.liike.liikegomi.administrate_products.ui.fragments.AdminProductFragment
import com.liike.liikegomi.administrate_products.view_model.AdminProductsViewModel
import com.liike.liikegomi.background.firebase_db.entities.Categoria

class AdminProductsViewPagerAdapter(private val productsList: List<Categoria>, private val viewModel: AdminProductsViewModel, fragment: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragment, lifecycle) {
    override fun getItemCount(): Int = productsList.size

    override fun createFragment(position: Int): Fragment {
        val idCategory = productsList[position].idCategory
        return AdminProductFragment(idCategory, viewModel)
    }
}