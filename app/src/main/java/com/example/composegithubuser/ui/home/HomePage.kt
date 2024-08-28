package com.example.composegithubuser.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.composegithubuser.data.entity.UserItem
import com.example.composegithubuser.utils.State
import com.example.composegithubuser.utils.ItemError
import com.example.composegithubuser.utils.ItemLoading
import com.example.composegithubuser.utils.ItemSuccess
import com.example.composegithubuser.utils.ViewModelActivity
import com.example.composegithubuser.utils.ViewModelFactory

@Composable
fun HomePage(
    navigateToDetail: (String) -> Unit
) {
    val context = LocalContext.current
    val homeViewModel: ViewModelActivity = viewModel(
        factory = ViewModelFactory.getInstance(context)
    )
    val query by homeViewModel.query
    val mainState by homeViewModel.stateHome.collectAsState(initial = State.Loading)

    Column {
        MySearchBar(query = query, onQueryChange = homeViewModel::getListUsers)
        when (mainState) {
            is State.Loading -> ItemLoading()
            is State.Success -> HomeContent(
                listUser = (mainState as State.Success<List<UserItem>>).data,
                navigateToDetail = navigateToDetail
            )
            is State.Error -> ItemError()
        }
    }
}

@Composable
fun HomeContent(
    listUser: List<UserItem>,
    navigateToDetail: (String) -> Unit
) {
    if (listUser.isNotEmpty()) {
        ListUsers(listUser = listUser, navigateToDetail = navigateToDetail)
    } else {
        ItemError()
    }
}

@Composable
fun ListUsers(
    listUser: List<UserItem>,
    navigateToDetail: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.testTag("listUser"),
    ){
        items(listUser, key = {it.id} ) { user ->
            ItemSuccess(
                photoUrl = user.avatarUrl,
                username = user.login,
                onClick = { navigateToDetail(user.login) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MySearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    SearchBar(
        query = query,
        onQueryChange = onQueryChange,
        onSearch = {},
        active = false,
        onActiveChange = {},
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        shape = MaterialTheme.shapes.large,
        modifier = modifier
            .padding(10.dp)
            .fillMaxWidth()
            .heightIn(min = 20.dp),
        content = {}
    )
}
