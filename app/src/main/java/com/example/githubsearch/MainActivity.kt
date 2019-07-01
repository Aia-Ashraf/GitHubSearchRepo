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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


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