package com.example.githubsearch

import android.support.v7.widget.SearchView
import android.util.Log
import com.example.githubsearch.model.Items
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Predicate
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class GitHubRepository() {
    var TAG: String = "MainActivity"
    lateinit var viewHolder: ChapterAdapter.ViewHolder
    private var mCompositeDisposable: CompositeDisposable? = null

    fun getData(searchView: SearchView) {
        mCompositeDisposable = CompositeDisposable()

        ObServableSearchView.of(searchView)
            .debounce(300, TimeUnit.MILLISECONDS)
            .filter(object : Predicate<String> {
                @Throws(Exception::class)
                override fun test(text: String): Boolean {
                    return if (text.isEmpty()) {
                        Log.d(TAG, "isEmpty: $text")

                        false
                    } else {
                        Log.d(TAG, "onQueryTextSubmit: $text")
                        beginSearch(text, searchView, emptyList())
                        true
                    }
                }
            })
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnEach {
            }
            .doOnError {
                Log.d(TAG, "Error")
            }
            .retry()
            .subscribe({
                Log.d(TAG, "subs")
            }, {
                Log.d(TAG, it.toString())
            })
    }


    fun beginSearch(query: String, searchView: SearchView, i: List<Items>) {
        // todo fetching data is repository or interactor  responsibility- refactor it
        // todo building retrofit api should be in the ApiClient.kt and rename it to something more meaningful

        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.github.com/")
            .build()

        viewHolder = ChapterAdapter.ViewHolder(searchView)

        val gHubAPI = retrofit.create(GitHubSearchService::class.java)
        mCompositeDisposable?.add(
            gHubAPI.searchGitHubRepo(query + "language:assembly", "stars", "desc")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { it -> it.items = i }
        )
    }

    fun handleError(error: Throwable) {
        Log.d(TAG, error.localizedMessage)
    }
}