package com.ezzat.inventoryportal.View;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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
import com.ezzat.inventoryportal.Model.ReturnItem;
import com.ezzat.inventoryportal.R;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ReturnActivity extends AppCompatActivity {

    private EditText itemNum;
    private EditText itemQuan;
    private TextView binLoc;
    private ImageButton wrong;
    private ImageButton right;
    private String idUser;
    private Items items;
    private Item item;
    private ProgressDialog pDialog;

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
                }
                new uploadReturn().execute(itemNum.getText().toString(), itemQuan.getText().toString(), item.getBin());
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

    class uploadReturn extends AsyncTask<String, String, String> {
        AlertDialog alertDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            alertDialog = new AlertDialog.Builder(ReturnActivity.this).create();
            alertDialog.setTitle("Error");
            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
            pDialog = new ProgressDialog(ReturnActivity.this);
            pDialog.setMessage("Loading Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            SharedPreferences pref = getApplication().getSharedPreferences("return", 0);
            Gson gson = new Gson();
            SharedPreferences.Editor editor = pref.edit();
            int counter = pref.getInt("returnsize", 0);
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = df.format(c);
            String json = gson.toJson(new ReturnItem(args[0],args[1], args[2],formattedDate));
            editor.putString((counter+1)+"", json);
            editor.putInt("returnsize", counter+1);
            editor.commit(); // commit changes
            return null;
        }


        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            Toast.makeText(getApplicationContext(), "The item has been successfully returned", Toast.LENGTH_LONG).show();
            goToHome();
        }
    }
}
