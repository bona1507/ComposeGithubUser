package com.example.composegithubuser.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.composegithubuser.data.entity.UserItem
import com.example.composegithubuser.data.retrofit.ApiService
import com.example.composegithubuser.data.room.FUDao
import com.example.composegithubuser.data.room.FUEntity
import com.example.composegithubuser.utils.wrapEspressoIdlingResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class UserRepository private constructor(
    private val apiService: ApiService,
    private val favoriteUserDao: FUDao
) {

    fun getListUsers(query: String): Flow<List<UserItem>> = flow {
        wrapEspressoIdlingResource {
            emit(apiService.getUser2(query).items)
        }
    }

    fun getDetailUser(username: String): Flow<UserItem> = flow {
        wrapEspressoIdlingResource {
            emit(apiService.getDetailUser2(username))
        }
    }

    suspend fun addFavorite(favoriteUser: FUEntity) {
        favoriteUserDao.addFavorite(favoriteUser)
    }

    fun getAllFavorite(): Flow<List<FUEntity>> = flow {
        emit(favoriteUserDao.getAllFavoriteUsers())
    }

    fun isUserFavorite(username: String): LiveData<Boolean> =
        favoriteUserDao.isUserFavorite(username)

    suspend fun deleteFavorite(username: String) {
        favoriteUserDao.deleteFavorite(username)
    }

    companion object {
        private const val TAG = "User Repository"
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(
            apiService: ApiService,
            favoriteUserDao: FUDao
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, favoriteUserDao)
            }.also { instance = it }
    }
}
