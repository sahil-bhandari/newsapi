package com.sahil.daggerdemo.HomePage;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.sahil.daggerdemo.BaseApp;
import com.sahil.daggerdemo.R;
import com.sahil.daggerdemo.Retrofit.NetworkingService;
import com.sahil.daggerdemo.WebViewActivity;
import com.sahil.daggerdemo.models.Article;
import com.sahil.daggerdemo.models.ResponseModel;

import java.util.TimerTask;

import javax.inject.Inject;

public class MainActivity extends BaseApp implements HomeView {

    RecyclerView list;
    ProgressBar progressBar;
    Toolbar toolbar;
    @Inject
    public NetworkingService service;
    HomeAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDeps().inject(this);
        setContentView(R.layout.activity_main);
        list = findViewById(R.id.list);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        list.setLayoutManager(llm);
        progressBar = findViewById(R.id.progress);
        setupToolbar();
        showSnackIfOffline();

        HomePresenter presenter = new HomePresenter(service, this);
        presenter.getNewsList("sports");
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("HOME");
        setSupportActionBar(toolbar);
    }

    @Override
    public void showWait() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void removeWait() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onFailure(String appErrorMessage) {

    }

    @Override
    public void getNewsListSuccess(ResponseModel ListResponse) {
         adapter = new HomeAdapter(MainActivity.this, ListResponse.getArticles(),
                new HomeAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(Article Item) {
                        Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("link", Item.getUrl());
                        intent.putExtras(bundle);
                        startActivity(intent);
//                        Toast.makeText(MainActivity.this, Item.getTitle(), Toast.LENGTH_LONG).show();
                    }
                });

        list.setAdapter(adapter);

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void showSnackIfOffline(){
        final boolean online = isOnline();
        runOnUiThread(new TimerTask() {
            @Override
            public void run() {
                if(!online)
                    Snackbar.make(findViewById(android.R.id.content), "Sorry, you're offline", Snackbar.LENGTH_SHORT);
                else{
                /*
                    Your code here if user is online
                */
                }
            }
        });
    }

    private boolean isOnline(){
        try {
            return Runtime.getRuntime().exec("/system/bin/ping -c 1 8.8.8.8").waitFor() == 0; //  "8.8.8.8" is the server to ping
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        showSnackIfOffline();
    }
}
