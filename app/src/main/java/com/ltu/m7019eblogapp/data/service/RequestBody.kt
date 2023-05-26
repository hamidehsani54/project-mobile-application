package com.ltu.m7019eblogapp.data.service

import android.os.Parcelable
import com.ltu.m7019eblogapp.model.Category
import com.ltu.m7019eblogapp.model.Post
import com.ltu.m7019eblogapp.model.SubmittablePost
import com.ltu.m7019eblogapp.model.Tag
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

//------------------------------SEARCH---------------------------------------

@Parcelize
data class PostSearchBody(
    @Json(name = "search")
    var search : Post
) : Parcelable

@Parcelize
data class CategorySearchBody(
    @Json(name = "search")
    var search : Category
) : Parcelable

@Parcelize
data class TagSearchBody(
    @Json(name = "search")
    var search : Tag
) : Parcelable


//----------------------------CREATE--------------------------------------------

@Parcelize
data class PostCreateBody(
    @Json(name = "post")
    var post : SubmittablePost
) : Parcelable