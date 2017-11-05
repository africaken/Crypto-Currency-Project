package com.example.android.cryptocurrencyconverter;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

public class CurrencyConversion extends AppCompatActivity {

    String fromSymbolCurrencyAbbreviation, fromSymbolCurrency, toSymbolCurrencyAbbreviation, toSymbolCurrency,
            mainCurrency, currencyName, exchangeSymbolCurrency, currencyExchanged, outputCurrency;
    double rateOfExchange, monetaryValue;
    int exchange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.currency_exchange);

        // get action bar
        ActionBar actionBar = getActionBar();

        // Enabling Up / Back navigation
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent inputIntent = getIntent();
        fromSymbolCurrencyAbbreviation = inputIntent.getStringExtra("fromCurrencySymbolAbbreviation");
        toSymbolCurrencyAbbreviation = inputIntent.getStringExtra("toCurrencySymbolAbbreviation");
        fromSymbolCurrency = inputIntent.getStringExtra("fromCurrencySymbol");
        toSymbolCurrency = inputIntent.getStringExtra("toCurrencySymbol");
        rateOfExchange = inputIntent.getDoubleExtra("exchangeRate", 0.0);

        // Find the TextView in the currency_exchange.xml layout with the ID default_currency
        TextView defaultCurrencyView = (TextView) findViewById(R.id.default_currency);

        mainCurrency = "Etherium (" + fromSymbolCurrencyAbbreviation + ")";

        // set this text on the defaultCurrencyView
        defaultCurrencyView.setText(mainCurrency);

        // Find the TextView in the currency_exchange.xml layout with the ID default_currency_symbol
        TextView defaultCurrencySymbolView = (TextView) findViewById(R.id.default_currency_symbol);

        // set this text on the defaultCurrencySymbolView
        defaultCurrencySymbolView.setText(fromSymbolCurrency);

        // Find the TextView in the currency_exchange.xml layout with the ID exchange_currency
        TextView exchangeCurrencyView = (TextView) findViewById(R.id.exchange_currency);

        currencyName = getCurrencyName(toSymbolCurrencyAbbreviation);
        currencyName = currencyName + " (" + toSymbolCurrencyAbbreviation + ")";

        // set this text on the userView
        exchangeCurrencyView.setText(currencyName);

        // Find the TextView in the currency_exchange.xml layout with the ID exchange_currency_symbol
        TextView exchangeCurrencySymbolView = (TextView) findViewById(R.id.exchange_currency_symbol);

        if (toSymbolCurrency != null) {
            exchangeSymbolCurrency = toSymbolCurrency;
        }

        // set this text on the defaultCurrencyView
        exchangeCurrencySymbolView.setText(exchangeSymbolCurrency);

        // Find the TextView in the currency_exchange.xml layout with the ID exchanged_value
        final TextView valuedCurrencyView = (TextView) findViewById(R.id.exchanged_value);

        final EditText editText = (EditText) findViewById(R.id.inputNumber);
        editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    currencyExchanged = editText.getText().toString();

                    // Initialize the value of exchange
                    exchange = 0;

                    try {
                        exchange = Integer.parseInt(currencyExchanged);

                        monetaryValue = rateOfExchange * exchange;

                        outputCurrency = Double.toString(monetaryValue);

                        // set this text on the valuedCurrencyView
                        valuedCurrencyView.setText(outputCurrency);

                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public String getCurrencyName(String toSymbolCurrencyAbbreviation) {

        switch (toSymbolCurrencyAbbreviation) {
            case "BTC":
                currencyName = getString(R.string.bitcoin);
                break;
            case "NGN":
                currencyName = getString(R.string.nigerian_naira);
                break;
            case "EUR":
                currencyName = getString(R.string.european_euro);
                break;
            case "USD":
                currencyName = getString(R.string.united_states_dollar);
                break;
            case "GBP":
                currencyName = getString(R.string.great_britain_pound);
                break;
            case "JPY":
                currencyName = getString(R.string.japanese_yen);
                break;
            case "CNY":
                currencyName = getString(R.string.chinese_yuan);
                break;
            case "RUB":
                currencyName = getString(R.string.russian_ruble);
                break;
            case "SAR":
                currencyName = getString(R.string.saudi_arabian_riyal);
                break;
            case "EGP":
                currencyName = getString(R.string.egyptian_pound);
                break;
            case "CHF":
                currencyName = getString(R.string.swiss_franc);
                break;
            case "INR":
                currencyName = getString(R.string.indian_rupee);
                break;
            case "KRW":
                currencyName = getString(R.string.south_korean_won);
                break;
            case "TRY":
                currencyName = getString(R.string.turkish_lira);
                break;
            case "ZAR":
                currencyName = getString(R.string.south_african_rand);
                break;
            case "LTC":
                currencyName = getString(R.string.litecoin);
                break;
            case "AUD":
                currencyName = getString(R.string.australian_dollar);
                break;
            case "NOK":
                currencyName = getString(R.string.norwegian_krone);
                break;
            case "MAD":
                currencyName = getString(R.string.moroccoan_dirham);
                break;
            case "SGD":
                currencyName = getString(R.string.singapore_dollar);
                break;
            default:
                currencyName = getString(R.string.crypto_currrency);
                break;
        }
        return currencyName;
    }
}