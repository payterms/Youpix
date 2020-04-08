package ru.payts.youpix.presenter;

import ru.payts.youpix.view.IViewHolder;

public interface I2RecyclerMain {
    void bindView(IViewHolder iViewHolder);

    int getItemCount();

    void imgClicked(int itemPos);
}
