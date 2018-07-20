package com.ezzat.inventoryportal.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.ezzat.inventoryportal.Controller.CustomAdapter;
import com.ezzat.inventoryportal.Model.CheckItem;
import com.ezzat.inventoryportal.Model.CreateItem;
import com.ezzat.inventoryportal.Model.CustomItem;
import com.ezzat.inventoryportal.Model.Items;
import com.ezzat.inventoryportal.Model.ReturnItem;
import com.ezzat.inventoryportal.Model.User;
import com.ezzat.inventoryportal.R;
import com.google.gson.Gson;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {


    private User user;
    private Items items;
    ArrayList<CustomItem> dataModels;
    ListView listView;
    private static CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        user = (User) getIntent().getSerializableExtra("user");
        items = (Items) getIntent().getSerializableExtra("items");
        setupToolbar();
        listView = findViewById(R.id.list);

        dataModels = new ArrayList<>();
        dataModels.addAll(loadCheckouts());
        dataModels.addAll(loadCreate());
        dataModels.addAll(loadReturn());
        adapter= new CustomAdapter(dataModels,getApplicationContext());
        listView.setAdapter(adapter);
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

    private void goToHome() {
        Intent intent = new Intent(CartActivity.this, HomeActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("items", items);
        startActivity(intent);
        finish();
    }

    public ArrayList<CheckItem> loadCheckouts() {
        ArrayList<CheckItem> res = new ArrayList<>();
        SharedPreferences pref = getApplication().getSharedPreferences("check", 0);
        Gson gson = new Gson();
        int size = pref.getInt("checksize", 0);
        for (int i = 0; i < size; i++) {
            String json = pref.getString((i+1)+"", "");
            CheckItem it = gson.fromJson(json, CheckItem.class);
            res.add(it);
        }
        return res;
    }

    public ArrayList<CreateItem> loadCreate() {
        ArrayList<CreateItem> res = new ArrayList<>();
        SharedPreferences pref = getApplication().getSharedPreferences("create", 0);
        Gson gson = new Gson();
        int size = pref.getInt("createsize", 0);
        for (int i = 0; i < size; i++) {
            String json = pref.getString((i+1)+"", "");
            CreateItem it = gson.fromJson(json, CreateItem.class);
            res.add(it);
        }
        return res;
    }

    public ArrayList<ReturnItem> loadReturn() {
        ArrayList<ReturnItem> res = new ArrayList<>();
        SharedPreferences pref = getApplication().getSharedPreferences("return", 0);
        Gson gson = new Gson();
        int size = pref.getInt("returnsize", 0);
        for (int i = 0; i < size; i++) {
            String json = pref.getString((i+1)+"", "");
            ReturnItem it = gson.fromJson(json, ReturnItem.class);
            res.add(it);
        }
        return res;
    }
}
