package com.ltu.m7019eblogapp.data.service

import android.os.Parcelable
import com.ltu.m7019eblogapp.model.*
import com.ltu.m7019eblogapp.model.Tag
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize
import retrofit2.http.*

//TODO: Add @Headers({"Authorization", "Bearer "+ token}) for auth

interface BlogApiService {
    //-----------------------------Posts-------------------------
    @GET("posts")
    suspend fun getPosts(
        @Query("set")
        set: Int = 1
    ) : List<Post>
    @GET("posts/{post_id}")
    suspend fun getPost(
        @Path("post_id")
        post_id: String
    ) : Post
    @POST("posts")
    suspend fun createPost(
        @Header("authorization") accessToken: String,
        @Body post: PostCreateBody
    )
    /*
    TODO: Implement when login is done
    @PATCH("posts/{post_id}")
    suspend fun updatePost(
        @Path("post_id")
        post_id: String
    )
    @DELETE("posts/{post_id}")
    suspend fun deletePost(
        @Path("post_id")
        post_id: String
    )
     */

    //----------------------Users-------------------------------
    @GET("user")
    suspend fun getUsers(
        @Query("set")
        set: Int = 1
    ) : List<User>
    @GET("user/{user_id}")
    suspend fun getUser(
        @Path("user_id")
        user_id: String
    ) : User
    @GET("user/{user_id}/posts")
    suspend fun getUserPosts(
        @Path("user_id")
        user_id: String,
        @Query("set")
        set: Int = 1
    ) : List<Post>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("user/me")
    suspend fun getCurrentUser(
        @Header("Authorization") auth: String
    ) : User

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("user")
    suspend fun createUser(
        @Header("Authorization") auth: String,
        @Body miniUser : MiniUser,
    ) : User
    /*
    @PATCH("user/{user_id}")
    suspend fun patchUser()
    @DELETE("user/{user_id}")
    suspend fun deleteUser()
     */

    //--------------------Categories & Tags------------------------------
    @GET("categories")
    suspend fun getCategories(
        @Query("set")
        set: Int = 1
    ) : List<Category>
    @GET("categories/{category_id}")
    suspend fun getCategory(
        @Path("category_id")
        category_id: String
    ) : Category
    @GET("tags")
    suspend fun getTags(
        @Query("set")
        set: Int = 1
    ) : List<Tag>
    @GET("tags/{tag_id}")
    suspend fun getTag(
        @Path("tag_id")
        tag_id: String
    ) : Tag

    //------------------------Search--------------------------------------
    @POST("search/posts")
    suspend fun searchPost(
        @Body params: PostSearchBody,
        @Query("set")
        set: Int = 1
    ) : List<Post>

    @POST("search/categories")
    suspend fun searchCategory(
        @Body params: CategorySearchBody,
        @Query("set")
        set: Int = 1
    ) : List<Category>

    @POST("search/tags")
    suspend fun searchTag(
        @Body params: TagSearchBody,
        @Query("set")
        set: Int = 1
    ) : List<Tag>

}