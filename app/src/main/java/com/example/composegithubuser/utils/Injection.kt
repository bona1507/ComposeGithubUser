package com.example.composegithubuser.utils

import android.content.Context
import com.example.composegithubuser.data.UserRepository
import com.example.composegithubuser.data.retrofit.ApiConfig
import com.example.composegithubuser.data.room.FURoom

object Injection {
    fun provideRepository(context: Context) : UserRepository {
        val apiService = ApiConfig.getApiService()
        val database = FURoom.getInstance(context)
        val dao = database.favoriteDao()
        return UserRepository.getInstance(apiService, dao)
    }
}