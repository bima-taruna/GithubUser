package com.app.bima.githubuser.utility

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadImage(url:String?) { Glide.with(this.context)
    .load(url)
    .circleCrop()
    .into(this)
}