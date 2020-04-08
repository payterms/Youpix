package ru.payts.youpix.app;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import java.util.ArrayList;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.payts.youpix.model.PhotoData;
import ru.payts.youpix.model.cache.PixCache;
import ru.payts.youpix.model.database.YoupixDatabase;
import ru.payts.youpix.model.entity.PhotoSet;
import ru.payts.youpix.model.glide.GlideLoader;
import ru.payts.youpix.model.retrofit.ApiHelper;
import ru.payts.youpix.presenter.DetailsPresenter;
import ru.payts.youpix.presenter.MainPresenter;

@Module
public class YoupixAppModule {
    private final Application application;

    public YoupixAppModule(Application application) {
        this.application = application;
    }

    @Singleton
    @Provides
    ApiHelper provideApiHelper() {
        return new ApiHelper();
    }

    @Singleton
    @Provides
    PhotoData providePhotoData(PhotoSet photoSet) {
        return new PhotoData(photoSet);
    }

    @Provides
    PhotoSet providePhotoSet() {
        PhotoSet photoSet = new PhotoSet();
        photoSet.hits = new ArrayList<>();
        return photoSet;
    }

    @Singleton
    @Provides
    GlideLoader provideGlideLoader(PixCache pixCache) {
        return new GlideLoader(pixCache);
    }

    @Singleton
    @Provides
    PixCache provideImageCache() {
        return new PixCache();
    }

    @Singleton
    @Provides
    MainPresenter provideMainPresenter(Context context) {
        return new MainPresenter(application.getApplicationContext());
    }

    @Singleton
    @Provides
    DetailsPresenter provideDetailsPresenter(Context context) {
        return new DetailsPresenter(application.getApplicationContext());
    }

    @Singleton
    @Provides
    YoupixDatabase provideappDatabase() {
        return Room.databaseBuilder(application.getApplicationContext(),
                YoupixDatabase.class, "youpics_database").build();
    }



 /*   @Singleton
    @Provides
    MainAdapter provideMainAdapter(Context context, I2RecyclerMain i2RecyclerMain) {
        return new MainAdapter(context, i2RecyclerMain);
    }*/

}
