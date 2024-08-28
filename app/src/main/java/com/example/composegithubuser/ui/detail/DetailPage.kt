package com.example.composegithubuser.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.composegithubuser.R
import com.example.composegithubuser.data.entity.UserItem
import com.example.composegithubuser.data.room.FUEntity
import com.example.composegithubuser.utils.State
import com.example.composegithubuser.utils.ItemError
import com.example.composegithubuser.utils.ItemLoading
import com.example.composegithubuser.utils.ViewModelActivity
import com.example.composegithubuser.utils.ViewModelFactory

@Composable
fun DetailPage(
    username: String,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: ViewModelActivity = viewModel(factory = ViewModelFactory.getInstance(context))
    val detailState by viewModel.stateDetail.collectAsState(initial = State.Loading)

    when (detailState) {
        is State.Loading -> {
            ItemLoading()
            viewModel.getDetailUser(username)
        }
        is State.Success -> {
            val user = (detailState as State.Success<UserItem>).data
            val favoriteUser = FUEntity(profileUrl = user.avatarUrl, username = user.login)
            viewModel.setFavoriteUser(favoriteUser)
            val favoriteStatus by viewModel.favoriteStatus.observeAsState(false)
            DetailContent(
                user = (detailState as State.Success<UserItem>).data,
                favoriteStatus = favoriteStatus,
                updateFavoriteStatus = {
                    viewModel.changeFavorite(favoriteUser)
                },
                onBackClick = onBackClick
            )
        }
        is State.Error -> {
            ItemError()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailContent(
    user: UserItem,
    favoriteStatus: Boolean,
    updateFavoriteStatus: () -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = { onBackClick() },
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                title = { Text(text = stringResource(id = R.string.detail_user)) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = updateFavoriteStatus,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.testTag("favoriteButton")
            ) {
                val icon = if (favoriteStatus) Icons.Default.Favorite else Icons.Default.FavoriteBorder
                Icon(imageVector = icon, contentDescription = null, tint = Color.White)
            }
        }
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            AsyncImage(
                model = user.avatarUrl,
                contentDescription = stringResource(id = R.string.user_thumbnail),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(16.dp)
                    .size(300.dp)
                    .clip(CircleShape)
            )
            Text(
                text = user.name?: "Username",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp
            )
            Text(
                text = user.login,
                fontWeight = FontWeight.Light,
                fontSize = 16.sp
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(64.dp)
            ) {
                Text(
                    text = stringResource(R.string.followers, user.followers),
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
                Text(
                    text = stringResource(R.string.following, user.following),
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            }
        }
    }
}
