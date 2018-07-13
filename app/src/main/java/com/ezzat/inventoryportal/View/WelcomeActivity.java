package com.ezzat.inventoryportal.View;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ezzat.inventoryportal.Controller.publicMethods;
import com.ezzat.inventoryportal.Model.Items;
import com.ezzat.inventoryportal.Model.Users;
import com.ezzat.inventoryportal.R;

import java.util.concurrent.Callable;

public class WelcomeActivity extends AppCompatActivity {

    private Button enter_id;
    private Button scan_id;
    private Context self;
    private Class<?> mClss;
    private publicMethods pub;
    private Items items;
    private Users users;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        self = this;
        pub = new publicMethods();
        items = new Items(this);
        users = new Users(this);
        setupViews();
        setupActions();
        new LoadAll().execute();
    }

    private void setupActions() {

        enter_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = "Employee ID";
                String desc = "Enter Your ID : ";
                pub.showMyDialog(title, desc, self, users, items);
            }
        });

        scan_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTheCam();
            }
        });
    }

    private void setupViews() {

        enter_id = findViewById(R.id.enter_id_bt);
        scan_id = findViewById(R.id.scan_id_bt);

    }

    public void startTheCam() {
        startAc(camActivity.class);
    }

    public void startAc(Class<?> cls){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED|| ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            mClss = cls;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            Intent intent = new Intent(this, cls);
            intent.putExtra("items", items);
            intent.putExtra("users", users);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(mClss != null) {
                        Intent intent = new Intent(this, mClss);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(this, "Please grant camera permission to use the QR Scanner", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    class LoadAll extends AsyncTask<String, String, String> {
        AlertDialog alertDialog;
        boolean finished = true;
        String msg;
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            alertDialog = new AlertDialog.Builder(WelcomeActivity.this).create();
            alertDialog.setTitle("Error");
            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
            pDialog = new ProgressDialog(WelcomeActivity.this);
            pDialog.setMessage("Loading Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            items.loadItems(self);
            users.loadUsers(self);
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
        }
    }
}
