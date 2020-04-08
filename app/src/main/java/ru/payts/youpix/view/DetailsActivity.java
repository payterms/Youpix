package ru.payts.youpix.view;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import moxy.MvpAppCompatActivity;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import ru.payts.youpix.R;
import ru.payts.youpix.app.YoupixApp;
import ru.payts.youpix.model.entity.Hit;
import ru.payts.youpix.model.glide.GlideLoader;
import ru.payts.youpix.presenter.DetailsPresenter;

public class DetailsActivity extends MvpAppCompatActivity implements DetailsView {

    private static final String TAG = "DetailsActivity";

    private int selectedItemPos;

    @Inject
    GlideLoader glideLoader;

    @BindView(R.id.detailsImageView)
    ImageView detailsImageView;

    @InjectPresenter
    DetailsPresenter presenter;

    @ProvidePresenter
    public DetailsPresenter providePresenter() {
        return new DetailsPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareFullScreenWindow();
        setContentView(R.layout.activity_detailsmvp);
        YoupixApp.getAppComponent().injectDetailsActivity(this);
        ButterKnife.bind(this);
        Bundle arguments = getIntent().getExtras();
        selectedItemPos = arguments.getInt("clickedItemPos");
        Log.d(TAG, "clickedItemPos: " + selectedItemPos);
        initViews();
    }

    private void initViews() {
        //glideLoader = new GlideLoader(this);
        Log.d(TAG, "initViews details: ");
        detailsImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        detailsImageView.setAdjustViewBounds(true);
        glideLoader.loadImageFromStorage(detailsImageView, presenter.getPhotoByPos(selectedItemPos));
    }

    private void prepareFullScreenWindow() {
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    @Override
    public void updateImageView() {
        Log.d(TAG, "updateRecyclerView: ");
    }

    @Override
    public void setImage(Hit hit) {
        glideLoader.loadImageFromStorage(detailsImageView, hit);
        //detailsImageView.setOnClickListener(v -> presenter.getActionDetails().imgClicked(position));
    }
}
