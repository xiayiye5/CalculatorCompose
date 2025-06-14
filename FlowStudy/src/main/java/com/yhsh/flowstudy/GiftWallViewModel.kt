package com.yhsh.flowstudy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GiftWallViewModel : ViewModel() {
    private val _gifts = MutableLiveData<List<Gift>>()
    val gifts: LiveData<List<Gift>> get() = _gifts

    private var selectedGift: Gift? = null

    fun loadGifts(giftList: List<Gift>) {
        // 重置选中状态
        selectedGift?.isSelected = false
        selectedGift = null

        _gifts.value = giftList.map { it.copy() }
    }

    fun selectGift(gift: Gift) {
        // 清除之前的选中状态
        selectedGift?.isSelected = false
        selectedGift = gift

        // 设置新的选中状态
        gift.isSelected = true

        // 更新LiveData
        _gifts.value = _gifts.value
    }
}