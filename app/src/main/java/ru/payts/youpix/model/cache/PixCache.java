package ru.payts.youpix.model.cache;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;

import ru.payts.youpix.model.entity.Hit;

public class PixCache {

    private static final String TAG = "PixCache";
    private static final String APP_FOLDER = "YoupixCache";

    public void clearCache() {
        if (getImageDir().exists()) {
            File[] Files = getImageDir().listFiles();
            if (Files != null) {
                int j;
                for (j = 0; j < Files.length; j++) {
                    Log.d(TAG, "Deleting Image: " + Files[j].getAbsolutePath());
                    Log.d(TAG, "Status >" + Files[j].delete());
                }
            }
        }
    }

    //сохраняем картинку
    public void saveImage2FS(Bitmap bitmap, @NotNull Hit hit) {

        //Log.d(TAG, "saveImage: сохранение image в хранилище телефона в папке " + APP_FOLDER + " под именем " + hit.id);

        //если папка существует или не получилось создать, выбрасываем ошибку
        if (!getImageDir().exists() && !getImageDir().mkdirs()) {
            throw new RuntimeException("fail");
        }

        File imageFile = new File(getImageDir(), hit.id + ".png");

        //сохраняем картинку на карту памяти
        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            Log.d(TAG, "saveImage: ");
        }

    }

    private File getImageDir() {
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        return new File(externalStorageDirectory + File.separator + APP_FOLDER);
    }


    public File getImagePath(@NotNull Hit hit) {
        return new File(Environment.getExternalStorageDirectory() + File.separator + APP_FOLDER + File.separator + hit.id + ".png");
    }
}
