package com.yhsh.playandroid.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * 自定区分方向添加间距
 */
class SpaceItemDecoration(
    private val spaceHeight: Int, private val layoutManager: LinearLayoutManager
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        if (layoutManager.orientation == RecyclerView.HORIZONTAL) {
            //水平方向
            // 设置左右对称间距
            outRect.left = spaceHeight / 2
            outRect.right = spaceHeight / 2
        } else {
            // 除最后一项外，其他项底部添加间距
            if (parent.getChildAdapterPosition(view) != parent.adapter?.itemCount?.minus(1)) {
                outRect.bottom = spaceHeight
            }
        }
    }
}
