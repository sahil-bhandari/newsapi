package com.sahil.daggerdemo.Retrofit;

import com.sahil.daggerdemo.BuildConfig;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import com.sahil.daggerdemo.models.ResponseModel;
import rx.schedulers.Schedulers;


public class NetworkingService {
    private final apiService networkService;

    public NetworkingService(apiService networkService) {
        this.networkService = networkService;
    }

    public Subscription getNewsList(final GetNewsListCallback callback, String category) {

        return networkService.getNewsList("Bearer "+ BuildConfig.APIKEY,"in",category,20,1 )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends ResponseModel>>() {
                    @Override
                    public Observable<? extends ResponseModel> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<ResponseModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(ResponseModel NewsListResponse) {
                        callback.onSuccess(NewsListResponse);

                    }
                });
    }

    public interface GetNewsListCallback{
        void onSuccess(ResponseModel NewsListResponse);

        void onError(NetworkError networkError);
    }
}
