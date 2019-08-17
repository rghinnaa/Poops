package com.asus.poops2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.asus.poops2.HttpHandler;
import com.asus.poops2.R;
import com.asus.poops2.SplashActivity;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity implements OnClickListener {

    private String TAG = LoginActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private TextInputEditText et_username, et_password;
    private Button btn_login;


    private static String url, method, response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_username = findViewById(R.id.editText);
        et_password = findViewById(R.id.editText2);
        btn_login = findViewById(R.id.buttonSignIn);

        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonSignIn) {
            String username = et_username.getText().toString();
            String password = et_password.getText().toString();

            url = "https://poops-api.000webhostapp.com/api/login/" + username + "/" + password;
            method = "POST";

            new login_api().execute();
        }
    }

    private class login_api extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, method);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    response = jsonObj.getString("message");

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

            if (response.equals("Success")) {
                String username = et_username.getText().toString();
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                i.putExtra("nismasuk", username);
                startActivity(i);
                finish();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Username atau Password Salah!",
                        Toast.LENGTH_LONG)
                        .show();
            }
        }

    }
}

