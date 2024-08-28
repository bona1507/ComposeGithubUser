package com.example.composegithubuser.ui.profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.composegithubuser.R

@Composable
fun ProfilePage() {
    val photoUrl = stringResource(R.string.photo)
    val name = stringResource(R.string.name)
    val email = stringResource(R.string.email)
    val github = stringResource(R.string.github)
    val linkedin = stringResource(R.string.linkedin)
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        ProfileImage(photoUrl = photoUrl)
        ProfileText(text = name, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
        ProfileText(text = email)
        ProfileText(text = github) {
            openLinkInBrowser(context, github)
        }
        ProfileText(text = linkedin) {
            openLinkInBrowser(context, linkedin)
        }
    }
}

@Composable
private fun ProfileImage(photoUrl: String) {
    Image(
        painter = rememberAsyncImagePainter(photoUrl),
        contentDescription = stringResource(R.string.user_thumbnail),
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .padding(16.dp)
            .size(300.dp)
            .clip(CircleShape)
    )
}

@Composable
private fun ProfileText(
    text: String,
    fontWeight: FontWeight = FontWeight.Light,
    fontSize: TextUnit = 16.sp,
    onClick: () -> Unit = {}
) {
    Text(
        text = text,
        fontWeight = fontWeight,
        fontSize = fontSize,
        modifier = Modifier.clickable(onClick = onClick)
    )
}

fun openLinkInBrowser(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}
