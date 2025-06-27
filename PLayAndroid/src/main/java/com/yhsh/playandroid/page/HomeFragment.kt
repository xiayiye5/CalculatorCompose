package com.yhsh.playandroid.page

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.SimpleOnPageChangeListener
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.yhsh.playandroid.R
import com.yhsh.playandroid.bean.Article
import com.yhsh.playandroid.bean.BannerBean
import com.yhsh.playandroid.util.AppUtils
import com.yhsh.playandroid.util.SpaceItemDecoration
import com.yhsh.playandroid.viewmodel.BannerViewModel
import com.yhsh.playandroid.viewmodel.HomeArticleListViewModel
import kotlinx.coroutines.delay
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
    private var bannerData: List<BannerBean>? = null
    override fun initData(view: View) {
        val homeViewPager = view.findViewById<ViewPager>(R.id.home_viewPager)
        val homeRecyclerView = view.findViewById<RecyclerView>(R.id.home_recyclerView)
        val homeBannerTitle = view.findViewById<TextView>(R.id.home_banner_title)
        val refreshLayout = view.findViewById<SmartRefreshLayout>(R.id.refreshLayout)
        articleViewModel.initRefresh(refreshLayout)
        val bannerAdapter = BannerAdapter()
        //初始化banner数量
        homeViewPager.adapter = bannerAdapter
        val rvLayoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        val articleAdapter = ArticleAdapter() {
            Toast.makeText(activity, "点击了$it", Toast.LENGTH_SHORT).show()
        }
        homeRecyclerView.apply {
            layoutManager = rvLayoutManager
            adapter = articleAdapter
            //添加分割线
            addItemDecoration(SpaceItemDecoration(AppUtils.dp2pxInt(10f), rvLayoutManager))
        }
        //请求接口数据
        bannerViewModel.banner()
        articleViewModel.getArticleList(0)
        lifecycleScope.launch {
            bannerViewModel._bannerState.filterNotNull().collect {
                if (imgList.size == 0) {
                    it.forEachIndexed { index, _ ->
                        val iv = ImageView(requireContext())
                        iv.setOnClickListener {
                            Toast.makeText(activity, "点击了:$index", Toast.LENGTH_SHORT).show()
                        }
                        imgList.add(iv)
                    }
                }
                it.let { url ->
                    bannerData = url
                    //设置标题
                    homeBannerTitle.text = url[0].title
                    bannerAdapter.refreshBanner(imgList, url)
                    bannerViewModel.startLoop(homeViewPager)
                }
            }
        }
        homeViewPager.addOnPageChangeListener(object : SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                bannerData?.let {
                    homeBannerTitle.text = it[position].title
                }
            }
        })
        lifecycleScope.launch {
            articleViewModel._articleStateFlow.filterNotNull().collect {
                //延迟1毫秒，让banner数据先展示
                delay(1)
                Log.d(TAG, "打印文章${it.datas.size}-curPage:${it.curPage}-total:${it.pageCount}")
                it.datas.let { articles ->
                    if (it.curPage >= it.pageCount) {
                        refreshLayout.setNoMoreData(true)
                        return@let
                    }
                    //添加分页数据
                    articleData.addAll(articles)
                    articleAdapter.updateArticle(articleData)
                    //下一页数据更新成功隐藏加载更多
                    refreshLayout.finishLoadMore(true)
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