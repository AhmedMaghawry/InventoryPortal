package com.ezzat.inventoryportal.View;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ezzat.inventoryportal.R;

public class ReturnActivity extends AppCompatActivity {

    private EditText itemNum;
    private TextView itemQuan;
    private TextView binLoc;
    private ImageButton wrong;
    private ImageButton right;
    private String idUser;
    private String idItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return);
        idUser = getIntent().getStringExtra("id");
        idItem = getIntent().getStringExtra("item");
        setupViews();
        setupActions();
        setupToolbar();
    }

    private void setupActions() {
        //Wrong
        wrong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToHome();
            }
        });

        //Right
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itmID = itemNum.getText().toString();
                String quan = itemQuan.getText().toString();
                //ToDo: Check if in table
                goToHome();
            }
        });
    }

    private void goToHome() {
        Intent intent = new Intent(ReturnActivity.this, HomeActivity.class);
        intent.putExtra("id", idUser);
        startActivity(intent);
        finish();
    }

    private void setupViews() {
        //TODO: Add values to views
        itemNum = findViewById(R.id.itm);
        if (idItem != null) {
            itemNum.setText(idItem);
        }
        itemQuan = findViewById(R.id.qn);
        wrong = findViewById(R.id.cross);
        right = findViewById(R.id.tick);
        binLoc = findViewById(R.id.bin);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }
}
