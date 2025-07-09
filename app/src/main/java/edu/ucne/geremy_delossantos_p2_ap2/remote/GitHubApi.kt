package edu.ucne.geremy_delossantos_p2_ap2.remote

import edu.ucne.geremy_delossantos_p2_ap2.remote.dto.RepositoryDto
import retrofit2.Response
import retrofit2.http.GET

interface GitHubApi {
    @GET("users/enelramon/repos")
    suspend fun listRepos(): Response<List<RepositoryDto>>
}