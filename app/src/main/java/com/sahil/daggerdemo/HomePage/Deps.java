package com.sahil.daggerdemo.HomePage;



import com.sahil.daggerdemo.Retrofit.NetworkModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {NetworkModule.class,})
public interface Deps {
    void inject(MainActivity homeActivity);
}
