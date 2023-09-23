package com.example.newsapp.ui.home.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.api.modul.newsResponse.News
import com.example.newsapp.databinding.ItemNewsBinding

class NewsAdapter(var newsList: List<News?>? = null) :
    RecyclerView.Adapter<NewsAdapter.ViewHolder>() {
    class ViewHolder(val itemBinding: ItemNewsBinding) : RecyclerView.ViewHolder(itemBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewBinding = ItemNewsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(viewBinding)
    }

    override fun getItemCount(): Int {
        return newsList?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val news = newsList!![position]
        holder.itemBinding.news = news
        holder.itemBinding.invalidateAll()

//        holder.itemBinding.title.text = news?.title
//        holder.itemBinding.description.text = news?.description
//        Glide.with(holder.itemView)
//            .load(news?.urlToImage)
//            .placeholder(R.drawable.ic_launcher_background)
//            .into(holder.itemBinding.image)
    }

    fun bindNews(articles: List<News?>?) {
        newsList = articles
        notifyDataSetChanged()
    }
}