package com.ltu.m7019eblogapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter


class ViewPagerAdapter(
    private val fragments: List<Fragment>,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    // Returns the number of fragments in the adapter
    override fun getItemCount(): Int = fragments.size

    // Creates a new instance of the specified fragment at the given position
    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}
