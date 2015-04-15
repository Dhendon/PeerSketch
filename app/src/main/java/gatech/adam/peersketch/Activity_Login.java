package gatech.adam.peersketch;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import data.Util;
import network.LoginController;


/**
 * A login screen that offers login via email/password.

 */
public class Activity_Login extends Activity {

    private static final String DUMMY_USERNAME = "peersketchtest";
    private static final String DUMMY_PASSWORD = "test";
    private String TAG = "login-activity";
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mUsernameView = (AutoCompleteTextView) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);
        mUsernameView.setText(DUMMY_USERNAME);
        mPasswordView.setText(DUMMY_PASSWORD);
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

        Button mUsernameSignInButton = (Button) findViewById(R.id.username_sign_in_button);
        Button mSkipLoginButton = (Button) findViewById(R.id.skip_login_button);
        mSkipLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent songEditorActivity = new Intent(context, Activity_Main.class);
                startActivity(songEditorActivity);
                finish();
            }
        });
        mUsernameSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        context = this;
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid username, missing fields, etc.), the
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
            mUsernameView.setError(getString(R.string.error_invalid_email));
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
        //TODO: Replace this with your own logic
        return username.length() > 0;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 0;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
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
            String user = mUsername;
            String pwd = mPassword;
            final List<JSONObject> responses = new ArrayList<JSONObject>();
            /*
             VERY IMPORTANT: Base64 on Android is different from Apache Base64
             must use Base64.NO_WRAP to emulate btoa from ES server.
             http://stackoverflow.com/questions/17912119/is-there-any-difference-between-apaches-base64-encodebase64-and-androids-base6
             */
            String pwdencode = Base64.encodeToString(pwd.getBytes(), Base64.NO_WRAP);
            String loginURL = Util.ES_LOGIN_BASE_URL + "/services/scripts/findall?username=" + user + "&password=" + pwdencode;

            //RequestQueue queue = Volley.newRequestQueue(context);
            Log.i(TAG, "Attempting login to:" + loginURL);

            LoginController controller = LoginController.getInstance(context);
            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                private boolean notified = false;

                @Override
                public void onResponse(JSONObject response) {
                    notified = true;
                    VolleyLog.d(TAG, "JSONObject response:" + response.toString());
                    responses.add(response);
                }

                @Override
                public String toString() {
                    return notified ? "notified" : "not notified";
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                private boolean notified = false;

                @Override
                public void onErrorResponse(VolleyError error) {
                    notified = true;
                    Toast.makeText(context, "Unable to connect to network!",
                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public String toString() {
                    return notified ? "notified" : "not notified";
                }
            };
            LoginStatusRequest loginStatusRequest = new LoginStatusRequest
                    (Request.Method.GET, loginURL, null, listener, errorListener);
            controller.getRequestQueue().add(loginStatusRequest);

            // TODO: make this not require overriding a toString to check for notification

            // This while loop is used to wait for a network response before returning.
            while (!errorListener.toString().equals("notified")
                    && !listener.toString().equals("notified")
                    && !loginStatusRequest.isCanceled()) {
            }
            final int SUCCESSFUL_LOGIN_CODE = 200;
            // TODO: register new user here.
            Log.i(TAG, "Request Status Code:" + loginStatusRequest.getStatusCode());
            return loginStatusRequest.getStatusCode() == SUCCESSFUL_LOGIN_CODE;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                //Intent homeActivity = new Intent(context, HomeActivity.class);
                //startActivity(homeActivity);
                // TODO: Go back to fragments after demo.
                Toast.makeText(context, mUsername + " logged in successfully", Toast.LENGTH_SHORT).show();
                Intent songEditorActivity = new Intent(context, Activity_Main.class);
                startActivity(songEditorActivity);
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    /**
     * This class is used to read the status code from the Request made to ES Servers.
     */
    private class LoginStatusRequest extends JsonObjectRequest {
        private int statusCode = -1;

        public LoginStatusRequest(int method, String url, JSONObject jsonRequest,
                                  Response.Listener<JSONObject> listener,
                                  Response.ErrorListener errorListener) {
            super(method, url, jsonRequest, listener, errorListener);
        }

        @Override
        protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
            statusCode = response.statusCode;
            return super.parseNetworkResponse(response);
        }

        public int getStatusCode() {
            return statusCode;
        }
    }

}




