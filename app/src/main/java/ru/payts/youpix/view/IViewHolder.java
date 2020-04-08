package ru.payts.youpix.view;

import ru.payts.youpix.model.entity.Hit;

public interface IViewHolder {
    int getPos();

    void showImageFromServer(Hit hit);

    void showImageFromStorage(Hit hit);

    void setImageSize(int width, int height);
}
