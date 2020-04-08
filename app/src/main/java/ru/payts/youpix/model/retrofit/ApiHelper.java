package ru.payts.youpix.model.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.payts.youpix.model.entity.PhotoSet;


public class ApiHelper {
    private IApiService api;

    public ApiHelper() {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(gson);

        api = new Retrofit.Builder()
                .baseUrl("https://pixabay.com")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(gsonConverterFactory)
                .build()
                .create(IApiService.class);
    }

    public Observable<PhotoSet> requestServer(String apiKey, String query, boolean editors_choice) {
        return api.getPhotoSetBySearch(apiKey, query, editors_choice, 1, 24, "").subscribeOn(Schedulers.io());
    }

    public Observable<PhotoSet> requestServerByID(String apiKey, String picID) {
        return api.getPhotoByID(apiKey, picID).subscribeOn(Schedulers.io());
    }
}
