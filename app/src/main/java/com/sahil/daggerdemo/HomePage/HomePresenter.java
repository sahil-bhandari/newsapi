package com.sahil.daggerdemo.HomePage;

import com.sahil.daggerdemo.Retrofit.NetworkError;
import com.sahil.daggerdemo.Retrofit.NetworkingService;
import com.sahil.daggerdemo.models.ResponseModel;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class HomePresenter {
    private final NetworkingService service;
    private final HomeView view;
    private CompositeSubscription subscriptions;

    public HomePresenter(NetworkingService service, HomeView view) {
        this.service = service;
        this.view = view;
        this.subscriptions = new CompositeSubscription();
    }

    public void getNewsList(String category, int page) {
        view.showWait();
        Subscription subscription = service.getNewsList(new NetworkingService.GetNewsListCallback() {
            @Override
            public void onSuccess(ResponseModel ListResponseNews) {
                view.removeWait();
                view.getNewsListSuccess(ListResponseNews);
            }

            @Override
            public void onError(NetworkError networkError) {
                view.removeWait();
                view.onFailure(networkError.getAppErrorMessage());
            }

        }, category,page);

        subscriptions.add(subscription);
    }
    public void onStop() {
        subscriptions.unsubscribe();
    }
}

