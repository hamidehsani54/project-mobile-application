package com.ltu.m7019eblogapp.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (
    @Json(name = "_id")
    var id: String,
    @Json(name = "auth0_id")
    var auth0_id: String = "",
    @Json(name = "username")
    var username: String,
    @Json(name = "email")
    var email: String = "",
    @Json(name = "profile_picture")
    var profilePicture: String,
    @Json(name = "createdAt")
    var createdAt: String = ""
    //var auth0_id: String = "",
        ) : Parcelable

@Parcelize
data class MiniUser (
    @Json(name = "username")
    val name : String
) : Parcelable