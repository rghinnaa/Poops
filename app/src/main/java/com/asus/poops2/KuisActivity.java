package com.asus.poops2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class KuisActivity extends AppCompatActivity  implements View.OnClickListener  {

    private String TAG = KuisActivity.class.getSimpleName();


    public static String url = "http://192.168.43.132:8000/api/soal_quiz";

    private ProgressDialog pDialog;
    private ArrayList<String> soal, jawab, jawab2, soalll,benar;
    private Random random;
    String[] jaw;
    int no = 1, r, skor;
    private ListView lv;
    Button pilA, pilB, pilC, pilD;
    TextView soalnya;
    String method;
    ArrayList<HashMap<String, String>> contactList;
    ArrayList<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kuis);

        method = "GET";
        soalll = new ArrayList<>();
        jawab = new ArrayList<>();
        jawab2 = new ArrayList<>();
        benar = new ArrayList<>();
        jaw = new String[20];

            soalnya = (TextView) findViewById(R.id.tampilsoal);
            pilA = (Button) findViewById(R.id.jawA);
            pilA.setOnClickListener(this);
            pilB = (Button) findViewById(R.id.jawB);
            pilB.setOnClickListener(this);
            pilC = (Button) findViewById(R.id.jawC);
            pilC.setOnClickListener(this);
            pilD = (Button) findViewById(R.id.jawD);
            pilD.setOnClickListener(this);

         new GetSoal().execute();

    }

    public void onClick(View v){
        Button pilihan = (Button) v;
        if(no>=19){
            Intent i = new Intent(KuisActivity.this, ScoreActivity.class);
            i.putExtra("skor", skor);
            startActivity(i);
        }
        else{
            if(pilihan.getText().equals(benar.get(no-1))){
                no++;
                skor += 5;
                restart();
            }
            else{
                no++;
                skor += 0;
                new GetSoal().execute();
            }
        }
    }

    void restart() {
        soalnya.setText(soalll.get(no-1));
        pilA.setText(jawab.get(no-1));
        pilB.setText(jawab.get(no-1));
        pilC.setText(jawab.get(no-1));
        pilD.setText(jawab.get(no-1));

    }

    private class GetSoal extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(KuisActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, method);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray quest = jsonObj.getJSONArray("values");

                    for (int i = 0; i < quest.length(); i++) {
                        JSONObject c = quest.getJSONObject(i);

                        String soal = c.getString("Soal");
                        String jawabA = c.getString("Jawaban1");
                        String jawabB = c.getString("Jawaban2");
                        String jawabC = c.getString("Jawaban3");
                        String jawabD = c.getString("Jawaban4");
                        String jawabbener = c.getString("Jawaban_Benar");

                        soalll.add(soal);
                        jawab.add(jawabA);
                        jawab.add(jawabB);
                        jawab.add(jawabC);
                        jawab.add(jawabD);
                        benar.add(jawabbener);

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
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
            new GetSoal().execute();

        }
    }
}

