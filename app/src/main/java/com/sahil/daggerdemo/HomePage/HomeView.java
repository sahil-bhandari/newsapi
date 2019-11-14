package com.sahil.daggerdemo.HomePage;


import com.sahil.daggerdemo.models.ResponseModel;

public interface HomeView {
    void showWait();

    void removeWait();

    void onFailure(String appErrorMessage);

    void getNewsListSuccess(ResponseModel ListResponse);

}
