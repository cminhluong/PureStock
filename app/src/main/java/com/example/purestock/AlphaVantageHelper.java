package com.example.purestock;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;


/**
 * Connection to Alpha Vantage API.
 * @see
 * ApiConnector

 */


public class AlphaVantageHelper {
    private static final String BASE_URL = "https://www.alphavantage.co/query?";
    private String apiKey;
    private int timeOut;
    private String apiParams[];
    private int apiNumParams;

    /**
     * Creates an AlphaVantageConnector.
     *
     * @param apiKey the secret key to access the api.
     * @param timeOut the timeout for when reading the connection should give up.
     */
    public AlphaVantageHelper(String apiKey, int numParams, int timeOut) {
        this.apiKey = apiKey;
        this.timeOut = timeOut;
        apiParams = new String[3];

        apiParams[0] = "";
        apiParams[1] = "";
        apiParams[2] = "";
        apiNumParams = numParams;
    }

    public void setParameter(int paramIndex, String value) {
        apiParams[paramIndex] = value;
    }

    public void setNumerParameters(int numParams)
    {
        apiNumParams = numParams;
    }

    public String submitRequest()
    {
        String tmp = "";

        for(int i=0; i<apiNumParams; i++)
            tmp += apiParams[i] + "&";

        try {
            URL url = new URL(BASE_URL + tmp + "apikey=" + apiKey);
            URLConnection urlConn = url.openConnection();
            BufferedReader bReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            StringBuilder sBuilder = new StringBuilder();

            String line = null;
            while ((line = bReader.readLine()) != null) {
                sBuilder.append(line + "\n");
            }

            return sBuilder.toString();

        } catch (Exception e) {
            Log.e("String & BufferedReader", "Error converting result " + e.toString());
        }

        return null;
    }
}
