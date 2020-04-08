package ru.payts.youpix.app;

import javax.inject.Singleton;

import dagger.Component;
import ru.payts.youpix.presenter.DetailsPresenter;
import ru.payts.youpix.presenter.MainPresenter;
import ru.payts.youpix.view.DetailsActivity;
import ru.payts.youpix.view.MainActivity;
import ru.payts.youpix.view.MainAdapter;

@Singleton
@Component(modules = {YoupixAppModule.class})
public interface YoupixAppComponent {
    void injectMainActivity(MainActivity mainActivity);

    void injectDetailsActivity(DetailsActivity detailsActivity);

    void injectMainPresenter(MainPresenter mainPresenter);

    void injectDetailsPresenter(DetailsPresenter detailsPresenter);

    void injectMainAdapter(MainAdapter mainAdapter);
}
