package com.sahil.daggerdemo.Retrofit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.sahil.daggerdemo.BaseApp;
import com.sahil.daggerdemo.BuildConfig;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

@Module
public class NetworkModule {
    File cacheFile;
    Context context;

    public NetworkModule(Context context, File cacheFile) {
        this.context = context;
        this.cacheFile = cacheFile;
    }

    @Provides
    @Singleton
    Retrofit provideCall() {
        Cache cache = null;
        try {
            cache = new Cache(cacheFile, 10 * 1024 * 1024);
        } catch (Exception e) {
            e.printStackTrace();
        }

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        if (isNetworkAvailable()) {
                            request = request.newBuilder().header("Cache-Control", "public, max-age=" + BuildConfig.CACHETIME).build();
                        } else {
                            request = request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build();
                        }
//                        @SuppressLint("DefaultLocale") Request request = original.newBuilder()
//                                .header("Content-Type", "application/json")
//                                .removeHeader("Pragma")
//                                .header("Cache-Control", String.format("max-age=%d", BuildConfig.CACHETIME))
//                                .build();
//                        okhttp3.Response response = chain.proceed(request);
//                        response.cacheResponse();
//                        return response;
                        return chain.proceed(request);
                    }
                })
                .build();


        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BASEURL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    public apiService providesNetworkService(
             Retrofit retrofit) {
        return retrofit.create(apiService.class);
    }
    @Provides
    @Singleton
    @SuppressWarnings("unused")
    public NetworkingService providesService(
            apiService networkService) {
        return new NetworkingService(networkService);
    }

}
