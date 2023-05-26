package com.ltu.m7019eblogapp.model;

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    @Json(name = "_id")
    var id: String = "",
    @Json(name = "name")
    var name: String,
    @Json(name = "description")
    var desc: String = ""
    ) : Parcelable {
        override fun toString(): String {
            return name
        }
    }
