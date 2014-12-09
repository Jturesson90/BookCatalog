package com.jesperturesson.booksearch;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

public class JsonParser extends Parser {

    JSONObject root = null;
    String json;

    @Override
    public void parse(InputStream data) {

        try {
            json = ServerConnection.readStream(data);
            root = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("", "Got error when trying to parse json from " + data);
        }
    }

    public JSONObject getRoot() {

        return root;
    }

}
