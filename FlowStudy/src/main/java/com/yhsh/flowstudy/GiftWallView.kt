package com.yhsh.flowstudy

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import me.relex.circleindicator.CircleIndicator3

class GiftWallView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val viewPager: ViewPager2
    private val pageIndicator: CircleIndicator3
    private lateinit var pageAdapter: GiftPageAdapter
    private lateinit var viewModel: GiftWallViewModel

    init {
        val gitView = View.inflate(getContext(), R.layout.layout_gift_wall, null)
        viewPager = gitView.findViewById(R.id.viewPager)
        pageIndicator = gitView.findViewById(R.id.pageIndicator)
        addView(gitView)
    }

    fun initialize(viewModel: GiftWallViewModel) {
        this.viewModel = viewModel
        setupViewPager()
        observeViewModel()
    }

    fun showGift(giftList: List<Gift>) {
        viewModel.loadGifts(giftList)
    }

    private fun setupViewPager() {
        pageAdapter = GiftPageAdapter { gift ->
            viewModel.selectGift(gift)
            Toast.makeText(context, "点击了礼物: ${gift.giftName}", Toast.LENGTH_SHORT).show()
        }

        viewPager.apply {
            adapter = pageAdapter
            offscreenPageLimit = 1
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                }
            })
        }

        pageIndicator.setViewPager(viewPager)
    }

    private fun observeViewModel() {
        viewModel.gifts.observeForever { gifts ->
            pageAdapter.submitList(gifts)
            pageIndicator.visibility = if (gifts.size > GiftPageAdapter.ITEMS_PER_PAGE) {
                VISIBLE
            } else {
                GONE
            }
            pageIndicator.setViewPager(viewPager)
        }
    }
}