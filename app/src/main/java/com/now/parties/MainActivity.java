package com.now.parties;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String ANONYMOUS = "anonymous";

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private FloatingActionButton mFloatingActionButton;
    private NavigationView mNavigationView;
    private FrameLayout mFrameLayout;

    private String mUsername;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private DatabaseReference mDatabaseArticleReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUsername = ANONYMOUS;

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerMainLayout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.navView);
        mNavigationView.setNavigationItemSelectedListener(this);

        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseArticleReference = FirebaseDatabase.getInstance().getReference().child("articles");

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        mFrameLayout = (FrameLayout) findViewById(R.id.RecyclerFrameLayout);
        if (savedInstanceState == null ) {
            RecyclerList recyclerList = new RecyclerList();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, recyclerList);
            fragmentTransaction.commit();
        }


        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                } else {
                    Intent signInIntent = new Intent(MainActivity.this, MemberStatusActivity.class);
                    signInIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(signInIntent);
                }
            }
        };
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerMainLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_placeMenu) {
            RecyclerList recyclerList = new RecyclerList();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, recyclerList);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_favoriteMenu) {

        } else if (id == R.id.nav_worldMap) {

        } else if (id == R.id.nav_signOut) {
            AuthUI.getInstance().signOut(this);
            Intent sign_out_intent = new Intent(MainActivity.this, MemberStatusActivity.class);
            startActivity(sign_out_intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerMainLayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
}
