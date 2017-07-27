package group14.wheresmystuff.controller;

import android.support.v7.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;

import group14.wheresmystuff.R;
import group14.wheresmystuff.model.Model;
import group14.wheresmystuff.model.User;

/**
 * A login screen that offers login via username/password.
 */
public class LoginActivity extends AppCompatActivity {

    //private Model model = (Model) getApplication();
    ArrayList<User> userList = Model.getUserList();
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
//    private TextView forgotPassLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Where's My Stuff? - Login");
        setContentView(R.layout.activity_login);


        // Set up the login form.
        mUsernameView = (AutoCompleteTextView) findViewById(R.id.username);
//        forgotPassLink = (TextView) findViewById(R.id.forgotPassLink);
//        forgotPassLink.setMovementMethod(LinkMovementMethod.getInstance());

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button register = (Button) findViewById(R.id.registerButton);
        register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPage(RegistrationActivity.class);
            }
        });
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
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
//        if (!TextUtils.isEmpty(password)) {// && !isPasswordValid(password)) {
//            mPasswordView.setError(getString(R.string.error_invalid_password));
//            focusView = mPasswordView;
//            cancel = true;
//        }

        // Check for a valid username
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            mUsernameView.setError("This account does not exist");//, registering " + username + ":" + password);
            focusView = mUsernameView;
            //Model.getUserList().add(username + ":" + password);
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            mAuthTask = new UserLoginTask(username, password);
            mAuthTask.execute((Void) null);

        }
    }

    private boolean isUsernameValid(String username) {
        for (User user : Model.getUserList()) {
            //String[] pieces = ((String) credential).split(":");
            if (user.getLoginID().equals(username)) {
                // Account exists, return true
                return true;
            }
        }
        return false;
    }

    public void onForgetClick() {
        goToPage(RegistrationActivity.class);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;

        UserLoginTask(String username, String password) {
            mUsername = username;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            for (User user : Model.getUserList()) {
                //String[] pieces = ((String) credential).split(":");
                if (user.getLoginID().equals(mUsername)) {
                    // Account exists, return true if the password matches.
                    Model.setActiveUser(user);
                    return user.getPassword().equals(mPassword);
                }
            }
            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;

            if (success) {
                goToPage(MainActivity.class);
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }

    private void goToPage(Class next) {
        Intent intent = new Intent(this, next);
        startActivity(intent);
    }
}

