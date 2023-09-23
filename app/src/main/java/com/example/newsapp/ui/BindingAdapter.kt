package com.example.newsapp.ui

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.newsapp.R

@BindingAdapter("app:url")
fun bindImageWithUrl(
    imageView: ImageView,
    url: String
) {
    Glide.with(imageView)
        .load(url)
        .placeholder(R.drawable.ic_launcher_background)
        .into(imageView)

}