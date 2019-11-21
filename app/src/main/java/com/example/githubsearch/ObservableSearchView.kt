package com.example.githubsearch

import android.content.ContentValues.TAG
import android.support.v7.widget.SearchView
import android.util.Log
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

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