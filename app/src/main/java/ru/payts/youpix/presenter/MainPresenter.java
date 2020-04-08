package ru.payts.youpix.presenter;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.payts.youpix.R;
import ru.payts.youpix.app.YoupixApp;
import ru.payts.youpix.model.PhotoData;
import ru.payts.youpix.model.database.HitDao;
import ru.payts.youpix.model.database.HitRec;
import ru.payts.youpix.model.database.YoupixDatabase;
import ru.payts.youpix.model.entity.Hit;
import ru.payts.youpix.model.entity.PhotoSet;
import ru.payts.youpix.model.glide.GlideLoader;
import ru.payts.youpix.model.retrofit.ApiHelper;
import ru.payts.youpix.view.IViewHolder;
import ru.payts.youpix.view.MainView;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {

    private static final String TAG = "MainPresenter";

    // ?? Inject для Dagger - будет создан 1 раз при создании MainPresenter
    private RecyclerMain recyclerMain;

    @Inject
    ApiHelper apiHelper;

    @Inject
    GlideLoader glideLoader;

    @Inject
    PhotoData photoData;

    @Inject
    YoupixDatabase appDatabase;

    private HitDao hitDao;

    private Context context;

    public MainPresenter(Context ct) {
        YoupixApp.getAppComponent().injectMainPresenter(this);
        Log.d(TAG, "MainPresenter: ");
        recyclerMain = new RecyclerMain();
        Log.d(TAG, "recyclerMain: ");
        context = ct;
        hitDao = appDatabase.hitDao();
    }

    @Override
    protected void onFirstViewAttach() {
        getPhotoListFromServer(context, "", true);
    }

    public void clearPicturesCache(Context context) {
        photoData.clearList();
        glideLoader.clearAllImagesFromStorage();
        Disposable disposable = hitDao.deleteAll().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    Log.d(TAG, "Database cleared");
                    getPhotoListFromServer(context, "", true);

                }, throwable -> Log.d(TAG, "clearPicturesCache DB err " + throwable));

    }

    public void getPhotoListFromServer(Context context, String query, boolean editors_choice) {
        String apiKey = context.getString(R.string.pixabay_api_key);
        /* чистим список фотографий для отображения*/
        photoData.clearList();

        Observable<PhotoSet> single = apiHelper.requestServer(apiKey, query, editors_choice);

        Disposable disposable = single.observeOn(AndroidSchedulers.mainThread()).subscribe(photoSet -> {
            /*Список получен - анализируем его*/
            for (Hit hitItem : photoSet.hits) {
                /* Проверяем - есть ли такая картинка уже в базе среди загруженных */
                Disposable disposableDaoGet = hitDao.getPictureRecordByPicId(hitItem.id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(hitRec -> {
                            // картинка уже есть есть в базе
                            // будем отображать из кэша
                            Log.d(TAG, "Picture with ID " + hitItem.id + "  is already in Database");
                            photoData.addElement(hitItem);
                            if (photoSet.hits.size() == photoData.getHitListSize()) {
                                getViewState().updateRecyclerView();
                            }
                        }, throwable -> {
                            // картинки в базе нет - добавляем ее в базу и загружаем в кэш изображение
                            Log.d(TAG, "No such picture in Database" + throwable);
                            HitRec dbRec = new HitRec();
                            dbRec.picId = hitItem.id;
                            dbRec.webformatURL = hitItem.webformatURL;
                            dbRec.imageWidth = hitItem.imageWidth;
                            dbRec.imageHeight = hitItem.imageHeight;
                            dbRec.imageSize = hitItem.imageSize;
                            dbRec.views = hitItem.views;
                            dbRec.userId = hitItem.user_id;
                            dbRec.userImageURL = hitItem.userImageURL;
                            Disposable disposableDaoInsert = hitDao.insert(dbRec).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(id -> {
                                        Log.d(TAG, "Picture with ID " + hitItem.id + "  added to Database");
                                        glideLoader.loadImageFromServer2Cache(context, hitItem);
                                        // добавляем в список для RecycleView
                                        photoData.addElement(hitItem);
                                        if (photoSet.hits.size() == photoData.getHitListSize()) {
                                            getViewState().updateRecyclerView();
                                        }
                                    }, throwableDao -> {
                                        Log.d(TAG, "Error while adding picture with ID: " + hitItem.id + throwableDao);
                                    });
                        });
            }
        }, throwable -> Log.e(TAG, "onError " + throwable));
    }

    private class RecyclerMain implements I2RecyclerMain {

        @Override
        public void bindView(IViewHolder iViewHolder) {
            iViewHolder.showImageFromStorage(photoData.getElementValueAtIndex(iViewHolder.getPos()));
        }

        @Override
        public int getItemCount() {
            if (photoData != null) {
                return photoData.getHitListSize();
            }
            return 0;
        }

        @Override
        public void imgClicked(int itemPos) {
            Hit hit = photoData.getElementValueAtIndex(itemPos);
            hit.views++;
            photoData.setElementValueAtIndex(itemPos, hit);
            Log.d(TAG, String.format("Img %d clicked %s time(s)", itemPos, hit.views));
            getViewState().startDetailsActivity(itemPos);
        }
    }

    public RecyclerMain getRecyclerMain() {
        return recyclerMain;
    }

    private void addHits2Database(List<Hit> hits) {
        HitRec dbRec;
        for (Hit item : hits) {
            dbRec = new HitRec();
            dbRec.picId = item.id;
            dbRec.webformatURL = item.webformatURL;
            dbRec.imageWidth = item.imageWidth;
            dbRec.imageHeight = item.imageHeight;
            dbRec.imageSize = item.imageSize;
            dbRec.views = item.views;
            dbRec.userId = item.user_id;
            dbRec.userImageURL = item.userImageURL;
            Disposable disposable = hitDao.insert(dbRec).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(id -> Log.d(TAG, "dbRec added: " + id), throwable -> Log.d(TAG, "dbRec err: " + throwable));

            Disposable disposabl2 = hitDao.insert(dbRec).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(id -> Log.d(TAG, "dbRec added: " + id), throwable -> Log.d(TAG, "dbRec err: " + throwable));
        }

    }

    void checkLinks(List<Hit> hitlist) {
        for (Hit hit : hitlist) {
            try {
                URL url = new URL(hit.webformatURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {

                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    // Если исключения не возникло - и ссылка живая , то добавляем в список
                    hitlist.add(hit);
                } catch (FileNotFoundException e) {
                    String apiKey = context.getString(R.string.pixabay_api_key);
                    Observable<PhotoSet> single = apiHelper.requestServerByID(apiKey, hit.id);
                    Disposable disposableUpd = single.observeOn(AndroidSchedulers.mainThread()).subscribe(photoSet -> {
                        updateItemsByPicIdInDatabase(photoSet.hits.get(0));
                    }, throwable -> Log.e(TAG, "onError " + throwable));
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }

                }
            } catch (Exception e) {
                Log.d(TAG, "checkLinks err " + e);
            }
        }
    }


    private void updateItemsByPicIdInDatabase(Hit item) {
        Disposable disposable = hitDao.getPictureRecordByPicId(item.id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(dbRecs -> {
                    HitRec dbRec = new HitRec();
                    dbRec.webformatURL = item.webformatURL;
                    dbRec.imageWidth = item.imageWidth;
                    dbRec.imageHeight = item.imageHeight;
                    dbRec.imageSize = item.imageSize;
                    dbRec.views = item.views;
                    dbRec.userId = item.user_id;
                    dbRec.userImageURL = item.userImageURL;

                    Disposable disposableUpd = hitDao.update(dbRec).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                            .subscribe(id -> {
                                Log.d(TAG, "dbRec update " + id);
                            }, throwable -> Log.d(TAG, "dbRec update err: " + throwable));

                }, throwable -> Log.d(TAG, "dbRec update err: " + throwable));

    }
}
