package com.example.githubsearch

import android.support.v7.widget.SearchView
import android.util.Log
import android.widget.Toast
import com.example.githubsearch.model.BaseModel
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

import android.content.ContentValues.TAG
import com.example.githubsearch.model.Items
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.internal.util.HalfSerializer.onComplete
import io.reactivex.internal.util.HalfSerializer.onNext
import io.reactivex.observers.DisposableObserver
import io.reactivex.plugins.RxJavaPlugins.onError
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ObServableSearchView {
    lateinit var viewHolder: ChapterAdapter.ViewHolder
    lateinit var searchView: SearchView
    private var mCompositeDisposable: CompositeDisposable? = null
    lateinit var adapter: ChapterAdapter
    lateinit var mainActivity: MainActivity
    lateinit var gitHubSearchService: GitHubSearchService





    fun of(searchView: SearchView): Observable<String> {
        val subject = PublishSubject.create<String>()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(s: String): Boolean {
                subject.onComplete()
                Log.d(TAG, "onQueryTextSubmit: \$text")
                return true
            }

            override fun onQueryTextChange(text: String): Boolean {
                subject.onNext(text)
                Log.d(TAG, "onQueryTextSubmit: change")
                return true
            }
        })
        return subject
    }
    }
