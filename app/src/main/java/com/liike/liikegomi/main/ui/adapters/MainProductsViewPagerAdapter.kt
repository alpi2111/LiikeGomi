package com.liike.liikegomi.main.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.liike.liikegomi.background.firebase_db.entities.Categoria
import com.liike.liikegomi.main.ui.fragments.MainProductFragment

class MainProductsViewPagerAdapter(private val productsList: List<Categoria>, fragment: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragment, lifecycle) {
    override fun getItemCount(): Int = productsList.size

    override fun createFragment(position: Int): Fragment {
        val idCategory = productsList[position].idCategory
        return MainProductFragment(idCategory)
    }
}