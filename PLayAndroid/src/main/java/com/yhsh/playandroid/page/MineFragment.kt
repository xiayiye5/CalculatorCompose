package com.yhsh.playandroid.page

import android.os.Bundle
import android.view.View
import com.yhsh.playandroid.R

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MineFragment : BaseFragment() {


    override fun initData(view: View) {

    }

    override fun layoutViewId(): Int = R.layout.fragment_mine

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MineFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}