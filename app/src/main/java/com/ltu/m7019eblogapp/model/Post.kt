package com.ltu.m7019eblogapp.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post(
    @Json(name = "_id")
    var id: String = "",
    @Json(name = "title")
    var title: String,
    @Json(name = "created_at")
    var created_at: String,
    @Json(name = "content")
    var content: String,
    @Json(name = "user_id")
    var userId: String,
    @Json(name = "category_id")
    var categoryId: String,
    @Json(name = "tags_id")
    var tagsId: List<String> = listOf(),
    @Json(name = "user")
    var user: User? = null,
    @Json(name= "category")
    var category: Category? = null,
    @Json(name = "tags")
    var tags: List<Tag>? = null,
    @Json(name = "media")
    var media: String,
) : Parcelable

@Parcelize
data class SubmittablePost(
    @Json(name = "title")
    var title: String,
    @Json(name = "content")
    var content: String,
    @Json(name = "category_id")
    var categoryId: String,
    @Json(name = "tags_id")
    var tagsId: List<String>,
    @Json(name = "media")
    var media: String,
) : Parcelable


/*
{
    "_id": "2j0h7ctiow2k5wq00amt2qb0",
    "title": "string",
    "created_at": "string",
    "content": "string",
    "user_id": "nr9823ubx4yofemxx6sr0x8o",
    "category_id": "ge4rd0x4l9qokuiwe3i93zig",
    "tags_id": [
    "8wqcqp7pxm8t7uz6a12pc0ay"
    ],
    "media": "string",
    "User": {
    "_id": "string",
    "username": "string",
    "profile_picture": "string"
    },
    "category": {
    "_id": "n2scf6j6nochf1o8pngmxs6m",
    "name": "string"
    },
    "tags": [
    {
        "_id": "2gbrgdtrge9jxnpdozww1y9e",
        "name": "string"
    }
    ]
}
 */