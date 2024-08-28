package com.example.composegithubuser.utils

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.example.composegithubuser.data.UserRepository
import com.example.composegithubuser.data.entity.UserItem
import com.example.composegithubuser.data.room.FUEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ViewModelActivity(private val userRepository: UserRepository) : ViewModel() {

    private val _stateDetail = MutableStateFlow<State<UserItem>>(State.Loading)
    val stateDetail: StateFlow<State<UserItem>> = _stateDetail

    private val _stateHome = MutableStateFlow<State<List<UserItem>>>(State.Loading)
    val stateHome: StateFlow<State<List<UserItem>>> = _stateHome

    private val _stateFavorite = MutableStateFlow<State<List<FUEntity>>>(State.Loading)
    val stateFavorite: StateFlow<State<List<FUEntity>>> = _stateFavorite

    private val _query = mutableStateOf("")
    val query: MutableState<String> = _query

    init {
        getListUsers("bona")
    }

    fun getListUsers(newQuery: String) {
        _query.value = newQuery
        viewModelScope.launch {
            userRepository.getListUsers(_query.value)
                .catch { _stateHome.value = State.Error(it.message.toString()) }
                .collect { _stateHome.value = State.Success(it) }
        }
    }

    fun getAllFavoriteUser() {
        viewModelScope.launch {
            userRepository.getAllFavorite()
                .catch { _stateFavorite.value = State.Error(it.message.toString()) }
                .collect { _stateFavorite.value = State.Success(it) }
        }
    }

    fun getDetailUser(username: String) {
        viewModelScope.launch {
            userRepository.getDetailUser(username)
                .catch { _stateDetail.value = State.Error(it.message.toString()) }
                .collect { _stateDetail.value = State.Success(it) }
        }
    }

    private val favoriteUserData = MutableLiveData<FUEntity>()

    fun setFavoriteUser(favoriteUser: FUEntity) {
        favoriteUserData.value = favoriteUser
    }

    val favoriteStatus = favoriteUserData.switchMap {
        userRepository.isUserFavorite(it.username)
    }

    fun changeFavorite(favoriteUser: FUEntity) {
        viewModelScope.launch {
            if (favoriteStatus.value == true) {
                userRepository.deleteFavorite(favoriteUser.username)
            } else {
                userRepository.addFavorite(favoriteUser)
            }
        }
    }
}
