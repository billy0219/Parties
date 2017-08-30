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
import android.widget.EditText;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "SignUpActivity";
    private final String CHILD_USER = "Users";

    private EditText mNameEditText;
    private EditText mEmailEditText;
    private EditText mPwEditText;

    private Button mSignUpButton;
    private SignInButton mGoogleSignUpButton;

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseUserReference;

    private GoogleApiClient mGoogleApiClient;

    private ProgressDialog mSignUpProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mNameEditText = (EditText) findViewById(R.id.newMemberNameEditText);
        mEmailEditText = (EditText) findViewById(R.id.newMemberEmailEditText);
        mPwEditText = (EditText) findViewById(R.id.newMemberPwEditText);
        mSignUpButton = (Button) findViewById(R.id.signupButton);
        mGoogleSignUpButton = (SignInButton) findViewById(R.id.googleSignUpButton);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseUserReference = FirebaseDatabase.getInstance().getReference();

        mSignUpProgressDialog = new ProgressDialog(this);

        GoogleSignInOptions googleSignUpOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignUpOptions).build();

        // Sign Up with Email & Password
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(mNameEditText.getText())
                        || TextUtils.isEmpty(mEmailEditText.getText())
                        || TextUtils.isEmpty(mPwEditText.getText())) {
                    Toast.makeText(SignUpActivity.this, "Please, Enter your Information", Toast.LENGTH_SHORT).show();
                } else {
                    mSignUpProgressDialog.setMessage("Creating New Account..");
                    mSignUpProgressDialog.show();
                    mFirebaseAuth.createUserWithEmailAndPassword(mEmailEditText.getText().toString(), mPwEditText.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Users users = new Users(mEmailEditText.getText().toString());
                                        mDatabaseUserReference.child(CHILD_USER).push().setValue(users);
                                        mSignUpProgressDialog.dismiss();
                                        Intent signUpIntent = new Intent(SignUpActivity.this, MainActivity.class);
                                        signUpIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(signUpIntent);
                                        overridePendingTransition(R.anim.fade, R.anim.hold);
                                    }
                                }
                            });

                }
            }
        });

        mGoogleSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignUp();
            }
        });
    }

    private void googleSignUp() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            mSignUpProgressDialog.setMessage("Signing In..");
            mSignUpProgressDialog.show();
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                mSignUpProgressDialog.dismiss();
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
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            Users users = new Users(mFirebaseAuth.getCurrentUser().getEmail());
                            mDatabaseUserReference.child(CHILD_USER).push().setValue(users);
                            Intent signInIntent = new Intent(SignUpActivity.this, MainActivity.class);
                            signInIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(signInIntent);
                            overridePendingTransition(R.anim.fade, R.anim.hold);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        mSignUpProgressDialog.dismiss();
                        // ...
                    }
                });
    }
}