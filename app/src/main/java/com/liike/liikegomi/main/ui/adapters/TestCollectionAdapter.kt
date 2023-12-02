package com.liike.liikegomi.main.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.liike.liikegomi.main.ui.fragments.MainProductFragment

class TestCollectionAdapter(private val size: Int, fragment: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragment, lifecycle) {
    override fun getItemCount(): Int = size

    override fun createFragment(position: Int): Fragment {
        val fragment = MainProductFragment()
        return fragment
    }
}