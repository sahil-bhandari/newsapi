package com.sahil.demo.main

import Articles
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.sahil.demo.R
import com.sahil.demo.utility.WebViewActivity
import kotlinx.android.synthetic.main.item_home.view.*

class PostsAdapter(): PagedListAdapter<Articles, PostsAdapter.PostViewHolder>(POST_COMPARATOR) {

    companion object {
        val POST_COMPARATOR = object : DiffUtil.ItemCallback<Articles>() {
            override fun areContentsTheSame(oldItem: Articles, newItem: Articles): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: Articles, newItem: Articles): Boolean =
                oldItem.title == newItem.title
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_home,
            parent,
            false
        )
        return PostViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))

    }

//    interface OnItemClickListener {
//        fun onClick(Item: Articles?)
//    }

    class PostViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(newspost: Articles?) {
            itemView.title.text = newspost?.title
            itemView.body.text = if (newspost?.description?.isNotEmpty() == true)
                newspost.description else newspost?.url

            val requestOptions: RequestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)

            Glide.with(itemView)
                .load(newspost?.urlToImage)
                .apply(requestOptions)
                .into(itemView.image)

            itemView.cardview.setOnClickListener {
//                listener.onClick(redditPost)
//
                val intent = Intent(itemView.context, WebViewActivity::class.java)
                val bundle = Bundle()
                bundle.putString("link", newspost!!.url)
                intent.putExtras(bundle)
                itemView.context.startActivity(intent)
            }

        }
    }
}