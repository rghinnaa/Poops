package com.asus.poops2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.widget.DrawerLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProfilActivity extends AppCompatActivity {
    private static final String TAG = "RecyclerViewAdapter";
    private static String url = "https://poops-api.000webhostapp.com/api/siswa";
    private String method;
    private ProgressDialog pDialog;
    Context context;
    TextView niss, namaa, emaill, kelass;
    String username;

    String[] nis1;
    String[] nama1;
    String[] email1;
    String[] kelas1;
    int nmateri = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        method = "GET";

        niss = findViewById(R.id.Nis);
        namaa = findViewById(R.id.Nama);
        emaill = findViewById(R.id.Email);
        kelass = findViewById(R.id.Kelas);
        username = getIntent().getStringExtra("nismasuk2");

        new GetMateri().execute();
    }

    private class GetMateri extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(ProfilActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(url, method);
            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray materi = jsonObj.getJSONArray("values");
                    nmateri = materi.length();

                    nis1 = new String[materi.length()];
                    nama1 = new String[materi.length()];
                    email1 = new String[materi.length()];
                    kelas1 = new String[materi.length()];
                    for (int i = 0; i < materi.length(); i++) {
                        JSONObject c = materi.getJSONObject(i);

                        String nis = c.getString("NIS");
                        String nama = c.getString("Nama");
                        String email = c.getString("Email");
                        String kelas = c.getString("Kelas");

                        nis1[i] = nis;
                        nama1[i] = nama;
                        email1[i] = email;
                        kelas1[i] = kelas;

                    }
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
            if (pDialog.isShowing())
                pDialog.dismiss();
            Log.d(TAG, "Contents: preparing contents." );

            for (int i = 0; i < nmateri; i++) {
                if(nis1[i].equals(username)){
                    niss.setText(nis1[i]);
                    namaa.setText(nama1[i]);
                    emaill.setText(email1[i]);
                    kelass.setText(kelas1[i]);
                }
            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}


