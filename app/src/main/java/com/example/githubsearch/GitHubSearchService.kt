package com.example.githubsearch

import com.example.githubsearch.model.BaseModel

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import io.reactivex.Observable;



interface GitHubSearchService {

    @GET("search/repositories")

    fun searchGitHubRepo(
        @Query("q")         searchParam : String,
//        @Query("page")      page : Int,
        @Query("sort")      sort : String,
        @Query("order")     order : String ) : Observable<BaseModel>

}

