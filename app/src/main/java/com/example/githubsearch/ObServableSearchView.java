package com.example.githubsearch;

import android.support.v7.widget.SearchView;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class ObServableSearchView {

    public static Observable<String> of(SearchView searchView) {

        final PublishSubject<String> subject = PublishSubject.create();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                subject.onComplete();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                subject.onNext(text);
                return true;
            }
        });

        return subject;
    }
}