package com.yhsh.playandroid.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            lastPageBundle(it)
        }
    }

    /**
     * 重写此方法可以从bundle中获取上个页面传过来的数据如果有
     */
    private fun lastPageBundle(bundle: Bundle) {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutId = layoutViewId()
        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initData(view)

    }
    abstract fun initData(view: View)

    abstract fun layoutViewId(): Int

}