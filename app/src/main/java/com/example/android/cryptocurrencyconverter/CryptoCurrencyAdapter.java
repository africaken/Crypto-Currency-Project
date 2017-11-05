package com.example.android.cryptocurrencyconverter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.example.android.cryptocurrencyconverter.R.id.toSymbol;


public class CryptoCurrencyAdapter extends ArrayAdapter<CryptoCurrency> {

    private String toSymbolAbbreviation = null, currencyName = null;

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the list is the data we want
     * to populate into the lists.
     *
     * @param context        The current context. Used to inflate the layout file.
     * @param cryptoCurrency A List of cryptocurrency exchange objects to display in a list
     */
    public CryptoCurrencyAdapter(Activity context, ArrayList<CryptoCurrency> cryptoCurrency) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, cryptoCurrency);
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The position in the list of data that should be displayed in the
     *                    list item view.
     * @param convertView The recycled view to populate.
     * @param parent      The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get the {@link DeveloperLagos} object located at this position in the list
        CryptoCurrency currentCryptoCurrency = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID currencyExchange_Market
        TextView exchangeMarketTextView = (TextView) listItemView.findViewById(R.id.currencyExchange_Market);

        // Get the crypto-currency Exchange Market from the current CryptoCurrency object and
        // set this text on the exchangeMarketTextView
        String exchangeMarket = null;
        if (currentCryptoCurrency != null) {
            exchangeMarket = currentCryptoCurrency.getCryptoCurrencyMarket();
        }
        exchangeMarketTextView.setText(exchangeMarket);

        // Find the TextView in the list_item.xml layout with the ID fromSymbol
        TextView fromSymbolTextView = (TextView) listItemView.findViewById(R.id.fromSymbol);

        // Get the crypto-currency Exchange fromSymbol from the current CryptoCurrency object and
        // set this text on the fromSymbolTextView
        String fromSymbolAbbreviation = null;
        if (currentCryptoCurrency != null) {
            fromSymbolAbbreviation = currentCryptoCurrency.getFromSymbolCurrencyAbbreviation();
        }
        fromSymbolTextView.setText(fromSymbolAbbreviation);

        // Find the TextView in the list_item.xml layout with the ID toSymbol
        TextView toSymbolTextView = (TextView) listItemView.findViewById(toSymbol);

        // Get the crypto-currency Exchange toSymbol from the current CryptoCurrency object and
        // set this text on the toSymbolTextView
        if (currentCryptoCurrency != null) {
            toSymbolAbbreviation = currentCryptoCurrency.getToSymbolCurrencyAbbreviation();
        }
        toSymbolTextView.setText(toSymbolAbbreviation);

        // Find the TextView in the list_item.xml layout with the ID exchanged_currency
        TextView currencyTextView = (TextView) listItemView.findViewById(R.id.exchanged_currency);

        // Get the crypto-currency Exchange toSymbol from the current CryptoCurrency object,
        // use it to determine the currency being exchanged to and
        // set this text on the currencyTextView

        currencyName = getCurrencyName(toSymbolAbbreviation);
        currencyTextView.setText(currencyName);

        // Find the TextView in the list_item.xml layout with the ID transaction_date
        // with the Date formatted as (Month Day, Year)
        TextView dateView = (TextView) listItemView.findViewById(R.id.transaction_date);
        Date dateObject = null;
        if (currentCryptoCurrency != null) {
            dateObject = new Date(currentCryptoCurrency.getCryptoCurrencyDate() * 1000);
        }
        // Format the date string (i.e. "Wednesday, Oct 20, 2017")
        SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, LLL dd, yyyy", Locale.getDefault());
        String formattedDate = dateFormatter.format(dateObject);
        // Display the date of the current crypto-currency exchange transaction in that TextView.
        dateView.setText(formattedDate);

        // Find the TextView in the list_item.xml layout with the ID transaction_time
        // with the Time formatted as (Hour:Minute AM/PM)
        TextView timeView = (TextView) listItemView.findViewById(R.id.transaction_time);
        Date timeObject = null;
        if (currentCryptoCurrency != null) {
            timeObject = new Date(currentCryptoCurrency.getCryptoCurrencyDate() * 1000);
        }
        // Format the time string (i.e. "4:30PM")
        SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a", Locale.getDefault());
        String formattedTime = timeFormatter.format(timeObject);
        // Display the time of the current crypto-currency exchange transaction in that TextView.
        timeView.setText(formattedTime);

        // Get the crypto-currency Exchange Rate from the current CryptoCurrency object and
        // set this text on the rateExchangeTextView
        TextView rateExchangeTextView = (TextView) listItemView.findViewById(R.id.rateExchange);
        // Format the formattedExchange to show 3 decimal place with "," as thousands separator
        DecimalFormat decimalFormatter = new DecimalFormat("###,###.000");
        String formattedExchange = null;
        if (currentCryptoCurrency != null) {
            formattedExchange = decimalFormatter.format(currentCryptoCurrency.getPriceCurrencyExchange());
        }
        // Display the crypto-currency exchange rate in that TextView.
        rateExchangeTextView.setText(formattedExchange);

        // Return list item view that is showing the appropriate data
        return listItemView;
    }

    private String getCurrencyName(String toSymbolAbbreviation) {
        switch (toSymbolAbbreviation) {
            case "BTC":
                currencyName = "Bitcoin";
                break;
            case "NGN":
                currencyName = "Nigerian Naira";
                break;
            case "EUR":
                currencyName = "European Euro";
                break;
            case "USD":
                currencyName = "United States Dollar";
                break;
            case "GBP":
                currencyName = "Pound Sterling";
                break;
            case "JPY":
                currencyName = "Japanese Yen";
                break;
            case "CNY":
                currencyName = "Chinese Yuan";
                break;
            case "RUB":
                currencyName = "Russian Ruble";
                break;
            case "SAR":
                currencyName = "Saudi Arabian Riyal";
                break;
            case "EGP":
                currencyName = "Egyptian Pound";
                break;
            case "CHF":
                currencyName = "Swiss Franc";
                break;
            case "INR":
                currencyName = "Indian Rupee";
                break;
            case "KRW":
                currencyName = "South Korean Won";
                break;
            case "TRY":
                currencyName = "Turkish Lira";
                break;
            case "ZAR":
                currencyName = "South African Rand";
                break;
            case "LTC":
                currencyName = "Litecoin";
                break;
            case "AUD":
                currencyName = "Australian Dollar";
                break;
            case "NOK":
                currencyName = "Norwegian Krone";
                break;
            case "MAD":
                currencyName = "Moroccoan Dirham";
                break;
            case "SGD":
                currencyName = "Singapore Dollar";
                break;
            default:
                currencyName = "Crypto Currrency";
                break;
        }
        return currencyName;
    }
}
