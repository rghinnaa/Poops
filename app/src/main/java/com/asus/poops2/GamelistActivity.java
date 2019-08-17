package com.asus.poops2;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.asus.poops2.MainActivity;

public class GamelistActivity extends AppCompatActivity implements View.OnClickListener {

    final Context context = this;
    ImageView btn1;
    TextView btn2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamelist);

        btn1 = findViewById(R.id.btnkuis);
        btn2 = findViewById(R.id.btnkuis2);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.buttomnavigation1);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Intent a = new Intent(context, MainActivity.class);
                        startActivity(a);
                        finish();
                        break;
                    case R.id.codes:
                        Intent b = new Intent(context, GamelistActivity.class);
                        startActivity(b);
                        finish();
                        break;
                }
                return false;
            }
        });

    }

    @Override
    public void onClick(View view) {
        Intent i = new Intent(this, KuisActivity.class);
        startActivity(i);
    }
}
