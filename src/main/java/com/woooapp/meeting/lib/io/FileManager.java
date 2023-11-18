package com.woooapp.meeting.lib.io;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 5:22 pm 17/11/2023
 * <code>class</code> FileManager.java
 */
public final class FileManager {

    private static final String TAG = FileManager.class.getSimpleName() + ".java";
    private static FileManager sInstance = null;
    private final Context mContext;

    /**
     *
     * @param mContext
     */
    private FileManager(Context mContext) {
        this.mContext = mContext;
    }

    /**
     *
     * @param name
     * @param object
     */
    public void save(@NonNull String name, @NonNull Object object) {
        try {
            FileOutputStream fos = mContext.openFileOutput(name, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(object);
            os.flush();
            os.close();
            fos.flush();
            fos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     *
     * @param filename
     * @return
     * @param <T>
     */
    @Nullable
    public <T> T load(@NonNull String filename) {
        try {
            FileInputStream fis = mContext.openFileInput(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            T t = (T) ois.readObject();
            ois.close();
            fis.close();
            return t;
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param context
     * @return
     */
    public static FileManager getFileManager(@NonNull Context context) {
        synchronized (FileManager.class) {
            return new FileManager(context);
        }
    }

} /** end class. */
