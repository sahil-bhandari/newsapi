package com.sahil.daggerdemo.HomePage;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.sahil.daggerdemo.BaseApp;
import com.sahil.daggerdemo.R;
import com.sahil.daggerdemo.Retrofit.NetworkingService;
import com.sahil.daggerdemo.WebViewActivity;
import com.sahil.daggerdemo.models.Article;
import com.sahil.daggerdemo.models.ResponseModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import javax.inject.Inject;

public class MainActivity extends BaseApp implements HomeView {

    RecyclerView list;
    ProgressBar progressBar;
    Toolbar toolbar;
    @Inject
    public NetworkingService service;
    HomeAdapter adapter;
    int page = 0;
    String category = "general";
    AppCompatSpinner spinner;
    List<String> category_list = new ArrayList<>();
    FloatingActionButton next,previous;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDeps().inject(this);
        setContentView(R.layout.activity_main);
        initViews();
        loadSpinnerData();
        setupToolbar();
        showSnackIfOffline();
        loadData(category,page);
        clickButton();
    }

    private void initViews() {
        list = findViewById(R.id.list);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        list.setLayoutManager(llm);
        progressBar = findViewById(R.id.progress);
        spinner = findViewById(R.id.spinner);
        next = findViewById(R.id.next);
        previous = findViewById(R.id.previous);
    }

    private void clickButton() {
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page=page++;
                loadData(category,page);
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page=page--;
                loadData(category,page);
            }
        });
    }

    private void loadSpinnerData() {
        category_list.clear();
        category_list.add("General");
        category_list.add("Business");
        category_list.add("Entertainment");
        category_list.add("Health");
        category_list.add("Science");
        category_list.add("Technology");
        category_list.add("Sports");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, category_list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setSelection(0);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                page=0;
                category = parent.getItemAtPosition(position).toString().toLowerCase().trim();
                loadData(category,page);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void loadData(String category, int page) {
        HomePresenter presenter = new HomePresenter(service, this);
        presenter.getNewsList(category,page);
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
        showSnackIfOffline();
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
        getPageNumber(page);
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void getPageNumber(int page) {
        if(page<1){
            previous.setVisibility(View.GONE);
        }
        else if (page>3){
            next.setVisibility(View.GONE);
        }
        else{

            next.setVisibility(View.VISIBLE);
            previous.setVisibility(View.VISIBLE);
        }
    }

    private void showSnackIfOffline(){
        final boolean online = isOnline();
        runOnUiThread(new TimerTask() {
            @Override
            public void run() {
                if(!online)
                    Snackbar.make(findViewById(android.R.id.content), "Sorry, you're offline", Snackbar.LENGTH_LONG).show();
                else{

                }
            }
        });
    }

    private boolean isOnline(){

        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        showSnackIfOffline();
    }
}
