package com.example.githubsearch

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.widget.EditText
import com.example.githubsearch.model.BaseModel
import com.example.githubsearch.model.Items
import io.reactivex.android.plugins.RxAndroidPlugins
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/* todo 1- complete the task */
/*todo enhance design --> check the next urls to help you
* https://www.uplabs.com
* https://androidniceties.tumblr.com
* http://wsdesign.in
* https://dribbble.com/tags/material_design
* https://www.sketchappsources.com/tag/android.html
* https://colorwise.io */

class MainActivity : AppCompatActivity() {


    var TAG: String = "MainActivity"
    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter: ChapterAdapter
    lateinit var viewHolder: ChapterAdapter.ViewHolder
    lateinit var searchView: SearchView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.rv)
        searchView = findViewById(R.id.searchView)
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        adapter = ChapterAdapter(this, Items.getList())
        recyclerView.adapter = adapter

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        val searchItem = menu.findItem(R.id.searchView)
        if (searchItem != null) {
            val searchView = searchItem.actionView as SearchView
            searchView.queryHint = "Search View Hint"
            val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
            searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
            searchView.setIconifiedByDefault(false)

        }

        // todo while you already using rx in this project you can use it to enhance search michanism
        // todo check ObServableSearchView.kt and investigate in some operators like interval, throttle, debounce
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                Log.d(TAG, "onQueryTextChange: $newText")
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                Log.d(TAG, "onQueryTextSubmit: $query")
                beginSearch(query)
                return false
            }
        })
        return true
    }

    fun beginSearch(query: String) {
        // todo fetching data is repository or interactor  responsibility- refactor it
        // todo building retrofit api should be in the ApiClient.kt and rename it to something more meaningful

        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(
                RxJava2CallAdapterFactory.create()
            )
            .addConverterFactory(
                GsonConverterFactory.create()
            )

            .baseUrl("https://api.github.com/")
            .build()
        viewHolder = ChapterAdapter.ViewHolder(searchView)
        val gHubAPI = retrofit.create(GitHubSearchService::class.java)
        val call =
            gHubAPI.searchGitHubRepo(
                query + "language:assembly",
                "stars",
                "desc"
            )

        call.enqueue(object : Callback<BaseModel> {
            override fun onFailure(call: Call<BaseModel>, t: Throwable) {
                Log.d(TAG, "onFailure()", t)
            }

            override fun onResponse(call: Call<BaseModel>, response: Response<BaseModel>) {
                Log.d(TAG, "onResponse()" + response.body())
                response.body()?.items?.let { adapter.updateItems(it) } ?: run { Log.d(TAG, "empty data ") }
            }
        }
        )
    }
}