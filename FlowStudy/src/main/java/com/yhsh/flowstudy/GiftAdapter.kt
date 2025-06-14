package com.yhsh.flowstudy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class GiftAdapter(
    private val onItemClick: (Gift) -> Unit
) : RecyclerView.Adapter<GiftAdapter.GiftViewHolder>() {

    private var gifts: List<Gift> = emptyList()

    inner class GiftViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val giftName: TextView = itemView.findViewById(R.id.tvGiftName)
        private val container: CardView = itemView.findViewById(R.id.cardContainer)

        fun bind(gift: Gift) {
            giftName.text = gift.giftName
            container.isSelected = gift.isSelected

            container.setOnClickListener {
                onItemClick(gift)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GiftViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gift, parent, false)
        return GiftViewHolder(view)
    }

    override fun onBindViewHolder(holder: GiftViewHolder, position: Int) {
        holder.bind(gifts[position])
    }

    override fun getItemCount() = gifts.size

    fun submitList(newList: List<Gift>) {
        gifts = newList
        notifyDataSetChanged()
    }
}