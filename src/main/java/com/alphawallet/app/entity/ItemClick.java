package com.alphawallet.app.entity;

/**
 * Created by Ehsan on 14/11/2023.
 */
public class ItemClick
{
    public final String buttonText;
    public final int buttonId;

    public ItemClick(String text, int id)
    {
        buttonId = id;
        buttonText = text;
    }
}
