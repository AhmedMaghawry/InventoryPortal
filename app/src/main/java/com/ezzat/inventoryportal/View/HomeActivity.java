package com.ezzat.inventoryportal.View;

import android.animation.Animator;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.ezzat.inventoryportal.R;

public class HomeActivity extends AppCompatActivity {

    private Button checkOut;
    private Button ret;
    private Button create;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        userId = getIntent().getStringExtra("id");
        checkOut = findViewById(R.id.ch_o);
        ret = findViewById(R.id.returnIt);
        create = findViewById(R.id.create);
        setupAnimation();
        setupToolbar();
        setupActions();
    }

    private void setupActions() {
        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTo(CheckOutActivity.class);
            }
        });

        ret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTo(ReturnActivity.class);
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTo(CreateAvtivity.class);
            }
        });
    }

    private void goTo(Class activityClass) {
        Intent intent = new Intent(HomeActivity.this, activityClass);
        intent.putExtra("id", userId);
        startActivity(intent);
    }

    private void setupAnimation() {

        checkOut.setTranslationX(-1000);
        ret.setTranslationX(-1000);
        create.setTranslationX(-1000);

        checkOut.animate()
                .translationX(0)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ret.animate()
                        .translationX(0)
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                create.animate()
                                        .translationX(0)
                                        .setDuration(1000);
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        })
                        .setDuration(1000);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .setDuration(1000);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menuItem;
        menuItem = menu.add(Menu.NONE, R.id.menu_auto_focus, 0, "LogOut");
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_NEVER);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_auto_focus:
                Intent intent = new Intent(HomeActivity.this, WelcomeActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
