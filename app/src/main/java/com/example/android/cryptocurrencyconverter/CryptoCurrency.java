package com.example.android.cryptocurrencyconverter;

public class CryptoCurrency {

    // Name of the market of used in cryptocurrency transaction on CryptoCompare
    private String mCryptoCurrencyMarket;

    // FromSymbol for a currency as used in cryptocurrency transaction on CryptoCompare
    private String mFromSymbolCurrencyAbbreviation;

    // FromSymbol for a currency as used in cryptocurrency transaction on CryptoCompare
    private String mFromSymbolCurrency;

    // ToSymbol for a currency as used in cryptocurrency transaction on CryptoCompare
    private String mToSymbolCurrencyAbbreviation;

    // ToSymbol for a currency as used in cryptocurrency transaction on CryptoCompare
    private String mToSymbolCurrency;

    // Currency Exchange rate as used in cryptocurrency transaction between FromSymbolCurrency to ToSymbolCurrency on CryptoCompare
    private double mPriceCurrencyExchange;

    /** Date of the cryptocurrency transaction on CryptoCompare*/
    private long mCryptoCurrencyDate;

    /*
    * Create a new CryptoCurrency object.
    *
    * @param cryptoCurrencyMarket is the name of the market of used in cryptocurrency transaction on CryptoCompare
    * @param fromSymbolCurrencyAbbreviation is the FromSymbol for a currency as used in cryptocurrency transaction on CryptoCompare
    * @param fromSymbolCurrency is the FromSymbol for a currency as used in cryptocurrency transaction on CryptoCompare
    * @param toSymbolCurrencyAbbreviation is the ToSymbol for a currency as used in cryptocurrency transaction on CryptoCompare
    * @param toSymbolCurrency is the ToSymbol for a currency as used in cryptocurrency transaction on CryptoCompare
    * @param toSymbolCurrency is the Currency Exchange rate as used in cryptocurrency transaction between FromSymbolCurrency to ToSymbolCurrency on CryptoCompare
    * @param cryptoCurrencyDate is the Date of the cryptocurrency transaction on CryptoCompare
    * */
    public CryptoCurrency(String cryptoCurrencyMarket, String fromSymbolCurrencyAbbreviation, String fromSymbolCurrency,
                          String toSymbolCurrencyAbbreviation, String toSymbolCurrency,
                          double priceCurrencyExchange, long cryptoCurrencyDate)
    {
        mCryptoCurrencyMarket = cryptoCurrencyMarket;
        mFromSymbolCurrencyAbbreviation = fromSymbolCurrencyAbbreviation;
        mFromSymbolCurrency = fromSymbolCurrency;
        mToSymbolCurrencyAbbreviation = toSymbolCurrencyAbbreviation;
        mToSymbolCurrency = toSymbolCurrency;
        mPriceCurrencyExchange = priceCurrencyExchange;
        mCryptoCurrencyDate = cryptoCurrencyDate;
    }

    /**
     * Get the name of the market of used in cryptocurrency transaction on CryptoCompare
     */
    public String getCryptoCurrencyMarket() {
        return mCryptoCurrencyMarket;
    }

    /**
     * Get the FromSymbolAbbreviation for a currency as used in cryptocurrency transaction on CryptoCompare
     */
    public String getFromSymbolCurrencyAbbreviation() {
        return mFromSymbolCurrencyAbbreviation;
    }

    /**
     * Get the FromSymbol for a currency as used in cryptocurrency transaction on CryptoCompare
     */
    public String getFromSymbolCurrency() {
        return mFromSymbolCurrency;
    }

    /**
     * Get the ToSymbolAbbreviation for a currency as used in cryptocurrency transaction on CryptoCompare
     */
    public String getToSymbolCurrencyAbbreviation() {
        return mToSymbolCurrencyAbbreviation;
    }

    /**
     * Get the ToSymbol for a currency as used in cryptocurrency transaction on CryptoCompare
     */
    public String getToSymbolCurrency() {
        return mToSymbolCurrency;
    }

    /**
     * Get the ToSymbol for a currency as used in cryptocurrency transaction on CryptoCompare
     */
    public double getPriceCurrencyExchange() {
        return mPriceCurrencyExchange;
    }

    /**
     * Get the ToSymbol for a currency as used in cryptocurrency transaction on CryptoCompare
     */
    public long getCryptoCurrencyDate() {
        return mCryptoCurrencyDate;
    }


}
