package ru.payts.youpix.view;

import moxy.MvpView;
import moxy.viewstate.strategy.OneExecutionStateStrategy;
import moxy.viewstate.strategy.SingleStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;

public interface MainView extends MvpView {
    @StateStrategyType(value = SingleStateStrategy.class)
    void updateRecyclerView();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void startDetailsActivity(int pos);
}
