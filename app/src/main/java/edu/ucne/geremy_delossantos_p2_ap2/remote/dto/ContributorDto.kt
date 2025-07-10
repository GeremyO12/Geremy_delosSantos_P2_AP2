package edu.ucne.geremy_delossantos_p2_ap2.remote.dto

import com.squareup.moshi.Json

data class ContributorDto(
    @Json(name = "login") val login: String,
    @Json(name = "contributions") val contributions: Int,
    @Json(name = "avatar_url") val avatarUrl: String
)
