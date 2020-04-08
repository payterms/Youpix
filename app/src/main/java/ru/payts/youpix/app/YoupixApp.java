package ru.payts.youpix.app;

import android.app.Application;

public class YoupixApp extends Application {

    private static YoupixAppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return;
//        }
//        LeakCanary.install(this);

        appComponent = generateAppComponent();
    }

    public static YoupixAppComponent getAppComponent() {
        return appComponent;
    }

    public YoupixAppComponent generateAppComponent() {
        //чтобы убратиь ошибку " cannot resolve symbol DaggerAppComponent "
        // необходимо сбилдить проект (DaggerAppComponent будет в ap_generated_sources)
        return DaggerYoupixAppComponent
                .builder()
                .youpixAppModule(new YoupixAppModule(this))
                .build();
    }
}
