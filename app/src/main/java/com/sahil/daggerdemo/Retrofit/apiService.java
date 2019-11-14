package com.sahil.daggerdemo.Retrofit;

import com.sahil.daggerdemo.models.ResponseModel;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import rx.Observable;

public interface apiService {
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("v2/top-headlines")
    Observable<ResponseModel> getNewsList(@Header("Authorization") String auth ,
                                          @Query("country") String country ,
                                          @Query("category") String category ,
                                          @Query("pageSize") int pageSize ,
                                          @Query("page") int page);

}
