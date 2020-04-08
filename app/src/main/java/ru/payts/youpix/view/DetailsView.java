package ru.payts.youpix.view;

import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.StateStrategyType;
import ru.payts.youpix.model.entity.Hit;

public interface DetailsView extends MvpView {
    @StateStrategyType(value = AddToEndSingleStrategy.class)
    void updateImageView();

    void setImage(Hit hit);
}
