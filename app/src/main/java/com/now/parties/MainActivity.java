package com.now.parties;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ru.aviasales.core.AviasalesSDK;
import ru.aviasales.core.identification.SdkConfig;
import ru.aviasales.template.ui.fragment.AviasalesFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String ANONYMOUS = "anonymous";
    private final static String TRAVEL_PAYOUTS_MARKER = "146985";
    private final static String TRAVEL_PAYOUTS_TOKEN = "d9c602e791a0b39bea0a979ef51ed608";
    private final static String SDK_HOST = "www.travel-api.pw";

    private AviasalesFragment aviasalesFragment;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
//    private FloatingToolbar mFloatingToolbar;
//    private FloatingActionButton mFloatingActionButton;

    private NavigationView mNavigationView;
    private FrameLayout mFrameLayout;
    private Toolbar mToolbar;

    private String mUsername;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private DatabaseReference mDatabaseArticleReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUsername = ANONYMOUS;

        AviasalesSDK.getInstance().init(this, new SdkConfig(TRAVEL_PAYOUTS_MARKER, TRAVEL_PAYOUTS_TOKEN, SDK_HOST));
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerMainLayout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        mToolbar = (Toolbar) findViewById(R.id.mainToolBar);
        mToolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

        mNavigationView = (NavigationView) findViewById(R.id.navView);
        mNavigationView.setNavigationItemSelectedListener(this);

//        mFloatingToolbar = (FloatingToolbar) findViewById(R.id.floatingToolbar);
//        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
//        mFloatingToolbar.attachFab(mFloatingActionButton);
//        mFloatingToolbar.setClickListener(new FloatingToolbar.ItemClickListener() {
//            @Override
//            public void onItemClick(MenuItem item) {
//
//            }
//
//            @Override
//            public void onItemLongClick(MenuItem item) {
//
//            }
//        });
//        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
//        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseArticleReference = FirebaseDatabase.getInstance().getReference().child("articles");
//        mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        mToolbar.setTitleTextColor(Color.parseColor("#ffffff"));
//        setSupportActionBar(mToolbar);
//
//        ActionBar actionBar = getSupportActionBar();
//        assert actionBar != null;
//        actionBar.setDisplayHomeAsUpEnabled(true);
        mFrameLayout = (FrameLayout) findViewById(R.id.fragment_container);
        if (savedInstanceState == null ) {
            RecyclerList recyclerList = new RecyclerList();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, recyclerList)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();

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
        if (!aviasalesFragment.onBackPressed()) {
            int count = getSupportFragmentManager().getBackStackEntryCount();
            if ( count == 0) {
                super.onBackPressed();
            } else {
                getSupportFragmentManager().popBackStack();
            }
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

        if ( id == R.id.nav_filghtSearch ) {
            aviasalesFragment = (AviasalesFragment) AviasalesFragment.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.findFragmentByTag(AviasalesFragment.TAG);
            fragmentManager.beginTransaction().replace(R.id.fragment_container, aviasalesFragment, AviasalesFragment.TAG)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(null)
                    .commit();

        } else if (id == R.id.nav_placeMenu) {
            RecyclerList recyclerList = new RecyclerList();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, recyclerList)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(null)
                    .commit();

        } else if (id == R.id.nav_favoriteMenu) {

        } else if (id == R.id.nav_worldMap) {

        } else if (id == R.id.nav_signOut) {
            mFirebaseAuth.signOut();
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

//    public void showFloatingActionButton() {
//        mFloatingActionButton.show();
//    };
//
//    public void hideFloatingActionButton() {
//        mFloatingActionButton.hide();
//    }
}
