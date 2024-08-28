package com.example.composegithubuser.ui.favorite

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.composegithubuser.data.room.FUEntity
import com.example.composegithubuser.utils.State
import com.example.composegithubuser.utils.ItemError
import com.example.composegithubuser.utils.ItemLoading
import com.example.composegithubuser.utils.ItemSuccess
import com.example.composegithubuser.utils.ViewModelActivity
import com.example.composegithubuser.utils.ViewModelFactory

@Composable
fun FavoritePage(
    navigateToDetail: (String) -> Unit
) {
    val context = LocalContext.current
    val viewModel: ViewModelActivity = viewModel(factory = ViewModelFactory.getInstance(context))
    val favoriteState by viewModel.stateFavorite.collectAsState(initial = State.Loading)

    when (favoriteState) {
        is State.Loading -> {
            ItemLoading()
            viewModel.getAllFavoriteUser()
        }
        is State.Success -> {
            if ((favoriteState as State.Success<List<FUEntity>>).data.isEmpty()) {
                ItemError()
            } else {
                FavoriteContent(
                    listUser = (favoriteState as State.Success<List<FUEntity>>).data,
                    navigateToDetail = navigateToDetail
                )
            }
        }
        is State.Error -> {
            ItemError()
        }
    }
}

@Composable
fun FavoriteContent(
    listUser: List<FUEntity>,
    navigateToDetail: (String) -> Unit
) {
    LazyColumn(modifier = Modifier.testTag("listUserFavorite")) {
        items(listUser, key = { it.username }) { user ->
            user.profileUrl?.let {
                ItemSuccess(
                    photoUrl = it,
                    username = user.username,
                    onClick = { navigateToDetail(user.username) }
                )
            }
        }
    }
}
