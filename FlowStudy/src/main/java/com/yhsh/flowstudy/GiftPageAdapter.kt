package com.yhsh.flowstudy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.Math.ceil

class GiftPageAdapter(
    private val onItemClick: (Gift) -> Unit
) : RecyclerView.Adapter<GiftPageAdapter.PageViewHolder>() {

    private var allGifts: List<Gift> = emptyList()

    inner class PageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val recyclerView: RecyclerView = itemView.findViewById(R.id.rvGiftGrid)
        private val adapter = GiftAdapter(onItemClick)

        init {
            recyclerView.layoutManager = GridLayoutManager(itemView.context, 4)
            recyclerView.adapter = adapter
        }

        fun bind(pageGifts: List<Gift>) {
            adapter.submitList(pageGifts)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gift_page, parent, false)
        return PageViewHolder(view)
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        val start = position * ITEMS_PER_PAGE
        val end = minOf(start + ITEMS_PER_PAGE, allGifts.size)
        holder.bind(allGifts.subList(start, end))
    }

    override fun getItemCount(): Int {
        return ceil(allGifts.size.toDouble() / ITEMS_PER_PAGE).toInt()
    }

    fun submitList(newList: List<Gift>) {
        allGifts = newList
        notifyDataSetChanged()
    }

    companion object {
        const val ITEMS_PER_PAGE = 8
    }
}