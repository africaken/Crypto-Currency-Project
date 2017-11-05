package com.example.android.cryptocurrencyconverter;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Crypto_Utils {

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = Crypto_Utils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link Crypto_Utils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private Crypto_Utils() {
    }

    /**
     * Query the cryptocompare api and return a list of {@link CryptoCurrency} objects.
     */
    public static List<CryptoCurrency> fetchCryptoCurrencyData(String requestUrl) {

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Extract relevant fields from the JSON response and create a list of {@link DeveloperLagos}s
        List<CryptoCurrency> cryptoCurrency = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link CryptoCurrency}s
        return cryptoCurrency;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link CryptoCurrency} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<CryptoCurrency> extractFeatureFromJson(String cryptoCurrencyJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(cryptoCurrencyJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding lagosDeveloper to
        List<CryptoCurrency> cryptoCurrency = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(cryptoCurrencyJSON);

            // Extract the JSONObject associated with the key called "RAW"
            JSONObject cryptoCurrencyRaw = baseJsonResponse.getJSONObject("RAW");

            // Extract the JSONObject associated with the key called "ETH"
            JSONObject cryptoCurrencyEth = cryptoCurrencyRaw.getJSONObject("ETH");

            // Extract the JSONObject associated with the key called "BTC"
            JSONObject cryptoCurrencyBtc = cryptoCurrencyEth.getJSONObject("BTC");

            // Extract the value for the key called "MARKET"
            String cryptoCurrencyMarket = cryptoCurrencyBtc.getString("MARKET");

            // Extract the value for the key called "FROMSYMBOL"
            String cryptoCurrencyFromSymbolA = cryptoCurrencyBtc.getString("FROMSYMBOL");

            // Extract the value for the key called "TOSYMBOL"
            String cryptoCurrencyToSymbolA = cryptoCurrencyBtc.getString("TOSYMBOL");

            // Extract the value for the key called "PRICE"
            double cryptoCurrencyExchangeRate = cryptoCurrencyBtc.getDouble("PRICE");

            // Extract the value for the key called "LASTUPDATE"
            long cryptoCurrencyMarketTime = cryptoCurrencyBtc.getLong("LASTUPDATE");

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponseA = new JSONObject(cryptoCurrencyJSON);

            // Extract the JSONObject associated with the key called "DISPLAY"
            JSONObject cryptoCurrencyDisplay = baseJsonResponseA.getJSONObject("DISPLAY");

            // Extract the JSONObject associated with the key called "ETH"
            JSONObject cryptoCurrencyEthA = cryptoCurrencyDisplay.getJSONObject("ETH");

            // Extract the JSONObject associated with the key called "BTC"
            JSONObject cryptoCurrencyBtcA = cryptoCurrencyEthA.getJSONObject("BTC");

            // Extract the value for the key called "FROMSYMBOL"
            String cryptoCurrencyFromSymbol = cryptoCurrencyBtcA.getString("FROMSYMBOL");

            // Extract the value for the key called "TOSYMBOL"
            String cryptoCurrencyToSymbol = cryptoCurrencyBtcA.getString("TOSYMBOL");

            // Create a new {@link CryptoCurrency} object with cryptoCurrencyMarket, cryptoCurrencyFromSymbolA, cryptoCurrencyFromSymbol,
            // cryptoCurrencyToSymbolA, cryptoCurrencyToSymbol, cryptoCurrencyExchangeRate and cryptoCurrencyMarketTime from the JSON response.
            CryptoCurrency currencyExchange = new CryptoCurrency(cryptoCurrencyMarket, cryptoCurrencyFromSymbolA, cryptoCurrencyFromSymbol,
                    cryptoCurrencyToSymbolA, cryptoCurrencyToSymbol, cryptoCurrencyExchangeRate, cryptoCurrencyMarketTime);

            // Add the new {@link CryptoCurrency} to the list of cryptoCurrency.
            cryptoCurrency.add(currencyExchange);

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse1 = new JSONObject(cryptoCurrencyJSON);

            // Extract the JSONObject associated with the key called "RAW"
            JSONObject cryptoCurrencyRaw1 = baseJsonResponse1.getJSONObject("RAW");

            // Extract the JSONObject associated with the key called "ETH"
            JSONObject cryptoCurrencyEth1 = cryptoCurrencyRaw1.getJSONObject("ETH");

            // Extract the JSONObject associated with the key called "NGN"
            JSONObject cryptoCurrencyNgn = cryptoCurrencyEth1.getJSONObject("NGN");

            // Extract the value for the key called "MARKET"
            String cryptoCurrencyMarket1 = cryptoCurrencyNgn.getString("MARKET");

            // Extract the value for the key called "FROMSYMBOL"
            String cryptoCurrencyFromSymbol1A = cryptoCurrencyNgn.getString("FROMSYMBOL");

            // Extract the value for the key called "TOSYMBOL"
            String cryptoCurrencyToSymbol1A = cryptoCurrencyNgn.getString("TOSYMBOL");

            // Extract the value for the key called "PRICE"
            double cryptoCurrencyExchangeRate1 = cryptoCurrencyNgn.getDouble("PRICE");

            // Extract the value for the key called "LASTUPDATE"
            long cryptoCurrencyMarketTime1 = cryptoCurrencyNgn.getLong("LASTUPDATE");

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse1A = new JSONObject(cryptoCurrencyJSON);

            // Extract the JSONObject associated with the key called "DISPLAY"
            JSONObject cryptoCurrencyDisplay1 = baseJsonResponse1A.getJSONObject("DISPLAY");

            // Extract the JSONObject associated with the key called "ETH"
            JSONObject cryptoCurrencyEth1A = cryptoCurrencyDisplay1.getJSONObject("ETH");

            // Extract the JSONObject associated with the key called "NGN"
            JSONObject cryptoCurrencyNgn1A = cryptoCurrencyEth1A.getJSONObject("NGN");

            // Extract the value for the key called "FROMSYMBOL"
            String cryptoCurrencyFromSymbol1 = cryptoCurrencyNgn1A.getString("FROMSYMBOL");

            // Extract the value for the key called "TOSYMBOL"
            String cryptoCurrencyToSymbol1 = cryptoCurrencyNgn1A.getString("TOSYMBOL");

            // Create a new {@link CryptoCurrency} object with cryptoCurrencyMarket1, cryptoCurrencyFromSymbol1A, cryptoCurrencyFromSymbol1,
            // cryptoCurrencyToSymbol1A, cryptoCurrencyToSymbol1, cryptoCurrencyExchangeRate1 and cryptoCurrencyMarketTime1 from the JSON response.
            CryptoCurrency currencyExchange1 = new CryptoCurrency(cryptoCurrencyMarket1, cryptoCurrencyFromSymbol1A, cryptoCurrencyFromSymbol1,
                    cryptoCurrencyToSymbol1A, cryptoCurrencyToSymbol1, cryptoCurrencyExchangeRate1, cryptoCurrencyMarketTime1);

            // Add the new {@link CryptoCurrency} to the list of cryptoCurrency.
            cryptoCurrency.add(currencyExchange1);

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse2 = new JSONObject(cryptoCurrencyJSON);

            // Extract the JSONObject associated with the key called "RAW"
            JSONObject cryptoCurrencyRaw2 = baseJsonResponse2.getJSONObject("RAW");

            // Extract the JSONObject associated with the key called "ETH"
            JSONObject cryptoCurrencyEth2 = cryptoCurrencyRaw2.getJSONObject("ETH");

            // Extract the JSONObject associated with the key called "EUR"
            JSONObject cryptoCurrencyEuro = cryptoCurrencyEth2.getJSONObject("EUR");

            // Extract the value for the key called "MARKET"
            String cryptoCurrencyMarket2 = cryptoCurrencyEuro.getString("MARKET");

            // Extract the value for the key called "FROMSYMBOL"
            String cryptoCurrencyFromSymbol2A = cryptoCurrencyEuro.getString("FROMSYMBOL");

            // Extract the value for the key called "TOSYMBOL"
            String cryptoCurrencyToSymbol2A = cryptoCurrencyEuro.getString("TOSYMBOL");

            // Extract the value for the key called "PRICE"
            double cryptoCurrencyExchangeRate2 = cryptoCurrencyEuro.getDouble("PRICE");

            // Extract the value for the key called "LASTUPDATE"
            long cryptoCurrencyMarketTime2 = cryptoCurrencyEuro.getLong("LASTUPDATE");

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse2A = new JSONObject(cryptoCurrencyJSON);

            // Extract the JSONObject associated with the key called "DISPLAY"
            JSONObject cryptoCurrencyDisplay2 = baseJsonResponse2A.getJSONObject("DISPLAY");

            // Extract the JSONObject associated with the key called "ETH"
            JSONObject cryptoCurrencyEth2A = cryptoCurrencyDisplay2.getJSONObject("ETH");

            // Extract the JSONObject associated with the key called "EUR"
            JSONObject cryptoCurrencyEur2A = cryptoCurrencyEth2A.getJSONObject("EUR");

            // Extract the value for the key called "FROMSYMBOL"
            String cryptoCurrencyFromSymbol2 = cryptoCurrencyEur2A.getString("FROMSYMBOL");

            // Extract the value for the key called "TOSYMBOL"
            String cryptoCurrencyToSymbol2 = cryptoCurrencyEur2A.getString("TOSYMBOL");

            // Create a new {@link CryptoCurrency} object with cryptoCurrencyMarket2, cryptoCurrencyFromSymbol2A, cryptoCurrencyFromSymbol2,
            // cryptoCurrencyToSymbol2A, cryptoCurrencyToSymbol2, cryptoCurrencyExchangeRate2 and cryptoCurrencyMarketTime2 from the JSON response.
            CryptoCurrency currencyExchange2 = new CryptoCurrency(cryptoCurrencyMarket2, cryptoCurrencyFromSymbol2A, cryptoCurrencyFromSymbol2,
                    cryptoCurrencyToSymbol2A, cryptoCurrencyToSymbol2, cryptoCurrencyExchangeRate2, cryptoCurrencyMarketTime2);

            // Add the new {@link CryptoCurrency} to the list of cryptoCurrency.
            cryptoCurrency.add(currencyExchange2);

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse3 = new JSONObject(cryptoCurrencyJSON);

            // Extract the JSONObject associated with the key called "RAW"
            JSONObject cryptoCurrencyRaw3 = baseJsonResponse3.getJSONObject("RAW");

            // Extract the JSONObject associated with the key called "ETH"
            JSONObject cryptoCurrencyEth3 = cryptoCurrencyRaw3.getJSONObject("ETH");

            // Extract the JSONObject associated with the key called "USD"
            JSONObject cryptoCurrencyUsd = cryptoCurrencyEth3.getJSONObject("USD");

            // Extract the value for the key called "MARKET"
            String cryptoCurrencyMarket3 = cryptoCurrencyUsd.getString("MARKET");

            // Extract the value for the key called "FROMSYMBOL"
            String cryptoCurrencyFromSymbol3A = cryptoCurrencyUsd.getString("FROMSYMBOL");

            // Extract the value for the key called "TOSYMBOL"
            String cryptoCurrencyToSymbol3A = cryptoCurrencyUsd.getString("TOSYMBOL");

            // Extract the value for the key called "PRICE"
            double cryptoCurrencyExchangeRate3 = cryptoCurrencyUsd.getDouble("PRICE");

            // Extract the value for the key called "LASTUPDATE"
            long cryptoCurrencyMarketTime3 = cryptoCurrencyUsd.getLong("LASTUPDATE");

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse3A = new JSONObject(cryptoCurrencyJSON);

            // Extract the JSONObject associated with the key called "DISPLAY"
            JSONObject cryptoCurrencyDisplay3 = baseJsonResponse3A.getJSONObject("DISPLAY");

            // Extract the JSONObject associated with the key called "ETH"
            JSONObject cryptoCurrencyEth3A = cryptoCurrencyDisplay3.getJSONObject("ETH");

            // Extract the JSONObject associated with the key called "USD"
            JSONObject cryptoCurrencyUsd3A = cryptoCurrencyEth3A.getJSONObject("USD");

            // Extract the value for the key called "FROMSYMBOL"
            String cryptoCurrencyFromSymbol3 = cryptoCurrencyUsd3A.getString("FROMSYMBOL");

            // Extract the value for the key called "TOSYMBOL"
            String cryptoCurrencyToSymbol3 = cryptoCurrencyUsd3A.getString("TOSYMBOL");

            // Create a new {@link CryptoCurrency} object with cryptoCurrencyMarket3, cryptoCurrencyFromSymbol3A, cryptoCurrencyFromSymbol3,
            // cryptoCurrencyToSymbol3A, cryptoCurrencyToSymbol3, cryptoCurrencyExchangeRate3 and cryptoCurrencyMarketTime3 from the JSON response.
            CryptoCurrency currencyExchange3 = new CryptoCurrency(cryptoCurrencyMarket3, cryptoCurrencyFromSymbol3A, cryptoCurrencyFromSymbol3,
                    cryptoCurrencyToSymbol3A, cryptoCurrencyToSymbol3, cryptoCurrencyExchangeRate3, cryptoCurrencyMarketTime3);

            // Add the new {@link CryptoCurrency} to the list of cryptoCurrency.
            cryptoCurrency.add(currencyExchange3);

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse4 = new JSONObject(cryptoCurrencyJSON);

            // Extract the JSONObject associated with the key called "RAW"
            JSONObject cryptoCurrencyRaw4 = baseJsonResponse4.getJSONObject("RAW");

            // Extract the JSONObject associated with the key called "ETH"
            JSONObject cryptoCurrencyEth4 = cryptoCurrencyRaw4.getJSONObject("ETH");

            // Extract the JSONObject associated with the key called "GBP"
            JSONObject cryptoCurrencyGbp = cryptoCurrencyEth4.getJSONObject("GBP");

            // Extract the value for the key called "MARKET"
            String cryptoCurrencyMarket4 = cryptoCurrencyGbp.getString("MARKET");

            // Extract the value for the key called "FROMSYMBOL"
            String cryptoCurrencyFromSymbol4A = cryptoCurrencyGbp.getString("FROMSYMBOL");

            // Extract the value for the key called "TOSYMBOL"
            String cryptoCurrencyToSymbol4A = cryptoCurrencyGbp.getString("TOSYMBOL");

            // Extract the value for the key called "PRICE"
            double cryptoCurrencyExchangeRate4 = cryptoCurrencyGbp.getDouble("PRICE");

            // Extract the value for the key called "LASTUPDATE"
            long cryptoCurrencyMarketTime4 = cryptoCurrencyGbp.getLong("LASTUPDATE");

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse4A = new JSONObject(cryptoCurrencyJSON);

            // Extract the JSONObject associated with the key called "DISPLAY"
            JSONObject cryptoCurrencyDisplay4 = baseJsonResponse4A.getJSONObject("DISPLAY");

            // Extract the JSONObject associated with the key called "ETH"
            JSONObject cryptoCurrencyEth4A = cryptoCurrencyDisplay4.getJSONObject("ETH");

            // Extract the JSONObject associated with the key called "GBP"
            JSONObject cryptoCurrencyGbp4A = cryptoCurrencyEth4A.getJSONObject("GBP");

            // Extract the value for the key called "FROMSYMBOL"
            String cryptoCurrencyFromSymbol4 = cryptoCurrencyGbp4A.getString("FROMSYMBOL");

            // Extract the value for the key called "TOSYMBOL"
            String cryptoCurrencyToSymbol4 = cryptoCurrencyGbp4A.getString("TOSYMBOL");

            // Create a new {@link CryptoCurrency} object with cryptoCurrencyMarket4, cryptoCurrencyFromSymbol4A, cryptoCurrencyFromSymbol4,
            // cryptoCurrencyToSymbol4A, cryptoCurrencyToSymbol4, cryptoCurrencyExchangeRate4 and cryptoCurrencyMarketTime4 from the JSON response.
            CryptoCurrency currencyExchange4 = new CryptoCurrency(cryptoCurrencyMarket4, cryptoCurrencyFromSymbol4A, cryptoCurrencyFromSymbol4,
                    cryptoCurrencyToSymbol4A, cryptoCurrencyToSymbol4, cryptoCurrencyExchangeRate4, cryptoCurrencyMarketTime4);

            // Add the new {@link CryptoCurrency} to the list of cryptoCurrency.
            cryptoCurrency.add(currencyExchange4);

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse5 = new JSONObject(cryptoCurrencyJSON);

            // Extract the JSONObject associated with the key called "RAW"
            JSONObject cryptoCurrencyRaw5 = baseJsonResponse5.getJSONObject("RAW");

            // Extract the JSONObject associated with the key called "ETH"
            JSONObject cryptoCurrencyEth5 = cryptoCurrencyRaw5.getJSONObject("ETH");

            // Extract the JSONObject associated with the key called "JPY"
            JSONObject cryptoCurrencyJpy = cryptoCurrencyEth5.getJSONObject("JPY");

            // Extract the value for the key called "MARKET"
            String cryptoCurrencyMarket5 = cryptoCurrencyJpy.getString("MARKET");

            // Extract the value for the key called "FROMSYMBOL"
            String cryptoCurrencyFromSymbol5A = cryptoCurrencyJpy.getString("FROMSYMBOL");

            // Extract the value for the key called "TOSYMBOL"
            String cryptoCurrencyToSymbol5A = cryptoCurrencyJpy.getString("TOSYMBOL");

            // Extract the value for the key called "PRICE"
            double cryptoCurrencyExchangeRate5 = cryptoCurrencyJpy.getDouble("PRICE");

            // Extract the value for the key called "LASTUPDATE"
            long cryptoCurrencyMarketTime5 = cryptoCurrencyJpy.getLong("LASTUPDATE");

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse5A = new JSONObject(cryptoCurrencyJSON);

            // Extract the JSONObject associated with the key called "DISPLAY"
            JSONObject cryptoCurrencyDisplay5 = baseJsonResponse5A.getJSONObject("DISPLAY");

            // Extract the JSONObject associated with the key called "ETH"
            JSONObject cryptoCurrencyEth5A = cryptoCurrencyDisplay5.getJSONObject("ETH");

            // Extract the JSONObject associated with the key called "JPY"
            JSONObject cryptoCurrencyJpy5A = cryptoCurrencyEth5A.getJSONObject("JPY");

            // Extract the value for the key called "FROMSYMBOL"
            String cryptoCurrencyFromSymbol5 = cryptoCurrencyJpy5A.getString("FROMSYMBOL");

            // Extract the value for the key called "TOSYMBOL"
            String cryptoCurrencyToSymbol5 = cryptoCurrencyJpy5A.getString("TOSYMBOL");

            // Create a new {@link CryptoCurrency} object with cryptoCurrencyMarket5, cryptoCurrencyFromSymbol5A, cryptoCurrencyFromSymbol5,
            // cryptoCurrencyToSymbol5A, cryptoCurrencyToSymbol5, cryptoCurrencyExchangeRate5 and cryptoCurrencyMarketTime5 from the JSON response.
            CryptoCurrency currencyExchange5 = new CryptoCurrency(cryptoCurrencyMarket5, cryptoCurrencyFromSymbol5A, cryptoCurrencyFromSymbol5,
                    cryptoCurrencyToSymbol5A, cryptoCurrencyToSymbol5, cryptoCurrencyExchangeRate5, cryptoCurrencyMarketTime5);

            // Add the new {@link CryptoCurrency} to the list of cryptoCurrency.
            cryptoCurrency.add(currencyExchange5);

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse6 = new JSONObject(cryptoCurrencyJSON);

            // Extract the JSONObject associated with the key called "RAW"
            JSONObject cryptoCurrencyRaw6 = baseJsonResponse6.getJSONObject("RAW");

            // Extract the JSONObject associated with the key called "ETH"
            JSONObject cryptoCurrencyEth6 = cryptoCurrencyRaw6.getJSONObject("ETH");

            // Extract the JSONObject associated with the key called "CNY"
            JSONObject cryptoCurrencyCny = cryptoCurrencyEth6.getJSONObject("CNY");

            // Extract the value for the key called "MARKET"
            String cryptoCurrencyMarket6 = cryptoCurrencyCny.getString("MARKET");

            // Extract the value for the key called "FROMSYMBOL"
            String cryptoCurrencyFromSymbol6A = cryptoCurrencyCny.getString("FROMSYMBOL");

            // Extract the value for the key called "TOSYMBOL"
            String cryptoCurrencyToSymbol6A = cryptoCurrencyCny.getString("TOSYMBOL");

            // Extract the value for the key called "PRICE"
            double cryptoCurrencyExchangeRate6 = cryptoCurrencyCny.getDouble("PRICE");

            // Extract the value for the key called "LASTUPDATE"
            long cryptoCurrencyMarketTime6 = cryptoCurrencyCny.getLong("LASTUPDATE");

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse6A = new JSONObject(cryptoCurrencyJSON);

            // Extract the JSONObject associated with the key called "DISPLAY"
            JSONObject cryptoCurrencyDisplay6 = baseJsonResponse6A.getJSONObject("DISPLAY");

            // Extract the JSONObject associated with the key called "ETH"
            JSONObject cryptoCurrencyEth6A = cryptoCurrencyDisplay6.getJSONObject("ETH");

            // Extract the JSONObject associated with the key called "CNY"
            JSONObject cryptoCurrencyCny6A = cryptoCurrencyEth6A.getJSONObject("CNY");

            // Extract the value for the key called "FROMSYMBOL"
            String cryptoCurrencyFromSymbol6 = cryptoCurrencyCny6A.getString("FROMSYMBOL");

            // Extract the value for the key called "TOSYMBOL"
            String cryptoCurrencyToSymbol6 = cryptoCurrencyCny6A.getString("TOSYMBOL");

            // Create a new {@link CryptoCurrency} object with cryptoCurrencyMarket6, cryptoCurrencyFromSymbol6A, cryptoCurrencyFromSymbol6,
            // cryptoCurrencyToSymbol6A, cryptoCurrencyToSymbol6, cryptoCurrencyExchangeRate6 and cryptoCurrencyMarketTime6 from the JSON response.
            CryptoCurrency currencyExchange6 = new CryptoCurrency(cryptoCurrencyMarket6, cryptoCurrencyFromSymbol6A, cryptoCurrencyFromSymbol6,
                    cryptoCurrencyToSymbol6A, cryptoCurrencyToSymbol6, cryptoCurrencyExchangeRate6, cryptoCurrencyMarketTime6);

            // Add the new {@link CryptoCurrency} to the list of cryptoCurrency.
            cryptoCurrency.add(currencyExchange6);

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse7 = new JSONObject(cryptoCurrencyJSON);

            // Extract the JSONObject associated with the key called "RAW"
            JSONObject cryptoCurrencyRaw7 = baseJsonResponse7.getJSONObject("RAW");

            // Extract the JSONObject associated with the key called "ETH"
            JSONObject cryptoCurrencyEth7 = cryptoCurrencyRaw7.getJSONObject("ETH");

            // Extract the JSONObject associated with the key called "RUB"
            JSONObject cryptoCurrencyRub = cryptoCurrencyEth7.getJSONObject("RUB");

            // Extract the value for the key called "MARKET"
            String cryptoCurrencyMarket7 = cryptoCurrencyUsd.getString("MARKET");

            // Extract the value for the key called "FROMSYMBOL"
            String cryptoCurrencyFromSymbol7A = cryptoCurrencyRub.getString("FROMSYMBOL");

            // Extract the value for the key called "TOSYMBOL"
            String cryptoCurrencyToSymbol7A = cryptoCurrencyRub.getString("TOSYMBOL");

            // Extract the value for the key called "PRICE"
            double cryptoCurrencyExchangeRate7 = cryptoCurrencyRub.getDouble("PRICE");

            // Extract the value for the key called "LASTUPDATE"
            long cryptoCurrencyMarketTime7 = cryptoCurrencyRub.getLong("LASTUPDATE");

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse7A = new JSONObject(cryptoCurrencyJSON);

            // Extract the JSONObject associated with the key called "DISPLAY"
            JSONObject cryptoCurrencyDisplay7 = baseJsonResponse7A.getJSONObject("DISPLAY");

            // Extract the JSONObject associated with the key called "ETH"
            JSONObject cryptoCurrencyEth7A = cryptoCurrencyDisplay7.getJSONObject("ETH");

            // Extract the JSONObject associated with the key called "RUB"
            JSONObject cryptoCurrencyRub7A = cryptoCurrencyEth7A.getJSONObject("RUB");

            // Extract the value for the key called "FROMSYMBOL"
            String cryptoCurrencyFromSymbol7 = cryptoCurrencyRub7A.getString("FROMSYMBOL");

            // Extract the value for the key called "TOSYMBOL"
            String cryptoCurrencyToSymbol7 = cryptoCurrencyRub7A.getString("TOSYMBOL");

            // Create a new {@link CryptoCurrency} object with cryptoCurrencyMarket7, cryptoCurrencyFromSymbol7A, cryptoCurrencyFromSymbol7,
            // cryptoCurrencyToSymbol7A, cryptoCurrencyToSymbol7, cryptoCurrencyExchangeRate7 and cryptoCurrencyMarketTime7 from the JSON response.
            CryptoCurrency currencyExchange7 = new CryptoCurrency(cryptoCurrencyMarket7, cryptoCurrencyFromSymbol7A, cryptoCurrencyFromSymbol7,
                    cryptoCurrencyToSymbol7A, cryptoCurrencyToSymbol7, cryptoCurrencyExchangeRate7, cryptoCurrencyMarketTime7);

            // Add the new {@link CryptoCurrency} to the list of cryptoCurrency.
            cryptoCurrency.add(currencyExchange7);

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse8 = new JSONObject(cryptoCurrencyJSON);

            // Extract the JSONObject associated with the key called "RAW"
            JSONObject cryptoCurrencyRaw8 = baseJsonResponse8.getJSONObject("RAW");

            // Extract the JSONObject associated with the key called "ETH"
            JSONObject cryptoCurrencyEth8 = cryptoCurrencyRaw8.getJSONObject("ETH");

            // Extract the JSONObject associated with the key called "SAR"
            JSONObject cryptoCurrencySar = cryptoCurrencyEth8.getJSONObject("SAR");

            // Extract the value for the key called "MARKET"
            String cryptoCurrencyMarket8 = cryptoCurrencySar.getString("MARKET");

            // Extract the value for the key called "FROMSYMBOL"
            String cryptoCurrencyFromSymbol8A = cryptoCurrencySar.getString("FROMSYMBOL");

            // Extract the value for the key called "TOSYMBOL"
            String cryptoCurrencyToSymbol8A = cryptoCurrencySar.getString("TOSYMBOL");

            // Extract the value for the key called "PRICE"
            double cryptoCurrencyExchangeRate8 = cryptoCurrencySar.getDouble("PRICE");

            // Extract the value for the key called "LASTUPDATE"
            long cryptoCurrencyMarketTime8 = cryptoCurrencySar.getLong("LASTUPDATE");

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse8A = new JSONObject(cryptoCurrencyJSON);

            // Extract the JSONObject associated with the key called "DISPLAY"
            JSONObject cryptoCurrencyDisplay8 = baseJsonResponse8A.getJSONObject("DISPLAY");

            // Extract the JSONObject associated with the key called "ETH"
            JSONObject cryptoCurrencyEth8A = cryptoCurrencyDisplay8.getJSONObject("ETH");

            // Extract the JSONObject associated with the key called "SAR"
            JSONObject cryptoCurrencySar8A = cryptoCurrencyEth8A.getJSONObject("SAR");

            // Extract the value for the key called "FROMSYMBOL"
            String cryptoCurrencyFromSymbol8 = cryptoCurrencySar8A.getString("FROMSYMBOL");

            // Extract the value for the key called "TOSYMBOL"
            String cryptoCurrencyToSymbol8 = cryptoCurrencySar8A.getString("TOSYMBOL");

            // Create a new {@link CryptoCurrency} object with cryptoCurrencyMarket8, cryptoCurrencyFromSymbol8A, cryptoCurrencyFromSymbol8,
            // cryptoCurrencyToSymbol8A, cryptoCurrencyToSymbol8, cryptoCurrencyExchangeRate8 and cryptoCurrencyMarketTime8 from the JSON response.
            CryptoCurrency currencyExchange8 = new CryptoCurrency(cryptoCurrencyMarket8, cryptoCurrencyFromSymbol8A, cryptoCurrencyFromSymbol8,
                    cryptoCurrencyToSymbol8A, cryptoCurrencyToSymbol8, cryptoCurrencyExchangeRate8, cryptoCurrencyMarketTime8);

            // Add the new {@link CryptoCurrency} to the list of cryptoCurrency.
            cryptoCurrency.add(currencyExchange8);

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse9 = new JSONObject(cryptoCurrencyJSON);

            // Extract the JSONObject associated with the key called "RAW"
            JSONObject cryptoCurrencyRaw9 = baseJsonResponse9.getJSONObject("RAW");

            // Extract the JSONObject associated with the key called "ETH"
            JSONObject cryptoCurrencyEth9 = cryptoCurrencyRaw9.getJSONObject("ETH");

            // Extract the JSONObject associated with the key called "EGP"
            JSONObject cryptoCurrencyEgp = cryptoCurrencyEth9.getJSONObject("EGP");

            // Extract the value for the key called "MARKET"
            String cryptoCurrencyMarket9 = cryptoCurrencyEgp.getString("MARKET");

            // Extract the value for the key called "FROMSYMBOL"
            String cryptoCurrencyFromSymbol9A = cryptoCurrencyEgp.getString("FROMSYMBOL");

            // Extract the value for the key called "TOSYMBOL"
            String cryptoCurrencyToSymbol9A = cryptoCurrencyEgp.getString("TOSYMBOL");

            // Extract the value for the key called "PRICE"
            double cryptoCurrencyExchangeRate9 = cryptoCurrencyEgp.getDouble("PRICE");

            // Extract the value for the key called "LASTUPDATE"
            long cryptoCurrencyMarketTime9 = cryptoCurrencyEgp.getLong("LASTUPDATE");

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse9A = new JSONObject(cryptoCurrencyJSON);

            // Extract the JSONObject associated with the key called "DISPLAY"
            JSONObject cryptoCurrencyDisplay9 = baseJsonResponse9A.getJSONObject("DISPLAY");

            // Extract the JSONObject associated with the key called "ETH"
            JSONObject cryptoCurrencyEth9A = cryptoCurrencyDisplay9.getJSONObject("ETH");

            // Extract the JSONObject associated with the key called "EGP"
            JSONObject cryptoCurrencyEgp9A = cryptoCurrencyEth9A.getJSONObject("EGP");

            // Extract the value for the key called "FROMSYMBOL"
            String cryptoCurrencyFromSymbol9 = cryptoCurrencyEgp9A.getString("FROMSYMBOL");

            // Extract the value for the key called "TOSYMBOL"
            String cryptoCurrencyToSymbol9 = cryptoCurrencyEgp9A.getString("TOSYMBOL");

            // Create a new {@link CryptoCurrency} object with cryptoCurrencyMarket9, cryptoCurrencyFromSymbol9A, cryptoCurrencyFromSymbol9,
            // cryptoCurrencyToSymbol9A, cryptoCurrencyToSymbol9, cryptoCurrencyExchangeRate9 and cryptoCurrencyMarketTime9 from the JSON response.
            CryptoCurrency currencyExchange9 = new CryptoCurrency(cryptoCurrencyMarket9, cryptoCurrencyFromSymbol9A, cryptoCurrencyFromSymbol9,
                    cryptoCurrencyToSymbol9A, cryptoCurrencyToSymbol9, cryptoCurrencyExchangeRate9, cryptoCurrencyMarketTime9);

            // Add the new {@link CryptoCurrency} to the list of cryptoCurrency.
            cryptoCurrency.add(currencyExchange9);

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse10 = new JSONObject(cryptoCurrencyJSON);

            // Extract the JSONObject associated with the key called "RAW"
            JSONObject cryptoCurrencyRaw10 = baseJsonResponse10.getJSONObject("RAW");

            // Extract the JSONObject associated with the key called "ETH"
            JSONObject cryptoCurrencyEth10 = cryptoCurrencyRaw10.getJSONObject("ETH");

            // Extract the JSONObject associated with the key called "CHF"
            JSONObject cryptoCurrencyChf = cryptoCurrencyEth10.getJSONObject("CHF");

            // Extract the value for the key called "MARKET"
            String cryptoCurrencyMarket10 = cryptoCurrencyUsd.getString("MARKET");

            // Extract the value for the key called "FROMSYMBOL"
            String cryptoCurrencyFromSymbol10A = cryptoCurrencyChf.getString("FROMSYMBOL");

            // Extract the value for the key called "TOSYMBOL"
            String cryptoCurrencyToSymbol10A = cryptoCurrencyChf.getString("TOSYMBOL");

            // Extract the value for the key called "PRICE"
            double cryptoCurrencyExchangeRate10 = cryptoCurrencyChf.getDouble("PRICE");

            // Extract the value for the key called "LASTUPDATE"
            long cryptoCurrencyMarketTime10 = cryptoCurrencyChf.getLong("LASTUPDATE");

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse10A = new JSONObject(cryptoCurrencyJSON);

            // Extract the JSONObject associated with the key called "DISPLAY"
            JSONObject cryptoCurrencyDisplay10 = baseJsonResponse10A.getJSONObject("DISPLAY");

            // Extract the JSONObject associated with the key called "ETH"
            JSONObject cryptoCurrencyEth10A = cryptoCurrencyDisplay10.getJSONObject("ETH");

            // Extract the JSONObject associated with the key called "CHF"
            JSONObject cryptoCurrencyChf10A = cryptoCurrencyEth10A.getJSONObject("CHF");

            // Extract the value for the key called "FROMSYMBOL"
            String cryptoCurrencyFromSymbol10 = cryptoCurrencyChf10A.getString("FROMSYMBOL");

            // Extract the value for the key called "TOSYMBOL"
            String cryptoCurrencyToSymbol10 = cryptoCurrencyChf10A.getString("TOSYMBOL");

            // Create a new {@link CryptoCurrency} object with cryptoCurrencyMarket10, cryptoCurrencyFromSymbol10A, cryptoCurrencyFromSymbol10,
            // cryptoCurrencyToSymbol10A, cryptoCurrencyToSymbol10, cryptoCurrencyExchangeRate10 and cryptoCurrencyMarketTime10 from the JSON response.
            CryptoCurrency currencyExchange10 = new CryptoCurrency(cryptoCurrencyMarket10, cryptoCurrencyFromSymbol10A, cryptoCurrencyFromSymbol10,
                    cryptoCurrencyToSymbol10A, cryptoCurrencyToSymbol10, cryptoCurrencyExchangeRate10, cryptoCurrencyMarketTime10);

            // Add the new {@link CryptoCurrency} to the list of cryptoCurrency.
            cryptoCurrency.add(currencyExchange10);

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse11 = new JSONObject(cryptoCurrencyJSON);

            // Extract the JSONObject associated with the key called "RAW"
            JSONObject cryptoCurrencyRaw11 = baseJsonResponse11.getJSONObject("RAW");

            // Extract the JSONObject associated with the key called "ETH"
            JSONObject cryptoCurrencyEth11 = cryptoCurrencyRaw11.getJSONObject("ETH");

            // Extract the JSONObject associated with the key called "INR"
            JSONObject cryptoCurrencyInr = cryptoCurrencyEth11.getJSONObject("INR");

            // Extract the value for the key called "MARKET"
            String cryptoCurrencyMarket11 = cryptoCurrencyInr.getString("MARKET");

            // Extract the value for the key called "FROMSYMBOL"
            String cryptoCurrencyFromSymbol11A = cryptoCurrencyInr.getString("FROMSYMBOL");

            // Extract the value for the key called "TOSYMBOL"
            String cryptoCurrencyToSymbol11A = cryptoCurrencyInr.getString("TOSYMBOL");

            // Extract the value for the key called "PRICE"
            double cryptoCurrencyExchangeRate11 = cryptoCurrencyInr.getDouble("PRICE");

            // Extract the value for the key called "LASTUPDATE"
            long cryptoCurrencyMarketTime11 = cryptoCurrencyInr.getLong("LASTUPDATE");

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse11A = new JSONObject(cryptoCurrencyJSON);

            // Extract the JSONObject associated with the key called "DISPLAY"
            JSONObject cryptoCurrencyDisplay11 = baseJsonResponse11A.getJSONObject("DISPLAY");

            // Extract the JSONObject associated with the key called "ETH"
            JSONObject cryptoCurrencyEth11A = cryptoCurrencyDisplay11.getJSONObject("ETH");

            // Extract the JSONObject associated with the key called "INR"
            JSONObject cryptoCurrencyInr11A = cryptoCurrencyEth11A.getJSONObject("INR");

            // Extract the value for the key called "FROMSYMBOL"
            String cryptoCurrencyFromSymbol11 = cryptoCurrencyInr11A.getString("FROMSYMBOL");

            // Extract the value for the key called "TOSYMBOL"
            String cryptoCurrencyToSymbol11 = cryptoCurrencyInr11A.getString("TOSYMBOL");

            // Create a new {@link CryptoCurrency} object with cryptoCurrencyMarket11, cryptoCurrencyFromSymbol11A, cryptoCurrencyFromSymbol11,
            // cryptoCurrencyToSymbol11A, cryptoCurrencyToSymbol11, cryptoCurrencyExchangeRate11 and cryptoCurrencyMarketTime11 from the JSON response.
            CryptoCurrency currencyExchange11 = new CryptoCurrency(cryptoCurrencyMarket11, cryptoCurrencyFromSymbol11A, cryptoCurrencyFromSymbol11,
                    cryptoCurrencyToSymbol11A, cryptoCurrencyToSymbol11, cryptoCurrencyExchangeRate11, cryptoCurrencyMarketTime11);

            // Add the new {@link CryptoCurrency} to the list of cryptoCurrency.
            cryptoCurrency.add(currencyExchange11);

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse12 = new JSONObject(cryptoCurrencyJSON);

            // Extract the JSONObject associated with the key called "RAW"
            JSONObject cryptoCurrencyRaw12 = baseJsonResponse12.getJSONObject("RAW");

            // Extract the JSONObject associated with the key called "ETH"
            JSONObject cryptoCurrencyEth12 = cryptoCurrencyRaw12.getJSONObject("ETH");

            // Extract the JSONObject associated with the key called "KRW"
            JSONObject cryptoCurrencyKrw = cryptoCurrencyEth12.getJSONObject("KRW");

            // Extract the value for the key called "MARKET"
            String cryptoCurrencyMarket12 = cryptoCurrencyKrw.getString("MARKET");

            // Extract the value for the key called "FROMSYMBOL"
            String cryptoCurrencyFromSymbol12A = cryptoCurrencyKrw.getString("FROMSYMBOL");

            // Extract the value for the key called "TOSYMBOL"
            String cryptoCurrencyToSymbol12A = cryptoCurrencyKrw.getString("TOSYMBOL");

            // Extract the value for the key called "PRICE"
            double cryptoCurrencyExchangeRate12 = cryptoCurrencyKrw.getDouble("PRICE");

            // Extract the value for the key called "LASTUPDATE"
            long cryptoCurrencyMarketTime12 = cryptoCurrencyKrw.getLong("LASTUPDATE");

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse12A = new JSONObject(cryptoCurrencyJSON);

            // Extract the JSONObject associated with the key called "DISPLAY"
            JSONObject cryptoCurrencyDisplay12 = baseJsonResponse12A.getJSONObject("DISPLAY");

            // Extract the JSONObject associated with the key called "ETH"
            JSONObject cryptoCurrencyEth12A = cryptoCurrencyDisplay12.getJSONObject("ETH");

            // Extract the JSONObject associated with the key called "KRW"
            JSONObject cryptoCurrencyKrw12A = cryptoCurrencyEth12A.getJSONObject("KRW");

            // Extract the value for the key called "FROMSYMBOL"
            String cryptoCurrencyFromSymbol12 = cryptoCurrencyKrw12A.getString("FROMSYMBOL");

            // Extract the value for the key called "TOSYMBOL"
            String cryptoCurrencyToSymbol12 = cryptoCurrencyKrw12A.getString("TOSYMBOL");

            // Create a new {@link CryptoCurrency} object with cryptoCurrencyMarket12, cryptoCurrencyFromSymbol12A, cryptoCurrencyFromSymbol12,
            // cryptoCurrencyToSymbol12A, cryptoCurrencyToSymbol12, cryptoCurrencyExchangeRate12 and cryptoCurrencyMarketTime12 from the JSON response.
            CryptoCurrency currencyExchange12 = new CryptoCurrency(cryptoCurrencyMarket12, cryptoCurrencyFromSymbol12A, cryptoCurrencyFromSymbol12,
                    cryptoCurrencyToSymbol12A, cryptoCurrencyToSymbol12, cryptoCurrencyExchangeRate12, cryptoCurrencyMarketTime12);

            // Add the new {@link CryptoCurrency} to the list of cryptoCurrency.
            cryptoCurrency.add(currencyExchange12);

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse13 = new JSONObject(cryptoCurrencyJSON);

            // Extract the JSONObject associated with the key called "RAW"
            JSONObject cryptoCurrencyRaw13 = baseJsonResponse13.getJSONObject("RAW");

            // Extract the JSONObject associated with the key called "ETH"
            JSONObject cryptoCurrencyEth13 = cryptoCurrencyRaw13.getJSONObject("ETH");

            // Extract the JSONObject associated with the key called "TRY"
            JSONObject cryptoCurrencyTry = cryptoCurrencyEth13.getJSONObject("TRY");

            // Extract the value for the key called "MARKET"
            String cryptoCurrencyMarket13 = cryptoCurrencyTry.getString("MARKET");

            // Extract the value for the key called "FROMSYMBOL"
            String cryptoCurrencyFromSymbol13A = cryptoCurrencyTry.getString("FROMSYMBOL");

            // Extract the value for the key called "TOSYMBOL"
            String cryptoCurrencyToSymbol13A = cryptoCurrencyTry.getString("TOSYMBOL");

            // Extract the value for the key called "PRICE"
            double cryptoCurrencyExchangeRate13 = cryptoCurrencyTry.getDouble("PRICE");

            // Extract the value for the key called "LASTUPDATE"
            long cryptoCurrencyMarketTime13 = cryptoCurrencyTry.getLong("LASTUPDATE");

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse13A = new JSONObject(cryptoCurrencyJSON);

            // Extract the JSONObject associated with the key called "DISPLAY"
            JSONObject cryptoCurrencyDisplay13 = baseJsonResponse13A.getJSONObject("DISPLAY");

            // Extract the JSONObject associated with the key called "ETH"
            JSONObject cryptoCurrencyEth13A = cryptoCurrencyDisplay13.getJSONObject("ETH");

            // Extract the JSONObject associated with the key called "TRY"
            JSONObject cryptoCurrencyTry13A = cryptoCurrencyEth13A.getJSONObject("TRY");

            // Extract the value for the key called "FROMSYMBOL"
            String cryptoCurrencyFromSymbol13 = cryptoCurrencyTry13A.getString("FROMSYMBOL");

            // Extract the value for the key called "TOSYMBOL"
            String cryptoCurrencyToSymbol13 = cryptoCurrencyTry13A.getString("TOSYMBOL");

            // Create a new {@link CryptoCurrency} object with cryptoCurrencyMarket13, cryptoCurrencyFromSymbol13A, cryptoCurrencyFromSymbol13,
            // cryptoCurrencyToSymbol13A, cryptoCurrencyToSymbol13, cryptoCurrencyExchangeRate13 and cryptoCurrencyMarketTime13 from the JSON response.
            CryptoCurrency currencyExchange13 = new CryptoCurrency(cryptoCurrencyMarket13, cryptoCurrencyFromSymbol13A, cryptoCurrencyFromSymbol13,
                    cryptoCurrencyToSymbol13A, cryptoCurrencyToSymbol13, cryptoCurrencyExchangeRate13, cryptoCurrencyMarketTime13);

            // Add the new {@link CryptoCurrency} to the list of cryptoCurrency.
            cryptoCurrency.add(currencyExchange13);

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse14 = new JSONObject(cryptoCurrencyJSON);

            // Extract the JSONObject associated with the key called "RAW"
            JSONObject cryptoCurrencyRaw14 = baseJsonResponse14.getJSONObject("RAW");

            // Extract the JSONObject associated with the key called "ETH"
            JSONObject cryptoCurrencyEth14 = cryptoCurrencyRaw14.getJSONObject("ETH");

            // Extract the JSONObject associated with the key called "ZAR"
            JSONObject cryptoCurrencyZar = cryptoCurrencyEth14.getJSONObject("ZAR");

            // Extract the value for the key called "MARKET"
            String cryptoCurrencyMarket14 = cryptoCurrencyZar.getString("MARKET");

            // Extract the value for the key called "FROMSYMBOL"
            String cryptoCurrencyFromSymbol14A = cryptoCurrencyZar.getString("FROMSYMBOL");

            // Extract the value for the key called "TOSYMBOL"
            String cryptoCurrencyToSymbol14A = cryptoCurrencyZar.getString("TOSYMBOL");

            // Extract the value for the key called "PRICE"
            double cryptoCurrencyExchangeRate14 = cryptoCurrencyZar.getDouble("PRICE");

            // Extract the value for the key called "LASTUPDATE"
            long cryptoCurrencyMarketTime14 = cryptoCurrencyZar.getLong("LASTUPDATE");

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse14A = new JSONObject(cryptoCurrencyJSON);

            // Extract the JSONObject associated with the key called "DISPLAY"
            JSONObject cryptoCurrencyDisplay14 = baseJsonResponse14A.getJSONObject("DISPLAY");

            // Extract the JSONObject associated with the key called "ETH"
            JSONObject cryptoCurrencyEth14A = cryptoCurrencyDisplay14.getJSONObject("ETH");

            // Extract the JSONObject associated with the key called "ZAR"
            JSONObject cryptoCurrencyZar14A = cryptoCurrencyEth14A.getJSONObject("ZAR");

            // Extract the value for the key called "FROMSYMBOL"
            String cryptoCurrencyFromSymbol14 = cryptoCurrencyZar14A.getString("FROMSYMBOL");

            // Extract the value for the key called "TOSYMBOL"
            String cryptoCurrencyToSymbol14 = cryptoCurrencyZar14A.getString("TOSYMBOL");

            // Create a new {@link CryptoCurrency} object with cryptoCurrencyMarket14, cryptoCurrencyFromSymbol14A, cryptoCurrencyFromSymbol14,
            // cryptoCurrencyToSymbol14A, cryptoCurrencyToSymbol14, cryptoCurrencyExchangeRate14 and cryptoCurrencyMarketTime14 from the JSON response.
            CryptoCurrency currencyExchange14 = new CryptoCurrency(cryptoCurrencyMarket14, cryptoCurrencyFromSymbol14A, cryptoCurrencyFromSymbol14,
                    cryptoCurrencyToSymbol14A, cryptoCurrencyToSymbol14, cryptoCurrencyExchangeRate14, cryptoCurrencyMarketTime14);

            // Add the new {@link CryptoCurrency} to the list of cryptoCurrency.
            cryptoCurrency.add(currencyExchange14);

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse15 = new JSONObject(cryptoCurrencyJSON);

            // Extract the JSONObject associated with the key called "RAW"
            JSONObject cryptoCurrencyRaw15 = baseJsonResponse15.getJSONObject("RAW");

            // Extract the JSONObject associated with the key called "ETH"
            JSONObject cryptoCurrencyEth15 = cryptoCurrencyRaw15.getJSONObject("ETH");

            // Extract the JSONObject associated with the key called "LTC"
            JSONObject cryptoCurrencyLtc = cryptoCurrencyEth15.getJSONObject("LTC");

            // Extract the value for the key called "MARKET"
            String cryptoCurrencyMarket15 = cryptoCurrencyLtc.getString("MARKET");

            // Extract the value for the key called "FROMSYMBOL"
            String cryptoCurrencyFromSymbol15A = cryptoCurrencyLtc.getString("FROMSYMBOL");

            // Extract the value for the key called "TOSYMBOL"
            String cryptoCurrencyToSymbol15A = cryptoCurrencyLtc.getString("TOSYMBOL");

            // Extract the value for the key called "PRICE"
            double cryptoCurrencyExchangeRate15 = cryptoCurrencyLtc.getDouble("PRICE");

            // Extract the value for the key called "LASTUPDATE"
            long cryptoCurrencyMarketTime15 = cryptoCurrencyLtc.getLong("LASTUPDATE");

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse15A = new JSONObject(cryptoCurrencyJSON);

            // Extract the JSONObject associated with the key called "DISPLAY"
            JSONObject cryptoCurrencyDisplay15 = baseJsonResponse15A.getJSONObject("DISPLAY");

            // Extract the JSONObject associated with the key called "ETH"
            JSONObject cryptoCurrencyEth15A = cryptoCurrencyDisplay15.getJSONObject("ETH");

            // Extract the JSONObject associated with the key called "LTC"
            JSONObject cryptoCurrencyLtc15A = cryptoCurrencyEth15A.getJSONObject("LTC");

            // Extract the value for the key called "FROMSYMBOL"
            String cryptoCurrencyFromSymbol15 = cryptoCurrencyLtc15A.getString("FROMSYMBOL");

            // Extract the value for the key called "TOSYMBOL"
            String cryptoCurrencyToSymbol15 = cryptoCurrencyLtc15A.getString("TOSYMBOL");

            // Create a new {@link CryptoCurrency} object with cryptoCurrencyMarket15, cryptoCurrencyFromSymbol15A, cryptoCurrencyFromSymbol15,
            // cryptoCurrencyToSymbol15A, cryptoCurrencyToSymbol15, cryptoCurrencyExchangeRate15 and cryptoCurrencyMarketTime15 from the JSON response.
            CryptoCurrency currencyExchange15 = new CryptoCurrency(cryptoCurrencyMarket15, cryptoCurrencyFromSymbol15A, cryptoCurrencyFromSymbol15,
                    cryptoCurrencyToSymbol15A, cryptoCurrencyToSymbol15, cryptoCurrencyExchangeRate15, cryptoCurrencyMarketTime15);

            // Add the new {@link CryptoCurrency} to the list of cryptoCurrency.
            cryptoCurrency.add(currencyExchange15);

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse16 = new JSONObject(cryptoCurrencyJSON);

            // Extract the JSONObject associated with the key called "RAW"
            JSONObject cryptoCurrencyRaw16 = baseJsonResponse16.getJSONObject("RAW");

            // Extract the JSONObject associated with the key called "ETH"
            JSONObject cryptoCurrencyEth16 = cryptoCurrencyRaw16.getJSONObject("ETH");

            // Extract the JSONObject associated with the key called "AUD"
            JSONObject cryptoCurrencyAud = cryptoCurrencyEth16.getJSONObject("AUD");

            // Extract the value for the key called "MARKET"
            String cryptoCurrencyMarket16 = cryptoCurrencyAud.getString("MARKET");

            // Extract the value for the key called "FROMSYMBOL"
            String cryptoCurrencyFromSymbol16A = cryptoCurrencyAud.getString("FROMSYMBOL");

            // Extract the value for the key called "TOSYMBOL"
            String cryptoCurrencyToSymbol16A = cryptoCurrencyAud.getString("TOSYMBOL");

            // Extract the value for the key called "PRICE"
            double cryptoCurrencyExchangeRate16 = cryptoCurrencyAud.getDouble("PRICE");

            // Extract the value for the key called "LASTUPDATE"
            long cryptoCurrencyMarketTime16 = cryptoCurrencyAud.getLong("LASTUPDATE");

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse16A = new JSONObject(cryptoCurrencyJSON);

            // Extract the JSONObject associated with the key called "DISPLAY"
            JSONObject cryptoCurrencyDisplay16 = baseJsonResponse16A.getJSONObject("DISPLAY");

            // Extract the JSONObject associated with the key called "ETH"
            JSONObject cryptoCurrencyEth16A = cryptoCurrencyDisplay16.getJSONObject("ETH");

            // Extract the JSONObject associated with the key called "AUD"
            JSONObject cryptoCurrencyAud16A = cryptoCurrencyEth16A.getJSONObject("AUD");

            // Extract the value for the key called "FROMSYMBOL"
            String cryptoCurrencyFromSymbol16 = cryptoCurrencyAud16A.getString("FROMSYMBOL");

            // Extract the value for the key called "TOSYMBOL"
            String cryptoCurrencyToSymbol16 = cryptoCurrencyAud16A.getString("TOSYMBOL");

            // Create a new {@link CryptoCurrency} object with cryptoCurrencyMarket6, cryptoCurrencyFromSymbol6A, cryptoCurrencyFromSymbol6,
            // cryptoCurrencyToSymbol6A, cryptoCurrencyToSymbol6, cryptoCurrencyExchangeRate6 and cryptoCurrencyMarketTime6 from the JSON response.
            CryptoCurrency currencyExchange16 = new CryptoCurrency(cryptoCurrencyMarket16, cryptoCurrencyFromSymbol16A, cryptoCurrencyFromSymbol16,
                    cryptoCurrencyToSymbol16A, cryptoCurrencyToSymbol16, cryptoCurrencyExchangeRate16, cryptoCurrencyMarketTime16);

            // Add the new {@link CryptoCurrency} to the list of cryptoCurrency.
            cryptoCurrency.add(currencyExchange16);

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse17 = new JSONObject(cryptoCurrencyJSON);

            // Extract the JSONObject associated with the key called "RAW"
            JSONObject cryptoCurrencyRaw17 = baseJsonResponse17.getJSONObject("RAW");

            // Extract the JSONObject associated with the key called "ETH"
            JSONObject cryptoCurrencyEth17 = cryptoCurrencyRaw17.getJSONObject("ETH");

            // Extract the JSONObject associated with the key called "NOK"
            JSONObject cryptoCurrencyNok = cryptoCurrencyEth17.getJSONObject("NOK");

            // Extract the value for the key called "MARKET"
            String cryptoCurrencyMarket17 = cryptoCurrencyNok.getString("MARKET");

            // Extract the value for the key called "FROMSYMBOL"
            String cryptoCurrencyFromSymbol17A = cryptoCurrencyNok.getString("FROMSYMBOL");

            // Extract the value for the key called "TOSYMBOL"
            String cryptoCurrencyToSymbol17A = cryptoCurrencyNok.getString("TOSYMBOL");

            // Extract the value for the key called "PRICE"
            double cryptoCurrencyExchangeRate17 = cryptoCurrencyNok.getDouble("PRICE");

            // Extract the value for the key called "LASTUPDATE"
            long cryptoCurrencyMarketTime17 = cryptoCurrencyNok.getLong("LASTUPDATE");

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse17A = new JSONObject(cryptoCurrencyJSON);

            // Extract the JSONObject associated with the key called "DISPLAY"
            JSONObject cryptoCurrencyDisplay17 = baseJsonResponse17A.getJSONObject("DISPLAY");

            // Extract the JSONObject associated with the key called "ETH"
            JSONObject cryptoCurrencyEth17A = cryptoCurrencyDisplay17.getJSONObject("ETH");

            // Extract the JSONObject associated with the key called "NOK"
            JSONObject cryptoCurrencyNok17A = cryptoCurrencyEth17A.getJSONObject("NOK");

            // Extract the value for the key called "FROMSYMBOL"
            String cryptoCurrencyFromSymbol17 = cryptoCurrencyNok17A.getString("FROMSYMBOL");

            // Extract the value for the key called "TOSYMBOL"
            String cryptoCurrencyToSymbol17 = cryptoCurrencyNok17A.getString("TOSYMBOL");

            // Create a new {@link CryptoCurrency} object with cryptoCurrencyMarket6, cryptoCurrencyFromSymbol6A, cryptoCurrencyFromSymbol6,
            // cryptoCurrencyToSymbol6A, cryptoCurrencyToSymbol6, cryptoCurrencyExchangeRate6 and cryptoCurrencyMarketTime6 from the JSON response.
            CryptoCurrency currencyExchange17 = new CryptoCurrency(cryptoCurrencyMarket17, cryptoCurrencyFromSymbol17A, cryptoCurrencyFromSymbol17,
                    cryptoCurrencyToSymbol17A, cryptoCurrencyToSymbol17, cryptoCurrencyExchangeRate17, cryptoCurrencyMarketTime17);

            // Add the new {@link CryptoCurrency} to the list of cryptoCurrency.
            cryptoCurrency.add(currencyExchange17);

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse18 = new JSONObject(cryptoCurrencyJSON);

            // Extract the JSONObject associated with the key called "RAW"
            JSONObject cryptoCurrencyRaw18 = baseJsonResponse18.getJSONObject("RAW");

            // Extract the JSONObject associated with the key called "ETH"
            JSONObject cryptoCurrencyEth18 = cryptoCurrencyRaw18.getJSONObject("ETH");

            // Extract the JSONObject associated with the key called "MAD"
            JSONObject cryptoCurrencyMad = cryptoCurrencyEth18.getJSONObject("MAD");

            // Extract the value for the key called "MARKET"
            String cryptoCurrencyMarket18 = cryptoCurrencyMad.getString("MARKET");

            // Extract the value for the key called "FROMSYMBOL"
            String cryptoCurrencyFromSymbol18A = cryptoCurrencyMad.getString("FROMSYMBOL");

            // Extract the value for the key called "TOSYMBOL"
            String cryptoCurrencyToSymbol18A = cryptoCurrencyMad.getString("TOSYMBOL");

            // Extract the value for the key called "PRICE"
            double cryptoCurrencyExchangeRate18 = cryptoCurrencyMad.getDouble("PRICE");

            // Extract the value for the key called "LASTUPDATE"
            long cryptoCurrencyMarketTime18 = cryptoCurrencyMad.getLong("LASTUPDATE");

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse18A = new JSONObject(cryptoCurrencyJSON);

            // Extract the JSONObject associated with the key called "DISPLAY"
            JSONObject cryptoCurrencyDisplay18 = baseJsonResponse18A.getJSONObject("DISPLAY");

            // Extract the JSONObject associated with the key called "ETH"
            JSONObject cryptoCurrencyEth18A = cryptoCurrencyDisplay18.getJSONObject("ETH");

            // Extract the JSONObject associated with the key called "MAD"
            JSONObject cryptoCurrencyMad18A = cryptoCurrencyEth18A.getJSONObject("MAD");

            // Extract the value for the key called "FROMSYMBOL"
            String cryptoCurrencyFromSymbol18 = cryptoCurrencyMad18A.getString("FROMSYMBOL");

            // Extract the value for the key called "TOSYMBOL"
            String cryptoCurrencyToSymbol18 = cryptoCurrencyMad18A.getString("TOSYMBOL");

            // Create a new {@link CryptoCurrency} object with cryptoCurrencyMarket6, cryptoCurrencyFromSymbol6A, cryptoCurrencyFromSymbol6,
            // cryptoCurrencyToSymbol6A, cryptoCurrencyToSymbol6, cryptoCurrencyExchangeRate6 and cryptoCurrencyMarketTime6 from the JSON response.
            CryptoCurrency currencyExchange18 = new CryptoCurrency(cryptoCurrencyMarket18, cryptoCurrencyFromSymbol18A, cryptoCurrencyFromSymbol18,
                    cryptoCurrencyToSymbol18A, cryptoCurrencyToSymbol18, cryptoCurrencyExchangeRate18, cryptoCurrencyMarketTime18);

            // Add the new {@link CryptoCurrency} to the list of cryptoCurrency.
            cryptoCurrency.add(currencyExchange18);

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse19 = new JSONObject(cryptoCurrencyJSON);

            // Extract the JSONObject associated with the key called "RAW"
            JSONObject cryptoCurrencyRaw19 = baseJsonResponse19.getJSONObject("RAW");

            // Extract the JSONObject associated with the key called "ETH"
            JSONObject cryptoCurrencyEth19 = cryptoCurrencyRaw19.getJSONObject("ETH");

            // Extract the JSONObject associated with the key called "SGD"
            JSONObject cryptoCurrencySgd = cryptoCurrencyEth19.getJSONObject("SGD");

            // Extract the value for the key called "MARKET"
            String cryptoCurrencyMarket19 = cryptoCurrencySgd.getString("MARKET");

            // Extract the value for the key called "FROMSYMBOL"
            String cryptoCurrencyFromSymbol19A = cryptoCurrencySgd.getString("FROMSYMBOL");

            // Extract the value for the key called "TOSYMBOL"
            String cryptoCurrencyToSymbol19A = cryptoCurrencySgd.getString("TOSYMBOL");

            // Extract the value for the key called "PRICE"
            double cryptoCurrencyExchangeRate19 = cryptoCurrencySgd.getDouble("PRICE");

            // Extract the value for the key called "LASTUPDATE"
            long cryptoCurrencyMarketTime19 = cryptoCurrencySgd.getLong("LASTUPDATE");

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse19A = new JSONObject(cryptoCurrencyJSON);

            // Extract the JSONObject associated with the key called "DISPLAY"
            JSONObject cryptoCurrencyDisplay19 = baseJsonResponse19A.getJSONObject("DISPLAY");

            // Extract the JSONObject associated with the key called "ETH"
            JSONObject cryptoCurrencyEth19A = cryptoCurrencyDisplay19.getJSONObject("ETH");

            // Extract the JSONObject associated with the key called "SGD"
            JSONObject cryptoCurrencySgd19A = cryptoCurrencyEth19A.getJSONObject("SGD");

            // Extract the value for the key called "FROMSYMBOL"
            String cryptoCurrencyFromSymbol19 = cryptoCurrencySgd19A.getString("FROMSYMBOL");

            // Extract the value for the key called "TOSYMBOL"
            String cryptoCurrencyToSymbol19 = cryptoCurrencySgd19A.getString("TOSYMBOL");

            // Create a new {@link CryptoCurrency} object with cryptoCurrencyMarket19, cryptoCurrencyFromSymbol19A, cryptoCurrencyFromSymbol19,
            // cryptoCurrencyToSymbol19A, cryptoCurrencyToSymbol19, cryptoCurrencyExchangeRate19 and cryptoCurrencyMarketTime19 from the JSON response.
            CryptoCurrency currencyExchange19 = new CryptoCurrency(cryptoCurrencyMarket19, cryptoCurrencyFromSymbol19A, cryptoCurrencyFromSymbol19,
                    cryptoCurrencyToSymbol19A, cryptoCurrencyToSymbol19, cryptoCurrencyExchangeRate19, cryptoCurrencyMarketTime19);

            // Add the new {@link CryptoCurrency} to the list of cryptoCurrency.
            cryptoCurrency.add(currencyExchange19);

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            e.printStackTrace();
        }

        // Return the list of cryptoCurrency
        return cryptoCurrency;
    }

}
