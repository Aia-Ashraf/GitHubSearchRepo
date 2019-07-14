package com.example.githubsearch

import com.example.githubsearch.model.BaseModel

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubSearchService {

    @GET("search/repositories")
    fun searchGitHubRepo(
        @Query("q")         searchParam : String,
//        @Query("page")      page : Int,
        @Query("sort")      sort : String,
        @Query("order")     order : String ) : Call<BaseModel>

    // todo you can return an observable and push it where you want
}

