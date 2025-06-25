package com.yhsh.playandroid.page

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.yhsh.playandroid.R
import com.yhsh.playandroid.bean.Article

class ArticleAdapter() : RecyclerView.Adapter<ArticleAdapter.ArticleHolder>() {
    private var articleData: List<Article> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleHolder {
        val articleView = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_article_list_item, parent, false)
        return ArticleHolder(articleView)
    }

    override fun getItemCount(): Int {
        return articleData.size
    }

    override fun onBindViewHolder(holder: ArticleHolder, position: Int) {
        val article = articleData[position]
        Glide.with(holder.itemView.context).load(R.mipmap.ic_launcher).into(holder.ivHeaderImage)
        holder.tvHeaderTitle.text = article.title
        holder.tvHeaderType.text = "${article.shareUser}_${article.niceShareDate}"
    }

    class ArticleHolder(articleView: View) : ViewHolder(articleView) {
        var ivHeaderImage: ImageView
        var tvHeaderTitle: TextView
        var tvHeaderType: TextView

        init {
            ivHeaderImage = articleView.findViewById<ImageView>(R.id.iv_header_image)
            tvHeaderTitle = articleView.findViewById<TextView>(R.id.tv_header_title)
            tvHeaderType = articleView.findViewById<TextView>(R.id.tv_header_type)
        }
    }

    /**
     * 更新文章列表
     */
    fun updateArticle(articleData: List<Article>) {
        this.articleData = articleData
        notifyDataSetChanged()
    }
}

