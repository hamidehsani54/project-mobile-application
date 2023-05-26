package com.ltu.m7019eblogapp.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("loadImageFromUrl")
// It is used to bind an image URL to an ImageView using Glide library.
fun bindImage(imgView: ImageView, imgUrl: String){
    imgUrl.let {
        // Use the Glide library to load the image from the URL into the ImageView.
        Glide.with(imgView).load(imgUrl).into(imgView)
    }
}