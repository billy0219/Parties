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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.concurrent.RecursiveTask;

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

    private ImageView mUserProfileImage;
    private TextView mUserProfileName;
    private TextView mUserProfileEmail;

    private String mUsername;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private DatabaseReference mDatabaseArticleReference;
    private DatabaseReference mUserInformationDatabaseReference;

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

        mFrameLayout = (FrameLayout) findViewById(R.id.fragment_container);
        initFragment();

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseArticleReference = FirebaseDatabase.getInstance().getReference().child("articlesList");

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

        mNavigationView = (NavigationView) findViewById(R.id.navView);
        mNavigationView.setNavigationItemSelectedListener(this);

        mUserProfileImage = (ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.userProfileImageView);
        mUserProfileName = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.userNameTextView);
        mUserProfileEmail = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.userEmailTextView);

        mUserInformationDatabaseReference = FirebaseDatabase.getInstance().getReference().child("userInformation");

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    mUserInformationDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String user_uid = mFirebaseAuth.getCurrentUser().getUid();
                            String user_email = dataSnapshot.child(user_uid).child("userEmail").getValue().toString();

                            if ( dataSnapshot.child(user_uid).hasChild("userPhoto") && dataSnapshot.child(user_uid).hasChild("userName")) {
                                String user_image_url = dataSnapshot.child(user_uid).child("userPhoto").getValue().toString();
                                String user_name = dataSnapshot.child(user_uid).child("userName").getValue().toString();

                                Picasso.with(getApplicationContext()).load(user_image_url).into(mUserProfileImage);
                                mUserProfileName.setText(user_name);
                            }

                            mUserProfileEmail.setText(user_email);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else {
                    Intent signInIntent = new Intent(MainActivity.this, MemberStatusActivity.class);
                    signInIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(signInIntent);
                }
            }
        };
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

//        mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        mToolbar.setTitleTextColor(Color.parseColor("#ffffff"));
//        setSupportActionBar(mToolbar);
//
//        ActionBar actionBar = getSupportActionBar();
//        assert actionBar != null;
//        actionBar.setDisplayHomeAsUpEnabled(true);

//        if (savedInstanceState == null ) {
//            RecyclerList recyclerList = new RecyclerList();
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            fragmentManager.beginTransaction().replace(R.id.fragment_container, recyclerList)
//                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//                    .commit();
//        }
    }

    private void initFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        RecyclerList recyclerList = new RecyclerList();
        if ( aviasalesFragment == null) {
            aviasalesFragment = (AviasalesFragment) AviasalesFragment.newInstance();
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, recyclerList).addToBackStack(null).commit();
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
            FavoriteRecyclerList favoriteRecyclerList = new FavoriteRecyclerList();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, favoriteRecyclerList)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(null)
                    .commit();

        } else if (id == R.id.nav_profileSetup) {
            Intent profileSetupIntent = new Intent (MainActivity.this, ProfileSetup.class);
            startActivity(profileSetupIntent);

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
