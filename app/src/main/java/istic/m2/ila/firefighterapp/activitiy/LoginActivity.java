package istic.m2.ila.firefighterapp.activitiy;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.rest.consumers.LoginConsumer;
import istic.m2.ila.firefighterapp.rest.RestTemplate;
import istic.m2.ila.firefighterapp.dto.LoginDTO;
import istic.m2.ila.firefighterapp.dto.TokenDTO;
import istic.m2.ila.firefighterapp.dto.UserDTO;
import retrofit2.Response;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {


    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = new UserLoginTask();
    private String TAG = this.getClass().getName();

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private boolean isRunning = false;
    private String errorAuthMessage;

    /**
     * Connexion en tant que Codis (true) ou Intervenant (false)
     */
    private boolean isCodis;
    SharedPreferences sharedPreferences;
    private String cleToken = "token";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);

        // Clic sur le bouton Entrer ou Next - sélectionne le TextView password
        mEmailView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_NEXT || id == EditorInfo.IME_NULL) {
                    mPasswordView.requestFocus();
                    return true;
                }
                return false;
            }
        });

        // TODO - A supprimer : valeur par defaut des champs admin/adin
        mEmailView.setText("admin");
        mPasswordView.setText("admin");


        // Bouton de connexion en tant qu'intervenant
        Button boutonIntervenant = (Button) findViewById(R.id.email_sign_in_intervenant);
        boutonIntervenant.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                isCodis = false;
                attemptLogin();
            }
        });

        // Bouton de connexion en tant que CODIS
        Button boutonCodis = (Button) findViewById(R.id.email_sign_in_CODIS);
        boutonCodis.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                isCodis = true;
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

    }



    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */

    private void attemptLogin() {
        if(isRunning) {
            return;
        }
        if(mAuthTask == null) {
            mAuthTask = new UserLoginTask();
        }
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
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
            mAuthTask.setmEmail(email);
            mAuthTask.setmPassword(password);
            Log.i(TAG, "Je me connecte");
            mAuthTask.execute();
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 3;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
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

        private String mEmail;
        private String mPassword;


        UserLoginTask() {
        }

        public void setmEmail(String mail) {
            this.mEmail = mail;
        }

        public void setmPassword(String pwd) {
            this.mPassword = pwd;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            isRunning = true;
            Boolean succes;
            RestTemplate template = RestTemplate.getInstance();
            LoginConsumer consumer = template.builConsumer(LoginConsumer.class);
            LoginDTO dto = new LoginDTO(mEmail, mPassword);
            UserDTO TokenDto = null;
            try {
                Response<TokenDTO> response = consumer.login(dto).execute();
                if(response != null && response.code() == HttpURLConnection.HTTP_OK) {
                    sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token", "Bearer " + response.body().getId_token());
                    editor.putBoolean("isCodis", isCodis);
                    editor.commit();
                    //editor.putString("token", );
                    Log.i(TAG,"token: "+response.body().getId_token());
                    Log.i(TAG, "storedToken: "+ sharedPreferences.getString("token", response.body().getId_token()));
                    succes = true;
                } else {
                    errorAuthMessage = "Le login ou le mot de passe est incorrect.";
                    Log.i(TAG, "response code : " + response.code());
                    succes = false;
                    return succes;
                }
            } catch (ConnectException e) {
                succes = false;
                errorAuthMessage = "Impossible de se connecter. Le serveur est indisponible pour l'instant.";
                e.printStackTrace();
            } catch (IOException e) {
                succes = false;
                errorAuthMessage = "Impossible de se connecter. Une erreur s'est produite.";
                e.printStackTrace();
            }

            // TODO: register the new account here.
            // En cas de succès
            return succes;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            showProgress(false);
            isRunning = false;
            mAuthTask = new UserLoginTask();
            if (success) {
                startActivity(new Intent(LoginActivity.this, NewListInterventionActivity.class));
            } else {
                Log.e(TAG, "you shall not pass");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, errorAuthMessage);
                        Toast.makeText(getApplicationContext(), errorAuthMessage, Toast.LENGTH_LONG)
                            .show();
                    }
                });
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

