package com.rangganf.githubuserbangkit.data.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class DetailAdapter(
    fa: FragmentActivity,
    private val fragment: MutableList<Fragment>
) : FragmentStateAdapter(fa) {

    // Berfungsi untuk mendapakan nomor dari fragmen di adapter.
    override fun getItemCount(): Int = fragment.size

    // Befungsi untuk membuat dan menggembalikan fragment berdasarkan posisinya.
    override fun createFragment(position: Int): Fragment = fragment[position]
}
