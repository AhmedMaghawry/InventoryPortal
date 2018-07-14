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

import com.ezzat.inventoryportal.Model.Item;
import com.ezzat.inventoryportal.Model.Items;
import com.ezzat.inventoryportal.R;

public class ReturnActivity extends AppCompatActivity {

    private EditText itemNum;
    private TextView itemQuan;
    private TextView binLoc;
    private ImageButton wrong;
    private ImageButton right;
    private String idUser;
    private Items items;
    private Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return);
        idUser = getIntent().getStringExtra("id");
        item = (Item) getIntent().getSerializableExtra("item");
        items = (Items) getIntent().getSerializableExtra("items");
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
                boolean b = items.updateItemID(item.getItemNumber(), itemNum.getText().toString(), getApplicationContext());
                if (b) {
                    items.saveItems(getApplicationContext());
                    goToHome();
                }
            }
        });
    }

    private void goToHome() {
        Intent intent = new Intent(ReturnActivity.this, HomeActivity.class);
        intent.putExtra("id", idUser);
        intent.putExtra("items", items);
        startActivity(intent);
        finish();
    }

    private void setupViews() {
        itemNum = findViewById(R.id.itm);
        itemNum.setText(item.getItemNumber());
        itemQuan = findViewById(R.id.qn);
        itemQuan.setText(item.getQuan());
        wrong = findViewById(R.id.cross);
        right = findViewById(R.id.tick);
        binLoc = findViewById(R.id.bin);
        binLoc.setText(item.getBin());
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToHome();
            }
        });
        final ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }
}
