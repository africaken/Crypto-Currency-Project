package com.example.android.cryptocurrencyconverter;

import android.content.Context;

import java.util.List;

public class CryptoCurrencyLoader extends android.support.v4.content.AsyncTaskLoader<List<CryptoCurrency>>{

    private String mUrl;

    public CryptoCurrencyLoader(Context context, String url){
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading(){
        forceLoad();
    }

    @Override
    public List<CryptoCurrency> loadInBackground() {
        if(mUrl == null){
            return null;
        }
        List<CryptoCurrency> result = Crypto_Utils.fetchCryptoCurrencyData(mUrl);
        return result;
    }

}
