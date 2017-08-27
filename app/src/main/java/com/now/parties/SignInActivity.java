package com.now.parties;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "SignInActivity";
    private final String CHILD_USER = "Users";

    private TextView mExMemberEmail;
    private TextView mExMemberPassword;
    private Button mSignInButton;

    private SignInButton mGoogleSignInButton;

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mUserDatabaseReference;

    private GoogleApiClient mGoogleApiClient;

    private ProgressDialog mSignInProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mExMemberEmail = (TextView) findViewById(R.id.exMemberEmailEditText);
        mExMemberPassword = (TextView) findViewById(R.id.exMemberPwEditText);
        mSignInButton = (Button) findViewById(R.id.buttonToSignIn);

        mGoogleSignInButton = (SignInButton) findViewById(R.id.googleSignInButton) ;

        mSignInProgressDialog = new ProgressDialog(this);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mUserDatabaseReference = FirebaseDatabase.getInstance().getReference();

        mSignInButton.setOnClickListener(this);
        mGoogleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignIn();
            }
        });

        // Configure Google Sign In
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();
    }


    @Override
    public void onClick(View view) {
        if (TextUtils.isEmpty(mExMemberEmail.getText()) || TextUtils.isEmpty(mExMemberPassword.getText())) {
            Toast.makeText(SignInActivity.this, "Please, Enter your Email, Password", Toast.LENGTH_SHORT).show();
        } else {
            mSignInProgressDialog.setMessage("Signing In..");
            mSignInProgressDialog.show();
            mFirebaseAuth.signInWithEmailAndPassword(
                    mExMemberEmail.getText().toString(),
                    mExMemberPassword.getText().toString()
            ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Intent signInIntent = new Intent(SignInActivity.this, MainActivity.class);
                        signInIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(signInIntent);
                        overridePendingTransition(R.anim.fade, R.anim.hold);
                    } else {
                        mSignInProgressDialog.dismiss();
                        Toast.makeText(SignInActivity.this,
                                "Please, Sign Up", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    // Google Login Function Below...................

    private void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            mSignInProgressDialog.setMessage("Signing In..");
            mSignInProgressDialog.show();
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                mSignInProgressDialog.dismiss();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                            mUserDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild(mFirebaseAuth.getCurrentUser().getUid())) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithCredential:success");
                                        Intent signInIntent = new Intent(SignInActivity.this, MainActivity.class);
                                        signInIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(signInIntent);
                                    } else {
                                        Intent signInIntent = new Intent(SignInActivity.this, MainActivity.class);
                                        Users users = new Users(mFirebaseAuth.getCurrentUser().getEmail());
                                        mUserDatabaseReference.child(CHILD_USER).push().setValue(users);
                                        signInIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(signInIntent);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        mSignInProgressDialog.dismiss();
                        // ...
                    }
                });
    }
}
