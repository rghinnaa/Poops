package com.asus.poops2;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class IsimateriActivity extends AppCompatActivity {

    private static final String TAG = "Adapter";
    private static String url = "https://poops-api.000webhostapp.com/api/materi";
    private String method;
    private ProgressDialog pDialog;
    Context context;
    int nmateri = 0;
    TextView jud;
    TextView isii;
    String[] judul1;
    String[] isi1;
    WebView webView;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_isimateri);

        webView = findViewById(R.id.webview1);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.google.com");

        jud = (TextView) findViewById(R.id.JudulMateri);
        isii = (TextView) findViewById(R.id.penjelasanMateri);
        method = "GET";
        new GetMateri().execute();
    }
    private class GetMateri extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(IsimateriActivity.this);
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
                    judul1 = new String[materi.length()];
                    isi1 = new String[materi.length()];
                    // looping through All Contacts
                    for (int i = 0; i < materi.length(); i++) {
                        JSONObject c = materi.getJSONObject(i);

                        String judul = c.getString("title");
                        String isi = c.getString("body");

                        judul1[i] = judul;
                        isi1[i] = isi;
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
            String judull = getIntent().getExtras().getString("judduel");
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            Log.d(TAG, "Contents: preparing contents." );
            jud.setText(judull);
            for(int i=0; i<nmateri; i++){
                if(judul1[i].equals(judull)){
                    isii.setText(Html.fromHtml(isi1[i]));
                }
            }
        }

    }

}
