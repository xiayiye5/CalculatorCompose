package com.yhsh.playandroid

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class HomeAdapter(fm: FragmentManager, private val listPage: List<Fragment>) :
    FragmentPagerAdapter(fm) {


    override fun getCount(): Int {
        return listPage.size
    }

    override fun getItem(position: Int): Fragment {
        return listPage[position]
    }
}
