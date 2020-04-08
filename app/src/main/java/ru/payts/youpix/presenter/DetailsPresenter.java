package ru.payts.youpix.presenter;

import android.content.Context;
import android.util.Log;

import javax.inject.Inject;

import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.payts.youpix.app.YoupixApp;
import ru.payts.youpix.model.PhotoData;
import ru.payts.youpix.model.entity.Hit;
import ru.payts.youpix.model.retrofit.ApiHelper;
import ru.payts.youpix.view.DetailsView;

@InjectViewState
public class DetailsPresenter extends MvpPresenter<DetailsView> {

    private static final String TAG = "DetailsPresenter";

    @Inject
    ApiHelper apiHelper;

    @Inject
    PhotoData photoData;

    private Context context;

    public DetailsPresenter(Context ct) {
        YoupixApp.getAppComponent().injectDetailsPresenter(this);
        Log.d(TAG, "DetailsPresenter: ");

        context = ct;
    }

    @Override
    protected void onFirstViewAttach() {
        Log.d(TAG, "DetailsPresenter: onFirstViewAttach ");
    }


    public Hit getPhotoByPos(int index) {
        return photoData.getElementValueAtIndex(index);
    }

    private class ActionDetails implements I2Details {
        @Override
        public void imgClicked(int ID) {
            Hit hit = photoData.getElementValueAtIndex(ID);
            hit.views++;
            photoData.setElementValueAtIndex(ID, hit);
            Log.d(TAG, String.format("Img %d clicked %s time(s)", ID, hit.views));
        }
    }


}
