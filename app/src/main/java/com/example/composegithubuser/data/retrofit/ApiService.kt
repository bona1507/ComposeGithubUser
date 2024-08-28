package com.example.composegithubuser.data.retrofit

import com.example.composegithubuser.data.entity.GithubResponse
import com.example.composegithubuser.data.entity.UserItem
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {
    @GET("search/users")
    suspend fun getUser2(
        @Query("q") q: String
    ) : GithubResponse

    @GET("users/{username}")
    suspend fun getDetailUser2(
        @Path("username") username: String
    ) : UserItem
}