package com.ezzat.inventoryportal.Controller;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ezzat.inventoryportal.R;
import com.ezzat.inventoryportal.View.PasswordActivity;

import java.util.concurrent.Callable;

public class publicMethods {

    String val = "";

    public Boolean searchInXSL() {
        //TODO:Check for ID
        return true;
    }

    public Boolean searchInXSL(String v) {
        //TODO:Check for ID
        return true;
    }

    public void showMyDialog(String title, String desc, final Context context, final Callable func) {
        // custom dialog
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog);
        dialog.setTitle(title);

        // set the custom dialog components - text, button
        TextView text = dialog.findViewById(R.id.desc_tv);
        text.setText(desc);
        final EditText editText = dialog.findViewById(R.id.input_et);
        editText.setHint(title + " ...");

        Button dialogCan = dialog.findViewById(R.id.cancel);
        // if button is clicked, close the custom dialog
        dialogCan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button dialogOk = dialog.findViewById(R.id.ok);
        // if button is clicked
        dialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    val = editText.getText().toString();
                    Boolean b = (Boolean) func.call();
                    if (b) {
                        dialog.dismiss();
                        Intent intent = new Intent(context, PasswordActivity.class);
                        intent.putExtra("id", val);
                        context.startActivity(intent);
                    } else {
                        TextView wro = dialog.findViewById(R.id.wrong);
                        editText.clearComposingText();
                        wro.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        dialog.show();
    }

}
