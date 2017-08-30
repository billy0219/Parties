package com.now.parties;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.github.rubensousa.floatingtoolbar.FloatingToolbar;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.robertsimoes.shareable.Shareable;

import ru.aviasales.template.ui.fragment.AviasalesFragment;


public class ArticleContents extends Fragment {

    private final String CHILD_USER_FAVORITE = "User_Favorite";

    private WebView mArticleContentView;
    private AviasalesFragment aviasalesFragment;

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseArticleReference;
    private DatabaseReference mUserFavoriteArticleDBReference;

    private String mArticleKey;
    private String mArticleName;
    private String mArticleUrl;

    private FloatingToolbar mFloatingToolbar;
    private FloatingActionButton mFloatingActionButton;

    public ArticleContents() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article_contents, container, false);

        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mFloatingToolbar = (FloatingToolbar) getView().findViewById(R.id.floatingToolbar);
        mFloatingActionButton = (FloatingActionButton) getView().findViewById(R.id.fab);

        mFloatingToolbar.attachFab(mFloatingActionButton);
        mFloatingToolbar.setClickListener(new FloatingToolbar.ItemClickListener() {
            @Override
            public void onItemClick(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.FabBookingButton) {
                    aviasalesFragment = (AviasalesFragment) AviasalesFragment.newInstance();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.findFragmentByTag(AviasalesFragment.TAG);
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, aviasalesFragment, AviasalesFragment.TAG)
                            .addToBackStack(null)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .commit();
                } else if ( id == R.id.FabShareButton ) {
                    Shareable shareAction = new Shareable.Builder(getActivity())
                            .message(mArticleName)
                            .url(mArticleUrl)
                            .socialChannel(Shareable.Builder.TWITTER)
                            .build();
                    shareAction.share();
                } else if ( id == R.id.FabFavoriteButton ) {

                }
            }

            @Override
            public void onItemLongClick(MenuItem item) {

            }
        });

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

