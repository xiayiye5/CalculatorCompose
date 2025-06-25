package com.yhsh.playandroid.page

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.yhsh.playandroid.R
import com.yhsh.playandroid.bean.Article
import com.yhsh.playandroid.viewmodel.BannerViewModel
import com.yhsh.playandroid.viewmodel.HomeArticleListViewModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : BaseFragment() {
    private val bannerViewModel by viewModels<BannerViewModel>()
    private val articleViewModel by viewModels<HomeArticleListViewModel>()
    private val imgList: ArrayList<ImageView> = ArrayList()
    private val articleData = mutableListOf<Article>()
    private val TAG = "HomeFragment"
    override fun initData(view: View) {
        val homeViewPager = view.findViewById<ViewPager>(R.id.home_viewPager)
        val homeRecyclerView = view.findViewById<RecyclerView>(R.id.home_recyclerView)
        val bannerAdapter = BannerAdapter()
        //初始化banner数量
        homeViewPager.adapter = bannerAdapter
        homeRecyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        val articleAdapter = ArticleAdapter()
        homeRecyclerView.adapter = articleAdapter
        //请求接口数据
        bannerViewModel.banner()
        articleViewModel.getArticleList(0)
        lifecycleScope.launch {
            bannerViewModel._bannerState.filterNotNull().collect {
                if (imgList.size == 0) {
                    it.data?.forEachIndexed { index, banner ->
                        val iv = ImageView(requireContext())
                        iv.setOnClickListener {
                            Toast.makeText(activity, "点击了:$index", Toast.LENGTH_SHORT).show()
                        }
                        imgList.add(iv)
                    }
                }
                it.data?.let { url ->
                    bannerAdapter.refreshBanner(imgList, url)
                    bannerViewModel.startLoop(homeViewPager)
                }
            }
        }
        lifecycleScope.launch {
            articleViewModel._articleStateFlow.filterNotNull().collect {
                Log.d(TAG, "打印文章${it.data?.datas?.size}")
                it.data?.datas?.let { articles ->
                    articleData.addAll(articles)
                    articleAdapter.updateArticle(articles)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bannerViewModel.stopLoop()
    }

    override fun layoutViewId(): Int = R.layout.fragment_home

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = HomeFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
            }
        }
    }
}