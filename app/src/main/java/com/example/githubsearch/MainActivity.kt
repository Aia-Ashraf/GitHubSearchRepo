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
import android.widget.Toast
import com.example.githubsearch.model.BaseModel
import com.example.githubsearch.model.Items
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import io.reactivex.ObservableSource
import android.arch.lifecycle.Transformations.switchMap
import android.support.design.widget.Snackbar
import io.reactivex.functions.Consumer
import io.reactivex.functions.Predicate
import java.util.concurrent.TimeUnit


/* todo 1- complete the task */
/*
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

   /*     searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                Log.d(TAG, "onQueryTextChange: $newText")
                return false
            }
            override fun onQueryTextSubmit(query: String): Boolean {
                Log.d(TAG, "onQueryTextSubmit: $query")
                beginSearch(query)
                return false
            }
        })*/
        return true
    }


    fun  handleResponse(baseModel: BaseModel) {
        Log.d(TAG, "onResponse()" + baseModel)
        baseModel?.items?.let { adapter.updateItems(it) }
    }



}