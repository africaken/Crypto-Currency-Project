package com.example.android.cryptocurrencyconverter;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<CryptoCurrency>> {

    /**
     * Constant value for the cryptocurrency loader ID.
     */
    private static final int CRYPTOCURRENCY_LOADER_ID = 1;

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView connectionAbsentView;

    /**
     * Progress Indicator that is displayed when fetching lagosDeveloper
     */
    private ProgressBar progressIndicator;

    /**
     * URL to query the CryptoCompare api for Developers in Lagos(Nigeria) for data
     */
    private static final String CryptoCompare_URL =
            "https://min-api.cryptocompare.com/data/pricemultifull?fsyms=ETH&tsyms=BTC,NGN,EUR," +
                    "USD,GBP,JPY,CNY,RUB,SAR,EGP,CHF,INR,KRW,TRY,ZAR,LTC,AUD,NOK,MAD,SGD";

    /**
     * Adapter for the list of CryptoCurrency
     */
    private CryptoCurrencyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find a reference to the {@link ListView} in the layout
        ListView cryptoCurrencyListView = (ListView) findViewById(R.id.listview_cryptocurrency);

        // Create a new adapter that takes an empty list of cryptocurrency as input
        mAdapter = new CryptoCurrencyAdapter(this, new ArrayList<CryptoCurrency>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        cryptoCurrencyListView.setAdapter(mAdapter);

        // Set an item click listener on the ListView, which sends an intent to another activity
        // to launch a conversion screen between the two currencies.
        cryptoCurrencyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // Get the {@link DeveloperLagos} object located at this position in the list
                CryptoCurrency currentCryptoCurrency = mAdapter.getItem(i);

                // Create a new intent to view the developer details
                Intent currencyConversionIntent = new Intent(MainActivity.this, CurrencyConversion.class);

                if (currentCryptoCurrency != null) {
                    currencyConversionIntent.putExtra("fromCurrencySymbolAbbreviation", currentCryptoCurrency.getFromSymbolCurrencyAbbreviation());
                    currencyConversionIntent.putExtra("toCurrencySymbolAbbreviation", currentCryptoCurrency.getToSymbolCurrencyAbbreviation());
                    currencyConversionIntent.putExtra("fromCurrencySymbol", currentCryptoCurrency.getFromSymbolCurrency());
                    currencyConversionIntent.putExtra("toCurrencySymbol", currentCryptoCurrency.getToSymbolCurrency());
                    currencyConversionIntent.putExtra("exchangeRate", currentCryptoCurrency.getPriceCurrencyExchange());
                }

                // Send the intent to launch a new activity
                startActivity(currencyConversionIntent);
            }
        });


        // TextView that is displayed when the list is empty
        connectionAbsentView = (TextView) findViewById(R.id.connection_absent_view);
        cryptoCurrencyListView.setEmptyView(connectionAbsentView);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            getSupportLoaderManager().initLoader(CRYPTOCURRENCY_LOADER_ID, null, this).forceLoad();

            // Progress Indicator shows that the data is being fetched
            progressIndicator = (ProgressBar) findViewById(R.id.loading_spinner);
            progressIndicator.setIndeterminate(true);
            progressIndicator.setVisibility(View.VISIBLE);
        } else {
            // If there is no network connection
            progressIndicator = (ProgressBar) findViewById(R.id.loading_spinner);
            progressIndicator.setVisibility(View.GONE);
            connectionAbsentView.setText(getString(R.string.connection_absent));
        }
    }

    @Override
    public Loader<List<CryptoCurrency>> onCreateLoader(int id, Bundle args) {
        // Create a new loader for the given URL
        return new CryptoCurrencyLoader(this, CryptoCompare_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<CryptoCurrency>> loader, List<CryptoCurrency> data) {

        // Hide loading indicator because the data has been loaded
        progressIndicator = (ProgressBar) findViewById(R.id.loading_spinner);
        progressIndicator.setIndeterminate(false);
        progressIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No Crypto-Currency Exchange Rate found."
        connectionAbsentView.setText(getString(R.string.crypto_exchange_absent));

        // Clear the adapter of previous cryptoCurrency data
        mAdapter.clear();

        // If there is a valid list of {@link CryptoCurrency}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<CryptoCurrency>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

}
