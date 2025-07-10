package edu.ucne.geremy_delossantos_p2_ap2.remote

import edu.ucne.geremy_delossantos_p2_ap2.remote.dto.ContributorDto
import edu.ucne.geremy_delossantos_p2_ap2.remote.dto.RepositoryDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubApi {
    @GET("users/enelramon/repos")
    suspend fun listRepos(): Response<List<RepositoryDto>>

    @GET("repos/{owner}/{repo}/contributors")
    suspend fun getContributors(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Response<List<ContributorDto>>
}