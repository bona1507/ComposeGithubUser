package com.example.composegithubuser.data.room

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class FUEntity (
    @PrimaryKey
    @ColumnInfo(name = "username")
    var username: String = "",

    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "profileUrl")
    var profileUrl: String? = null,

    @ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean = true,

    @ColumnInfo(name = "followers")
    var followers: String? = null,

    @ColumnInfo(name = "following")
    var following: String? = null
) : Parcelable