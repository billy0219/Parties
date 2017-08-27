package com.now.parties;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ArticleContentsFragment extends Fragment {

    private WebView mArticleContentView;

    private DatabaseReference mDatabaseArticleReference;

    private String mArticleKey;
    private String mArticleName;
    private String mArticleUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.article_fragment, container, false);
    }



    @Override
    public void onActivityCreated(Bundle savedInstance) {
        super.onActivityCreated(savedInstance);

        mArticleContentView = (WebView) getView().findViewById(R.id.articleWebView);
        mDatabaseArticleReference = FirebaseDatabase.getInstance().getReference().child("articles");
        mArticleKey = getArguments().getString("article_key");
        mArticleName = null;
        mArticleUrl = null;

        mArticleContentView.getSettings().setJavaScriptEnabled(true);
        mArticleContentView.setWebViewClient(new WebViewClient());

        mDatabaseArticleReference.child(mArticleKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String article_main_name = (String) dataSnapshot.child("placeDesc").getValue();
                String article_url = (String) dataSnapshot.child("placeArticle").getValue();
                mArticleName = article_main_name;
                mArticleUrl = article_url;

                mArticleContentView.loadUrl(mArticleUrl);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
