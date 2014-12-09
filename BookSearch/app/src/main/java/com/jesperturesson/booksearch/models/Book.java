package com.jesperturesson.booksearch.models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jesper on 29/11/14.
 */
public class Book {

    private static final String KEY_ID = "id";
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_TITLE = "title";
    private static final String KEY_GENRE = "genre";
    private static final String KEY_PRICE = "price";
    private static final String KEY_PUBLISH_DATE = "publish_date";
    private static final String KEY_DESCRIPTION = "description";

    private String id;
    private String author;
    private String title;
    private String genre;
    private double price;
    private String publishDate;
    private String description;

    public Book(JSONObject root) {

        this.id = getString(root, KEY_ID);
        this.author = getString(root, KEY_AUTHOR);
        this.title = getString(root, KEY_TITLE);
        this.genre = getString(root, KEY_GENRE);
        this.price = getDouble(root, KEY_PRICE);
        this.publishDate = getString(root, KEY_PUBLISH_DATE);
        this.description = getString(root, KEY_DESCRIPTION);

    }

    private double getDouble(JSONObject root, String key) {

        try {
            return root.getDouble(key);
        } catch (JSONException e) {
            return 0.0;
        }
    }

    private Date getDate(JSONObject root, String key) {

        try {
            String date = root.getString(key);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Log.v("TAG", "" + simpleDateFormat.parse(date).toString());
            return simpleDateFormat.parse(date);
        } catch (JSONException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    private String getString(JSONObject root, String key) {

        try {
            return root.getString(key);
        } catch (JSONException e) {
            return "";
        }
    }

    public String getId() {

        return id;
    }

    public String getAuthor() {

        return author;
    }

    public String getTitle() {

        return title;
    }

    public String getGenre() {

        return genre;
    }

    public double getPrice() {

        return price;
    }

    public String getPublishDate() {

        return publishDate;
    }

    public String getDescription() {

        return description;
    }

    @Override
    public String toString() {

        return "\nBook{\n\t" +
                "\tid='" + id + '\'' +
                ", \n\t\tauthor='" + author + '\'' +
                ", \n\t\ttitle='" + title + '\'' +
                ", \n\t\tgenre='" + genre + '\'' +
                ", \n\t\tprice=" + price +
                ", \n\t\tpublishDate=" + publishDate +
                ", \n\t\tdescription='" + description + '\'' +
                "}\n\n";
    }
}
