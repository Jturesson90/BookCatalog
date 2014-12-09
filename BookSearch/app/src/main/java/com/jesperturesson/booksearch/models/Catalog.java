package com.jesperturesson.booksearch.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jesper on 29/11/14.
 */
public class Catalog {

    private static final String TAG = "CATALOG";
    private static final String KEY_CATALOG = "catalog";
    private static final String KEY_BOOK = "book";

    private JSONObject catalog;

    private JSONArray bookJson;
    private ArrayList<Book> books;

    public Catalog(JSONObject root) {

        this.books = new ArrayList<Book>();
        this.catalog = getJsonObject(root, KEY_CATALOG);
        if (this.catalog != null) {
            this.bookJson = getJsonArray(this.catalog, KEY_BOOK);
        }
        if (this.bookJson != null) {
            this.books = getBooks(this.bookJson);
        } else {
            Log.e(TAG, "Null was found");
        }
    }

    private ArrayList<Book> getBooks(JSONArray bookJson) {

        ArrayList<Book> tempBooks = new ArrayList<Book>();
        if (bookJson != null) {
            for (int i = 0; i < bookJson.length(); i++) {
                Book book = null;
                try {
                    book = new Book(bookJson.getJSONObject(i));
                    tempBooks.add(book);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.v("MyBook", "new book!: " + book.getTitle());

            }
        }
        return tempBooks;
    }

    private JSONObject getJsonObject(JSONObject root, String key) {

        try {
            return root.getJSONObject(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private JSONArray getJsonArray(JSONObject root, String key) {

        try {
            return root.getJSONArray(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Book> getBooks() {

        return books;
    }

    @Override
    public String toString() {

        String string = "";
        string += "\nCatalog{\n\tbooks = ";
        for (int i = 0; i < bookJson.length(); i++) {
            string += books.get(i).toString();
        }
        string += "}";
        return string;
    }
}
