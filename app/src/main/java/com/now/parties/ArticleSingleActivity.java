package com.now.parties;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ArticleSingleActivity extends AppCompatActivity {

    private TextView mArticleMainTitle;
    private TextView mArticleSubTitle;
    private TextView mArticleContent;

    private ImageView mArticleMainImage;

    private Button mBookmarkButton;
    private Button mSearchButton;
    private Button mShareButton;

    private DatabaseReference mDatabaseReference;
    private String mArticle_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_single);

        mArticleMainTitle = (TextView) findViewById(R.id.articleMainTextView);
        mArticleSubTitle = (TextView) findViewById(R.id.articleSubTitleTextView);
        mArticleContent = (TextView) findViewById(R.id.articleContentTextView);

        mArticleMainImage = (ImageView) findViewById(R.id.articleMainImageView);

        mBookmarkButton = (Button) findViewById(R.id.bookmarkButton);
        mSearchButton = (Button) findViewById(R.id.searchButton);
        mShareButton = (Button) findViewById(R.id.shareButton);

        mArticle_id = getIntent().getExtras().getString("article_id");

        mDatabaseReference.child(mArticle_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String article_main_title = (String) dataSnapshot.child("placeDesc").getValue();
                String article_sub_title = (String) dataSnapshot.child("placeName").getValue();
                String article_main_image = (String) dataSnapshot.child("placeImage").getValue();

                mArticleMainTitle.setText(article_main_title);
                mArticleSubTitle.setText(article_sub_title);

                Picasso.with(ArticleSingleActivity.this).load(article_main_image).into(mArticleMainImage);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
