package com.woooapp.meeting.impl.utils;

import android.media.MediaDataSource;

import androidx.annotation.NonNull;

import java.io.IOException;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 11:12 am 31/10/2023
 * <code>class</code> VoiceDataSource.java
 */
public class VoiceDataSource extends MediaDataSource {

    private final byte[] data;

    /**
     *
     * @param data
     */
    public VoiceDataSource(@NonNull byte[] data) {
        this.data = data;
    }

    @Override
    public int readAt(long position, byte[] buffer, int offset, int size) throws IOException {
        int length = data.length;
        if (position >= length) {
            return -1;
        }
        if (position + size > length) {
            size -= (position + size) - length;
        }

        System.arraycopy(data, (int) position, buffer, offset, size);
        return size;
    }

    @Override
    public long getSize() throws IOException {
        return data.length;
    }

    @Override
    public void close() throws IOException {

    }

} /** end class. */
