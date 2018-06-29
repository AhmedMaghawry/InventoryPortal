package com.ezzat.inventoryportal.View;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ezzat.inventoryportal.Controller.cameraClasses.MessageDialogFragment;
import com.ezzat.inventoryportal.R;

public class PasswordActivity extends AppCompatActivity implements MessageDialogFragment.MessageDialogListener {

    private EditText pswd;
    private Button btn;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        pswd = findViewById(R.id.passwrd);
        btn = findViewById(R.id.enter);
        userID = getIntent().getStringExtra("id");
        Log.i("dodo", userID);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean val = checkPassword(pswd.getText().toString(), userID);
                if (val) {
                    Intent intent = new Intent(PasswordActivity.this, HomeActivity.class);
                    intent.putExtra("id", userID);
                    startActivity(intent);
                } else {
                    showMessageDialog("Wrong Password for userID " + userID);
                }
            }
        });
    }

    private boolean checkPassword(String pas, String usrID) {
        //TODO Check PassWord
        return true;
    }

    public void showMessageDialog(String message) {
        DialogFragment fragment = MessageDialogFragment.newInstance("Wrong", message, this);
        fragment.show(getSupportFragmentManager(), "wrong");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        closeDialog("Wrong");
    }

    public void closeDialog(String dialogName) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogFragment fragment = (DialogFragment) fragmentManager.findFragmentByTag(dialogName);
        if(fragment != null) {
            fragment.dismiss();
        }
    }
}
