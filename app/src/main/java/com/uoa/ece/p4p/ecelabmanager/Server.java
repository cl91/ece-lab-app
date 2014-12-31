package com.uoa.ece.p4p.ecelabmanager;

/**
 * Created by chang on 30/12/14.
 */

import android.util.Log;

import com.uoa.ece.p4p.ecelabmanager.exception.LoginFailed;

import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.net.URL;
import java.net.URLEncoder;

public class Server {
    private static Api api;

    public static void login(String name, String pass) throws LoginFailed {
        try {
            String reply = Api.make_request_no_auth("auth", "name=" +
                    URLEncoder.encode(name, "UTF-8") + "&pass=" +
                    URLEncoder.encode(pass, "UTF-8"));
            JSONObject obj = new JSONObject(reply);
            String auth = obj.getString("auth");
            if (auth == null || auth.isEmpty()) {
                throw new LoginFailed("Not authorised.");
            }
            api = new Api(auth);
        } catch (FileNotFoundException e) {
            throw new LoginFailed("Invalid username or password.");
        } catch (Exception e) {
            throw new LoginFailed(e.getMessage());
        }
    }
}
