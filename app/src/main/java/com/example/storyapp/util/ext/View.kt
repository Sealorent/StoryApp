package com.example.storyapp.util.ext

import android.widget.ImageView
import com.bumptech.glide.Glide


fun ImageView.setImageUrl(url: String, isCenterCrop: Boolean) {
    Glide.with(context)
        .load(url)
        .centerCrop()
        .into(this)
}