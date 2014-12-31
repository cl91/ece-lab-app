package com.uoa.ece.p4p.ecelabmanager;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by chang on 30/12/14.
 */
public class Api {
    final private static String server_addr = "http://192.168.20.9:3000/";

    private String auth;

    public Api(String auth) {
        this.auth = auth;
    }

    public static String make_request_no_auth(String api, String query) throws IOException {
        URL url = new URL(server_addr + "api/" +
                ((query == null || query.isEmpty()) ? api : api+"?"+ query));
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoOutput(true);
        try {
            con.getInputStream();   // whoever designed HttpURLConnection (stupid name!) must be fucking brain damaged
        } catch (IOException e) {
            // do nothing. see? this is fucking stupid.
        }
        InputStream in = new BufferedInputStream(con.getInputStream());
        BufferedReader r = new BufferedReader(new InputStreamReader(in));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line);
        }
        return total.toString();
    }

    public String make_request(String api, String query) throws IOException {
        return make_request_no_auth(api, "auth="+auth+"&"+query);
    }
}
