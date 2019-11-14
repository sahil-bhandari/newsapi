package com.sahil.daggerdemo;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import com.sahil.daggerdemo.HomePage.DaggerDeps;
import com.sahil.daggerdemo.HomePage.Deps;
import com.sahil.daggerdemo.Retrofit.NetworkModule;

import java.io.File;

public class BaseApp extends AppCompatActivity {
    Deps deps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        File cacheFile = new File(getCacheDir(), "offlineCache");
        deps = DaggerDeps.builder().networkModule(new NetworkModule(BaseApp.this,cacheFile)).build();
    }

    public Deps getDeps() {
        return deps;
    }
}
