/*
* Name : LoginActivity.java
* Author : Tae-Woong Youn & Henry Lee
*  This activity is for initial login screen of this application.
* A login screen offers login via username and password.
*/




package com.uoa.ece.p4p.ecelabmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.dd.processbutton.iml.ActionProcessButton;
import com.uoa.ece.p4p.ecelabmanager.api.Server;
import com.uoa.ece.p4p.ecelabmanager.api.exception.LoginFailed;
import com.uoa.ece.p4p.ecelabmanager.utility.ProgressGenerator;


public class LoginActivity extends Activity {

    private String TAG = "Part4ProjectLabApplication";

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mUsernameView;
    private EditText mPasswordView;
    private ActionProcessButton btnSignIn;
    private ProgressGenerator progressGenerator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mUsernameView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                Log.v(TAG,"onEditorAction Fired");
                if (id == R.id.login || id == EditorInfo.IME_ACTION_DONE || id==EditorInfo.IME_NULL) {
                    Log.v(TAG, "IME_ACTION_DONE Found");
                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(LoginActivity.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        progressGenerator = new ProgressGenerator(new ProgressGenerator.OnCompleteListener() {
            @Override
            public void onComplete() {
                //Do Nothing
            }
        });

        btnSignIn = (ActionProcessButton) findViewById(R.id.user_sign_in_button);
        btnSignIn.setMode(ActionProcessButton.Mode.ENDLESS);
        btnSignIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(LoginActivity.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                attemptLogin();
            }
        });
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid username address.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            mUsernameView.setError(getString(R.string.error_invalid_username));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(username, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isUsernameValid(String username) {
        return username != null && username.length() > 0;
    }

    private boolean isPasswordValid(String password) {
        return password != null && password.length() > 0;
    }

    public void showProgress(final boolean show) {
        if(show){
            btnSignIn.setProgress(0);
            progressGenerator.start(btnSignIn);
        }else{
            btnSignIn.setProgress(-1);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, String> {

        private final String mUsername;
        private final String mPassword;

        UserLoginTask(String username, String password) {
            mUsername = username;
            mPassword = password;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                Server.login(mUsername, mPassword);
                return "";
            } catch (LoginFailed e) {
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String error) {
            mAuthTask = null;
            if (error != null && error.isEmpty()) {
                Log.d(TAG, "Login ok.");
                Intent mIntent = new Intent(LoginActivity.this, CourseActivity.class).putExtra(Intent.EXTRA_TEXT, "Login ok.");
                mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mIntent);
                finish();
            } else {
                mPasswordView.setError("Login failed: " + error);
                mPasswordView.requestFocus();
                showProgress(false);
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}



