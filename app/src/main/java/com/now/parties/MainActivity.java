package com.now.parties;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    public static final int RC_SIGN_IN = 1;
    public static final String ANONYMOUS = "anonymous";

    private RecyclerView mRecyclerView;

    private String mUsername;

    private GridLayoutManager mGridLayoutManager;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUsername = ANONYMOUS;

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mGridLayoutManager = new GridLayoutManager(this, 1);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getInstance().getReference();

        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(MainActivity.this, "You're signed in.", Toast.LENGTH_SHORT).show();
                } else {
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(
                                            Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<ViewItem, ArticlesViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<ViewItem, ArticlesViewHolder>(
                        ViewItem.class,
                        R.layout.list_row,
                        ArticlesViewHolder.class,
                        mDatabaseReference.child("articles")
                ){
                    @Override
                    protected void populateViewHolder(ArticlesViewHolder viewHolder, ViewItem model, int position) {

                        final String article_key = getRef(position).toString();

                        viewHolder.setPlaceName(model.getPlaceName());
                        viewHolder.setPlaceDesc(model.getPlaceDesc());
                        viewHolder.setPlaceImage(getApplicationContext(), model.getPlaceImage());

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                Intent singleArticleIntent = new Intent(MainActivity.this, ArticleSingleActivity.class );
                                singleArticleIntent.putExtra("article_id", article_key);
                                startActivity(singleArticleIntent);
                            }
                        });
                    }
                };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class ArticlesViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public ArticlesViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setPlaceName(String placeName) {
            TextView article_place_name = (TextView) mView.findViewById(R.id.placeTitleTextView);
            article_place_name.setText(placeName);
        }
        public void setPlaceDesc(String placeDesc) {
            TextView article_place_desc = (TextView) mView.findViewById(R.id.placeDescTextView);
            article_place_desc.setText(placeDesc);
        }
        public void setPlaceImage(Context context, String placeImage) {
            ImageView article_place_image = (ImageView) mView.findViewById(R.id.placeImageView);
            Picasso.with(context).load(placeImage).into(article_place_image);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.signOutMenu:
                AuthUI.getInstance().signOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    private void onSignedOutCleanup() {
        mUsername = ANONYMOUS;
    }
}
