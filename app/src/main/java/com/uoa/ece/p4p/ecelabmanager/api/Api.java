package com.uoa.ece.p4p.ecelabmanager.api;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by chang on 30/12/14.
 */
public class Api {
    final private static String server_addr = "http://engsspdev01.foe.auckland.ac.nz:3000/";

    private String auth;

    public Api(String auth) {
        this.auth = auth;
    }

    public static String make_request_no_auth_with_body(String api, String query,
                                                        String body) throws IOException {
        URL url = new URL(server_addr + "api/" +
                ((query == null || query.isEmpty()) ? api : api+"?"+ query));
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoOutput(true);
        con.setChunkedStreamingMode(0);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(body);
        wr.flush();
        wr.close();

        InputStream in = new BufferedInputStream(con.getInputStream());
        BufferedReader r = new BufferedReader(new InputStreamReader(in));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line);
        }
        con.disconnect();
        return total.toString();
    }

    public static String make_request_no_auth(String api, String query) throws IOException {
        return make_request_no_auth_with_body(api, query, "");
    }

    public String make_request(String api, String query) throws IOException {
        return make_request_no_auth(api, "auth=" + auth + "&" + query);
    }

    public String make_request(String api) throws IOException {
        return make_request_no_auth(api, "auth=" + auth);
    }

    public String make_request_with_body(String api, String query, String body) throws IOException {
        return make_request_no_auth_with_body(api, "auth=" + auth + "&" + query, body);
    }
}
