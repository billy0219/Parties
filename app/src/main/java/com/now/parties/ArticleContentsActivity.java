package com.now.parties;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ArticleContentsActivity extends AppCompatActivity {

    private WebView mArticleContentView;
    private Toolbar mToolbar;


    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private DatabaseReference mDatabaseArticleReference;

    private String mArticleKey;
    private String mArticleName;
    private String mArticleUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_contents);

        mArticleContentView = (WebView) findViewById(R.id.articleWebView);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(mToolbar);

        mToolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        mDatabaseArticleReference = FirebaseDatabase.getInstance().getReference().child("articles");
        mArticleKey = getIntent().getExtras().getString("article_key");
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
//dddd

    }

}
