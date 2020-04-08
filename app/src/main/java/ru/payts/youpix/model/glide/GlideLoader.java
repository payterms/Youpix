package ru.payts.youpix.model.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import ru.payts.youpix.R;
import ru.payts.youpix.model.cache.PixCache;
import ru.payts.youpix.model.entity.Hit;


public class GlideLoader {

    private static final String TAG = "GlideLoader";
    private PixCache pixCache;

    public GlideLoader(PixCache pixCache) {
        this.pixCache = pixCache;
    }

    public void loadImageFromServer2Cache(Context ct, Hit hit) {
        Glide
                .with(ct)
                .asBitmap()
                .load(hit.webformatURL)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Log.d(TAG, "Picture loaded " + hit.id);
                        pixCache.saveImage2FS(resource, hit);
                    }

                    @Override
                    public void onLoadCleared(@androidx.annotation.Nullable Drawable placeholder) {

                    }
                });
    }

    public void loadImageFromServer(Hit hit, ImageView imageView) {
        Glide
                .with(imageView.getContext())
                .asBitmap()
                .load(hit.webformatURL)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@androidx.annotation.Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        Log.d(TAG, "onLoadFailed" + e.getMessage());
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        //Log.d(TAG, "Picture loaded " + hit.id);
                        pixCache.saveImage2FS(resource, hit);
                        return false;
                    }
                })
                .error(R.drawable.notfound)
                .fitCenter()
                .into(imageView);
    }


    public void loadImageFromStorage(ImageView imageView, Hit hit) {
        File imagePath = pixCache.getImagePath(hit);
        Glide
                .with(imageView.getContext())
                .load(imagePath)
                .into(imageView);
    }

    public void clearAllImagesFromStorage() {
        pixCache.clearCache();
    }
}
