package edu.psu.sweng888.nightout;

import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.Login;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private static final String GTAG = "GoogleSignIn";
    private static final String FTAG = "FacebookSignIn";
    private static final int RC_SIGN_IN = 9001;

    private TextView mGoogleTextView=null;

    private LoginButton mBtnLoginFacebook = null;
    private SignInButton mBtnLoginGoogle = null;

    private GoogleSignInClient mGoogleSignInClient=null;
    private CallbackManager mFacebookCallbackManager=null;

    private FirebaseAuth mAuth;
    private Intent callerIntent;

    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);

        mActionBar=getSupportActionBar();
        mActionBar.setTitle(R.string.login);


        mAuth = FirebaseAuth.getInstance();

        mBtnLoginFacebook = (LoginButton) findViewById(R.id.btn_login_facebook);
        mBtnLoginGoogle = (SignInButton) findViewById(R.id.btn_login_google);

        callerIntent=getIntent();

        getGoogleTextView();

        mBtnLoginFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
                if (!isLoggedIn) {
                    //Toast.makeText(LoginActivity.this, "Calling Facebook External API To Login", Toast.LENGTH_SHORT).show();
                    mFacebookCallbackManager = CallbackManager.Factory.create();
                    mBtnLoginFacebook.setReadPermissions("email", "public_profile");
                    signInFacebook();
                }
                else {
                    //Toast.makeText(LoginActivity.this, "Calling Facebook External API To Logout", Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                    mBtnLoginGoogle.setVisibility(View.VISIBLE);
                }
            }
        });
        mBtnLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((mGoogleTextView.getText().toString()).equals(getString(R.string.btn_login_google))){
                    //Toast.makeText(LoginActivity.this,"Calling Google External API to Login", Toast.LENGTH_SHORT).show();
                    GoogleSignInOptions gso =new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(getString(R.string.default_web_client_id))
                            .requestEmail()
                            .build();
                    mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);
                    signInGoogle();
                }
                else{
                    //Toast.makeText(LoginActivity.this,"Calling Google External API to Logout", Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                    mGoogleTextView.setText(R.string.btn_login_google);
                    mBtnLoginFacebook.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    private void getGoogleTextView(){
        for (int i = 0; i < mBtnLoginGoogle.getChildCount(); i++) {
            View v = mBtnLoginGoogle.getChildAt(i);

            if (v instanceof TextView) {
                mGoogleTextView = (TextView) v;
                mGoogleTextView.setText(R.string.btn_login_google);
                return;
            }
        }
    }
    private void signInGoogle(){
        Intent googleSignInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(googleSignInIntent,RC_SIGN_IN);
    }

    private void signInFacebook(){
        mBtnLoginFacebook.registerCallback(mFacebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(FTAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(FTAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d(FTAG, "facebook:onError", exception);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(GTAG, "Google sign in failed", e);
                // ...
            }
        }
        else {
            mFacebookCallbackManager.onActivityResult(requestCode,resultCode,data);
        }
    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(FTAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(FTAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(FTAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(GTAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(GTAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(GTAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }
    private void updateUI(FirebaseUser user){
        if (user != null) {
            for (UserInfo userInfo : FirebaseAuth.getInstance().getCurrentUser().getProviderData()) {
                if (userInfo.getProviderId().equals("google.com")) {
                    //Toast.makeText(LoginActivity.this, "USER is signed in with google", Toast.LENGTH_LONG).show();
                    mGoogleTextView.setText(R.string.btn_logout_google);
                    mBtnLoginFacebook.setVisibility(View.GONE);
                    if (callerIntent.hasExtra("class")){
                        finish();
                    }
                    else{
                        return;
                    }
                }
                else if (userInfo.getProviderId().equals("facebook.com")) {
                    //Toast.makeText(LoginActivity.this, "USER is signed in with facebook", Toast.LENGTH_LONG).show();
                    mBtnLoginFacebook.setText(R.string.btn_logout_facebook);
                    mBtnLoginGoogle.setVisibility(View.GONE);
                    if (callerIntent.hasExtra("class")){
                        finish();
                    }
                    else{
                        return;
                    }
                }
            }
            if (callerIntent.hasExtra("class")){
                finish();
            }
        }

    }
}
