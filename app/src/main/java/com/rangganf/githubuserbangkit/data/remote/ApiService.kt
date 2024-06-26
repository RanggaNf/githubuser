package com.rangganf.githubuserbangkit.data.remote

import com.rangganf.githubuserbangkit.BuildConfig
import com.rangganf.githubuserbangkit.model.DetailResponse
import com.rangganf.githubuserbangkit.model.UserResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface ApiService {

    // Get a list of GitHub users.
    @JvmSuppressWildcards
    @GET("users")
    suspend fun getUserGithub(
        @Header("Authorization")
        authorization: String = BuildConfig.TOKEN // Authorization token for accessing the API.
    ): MutableList<UserResponse.Item>

    // Get details of a specific GitHub user.
    @JvmSuppressWildcards
    @GET("users/{username}")
    suspend fun getDetailUserGithub(
        @Path("username") username: String, // Username of the GitHub user.
        @Header("Authorization")
        authorization: String = BuildConfig.TOKEN // Authorization token for accessing the API.
    ): DetailResponse

    // Get followers of a specific GitHub user.
    @JvmSuppressWildcards
    @GET("/users/{username}/followers")
    suspend fun getFollowersUserGithub(
        @Path("username") username: String, // Username of the GitHub user.
        @Header("Authorization")
        authorization: String = BuildConfig.TOKEN // Authorization token for accessing the API.
    ): MutableList<UserResponse.Item>

    // Get users that a specific GitHub user is following.
    @JvmSuppressWildcards
    @GET("/users/{username}/following")
    suspend fun getFollowingUserGithub(
        @Path("username") username: String, // Username of the GitHub user.
        @Header("Authorization")
        authorization: String = BuildConfig.TOKEN // Authorization token for accessing the API.
    ): MutableList<UserResponse.Item>

    // Search for GitHub users based on query parameters.
    @JvmSuppressWildcards
    @GET("search/users")
    suspend fun searchUserGithub(
        @QueryMap params: Map<String, Any>, // Query parameters for searching users.
        @Header("Authorization")
        authorization: String = BuildConfig.TOKEN // Authorization token for accessing the API.
    ): UserResponse
}
