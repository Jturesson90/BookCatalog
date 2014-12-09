package com.jesperturesson.booksearch.models;

import android.widget.ArrayAdapter;

import java.util.Comparator;

/**
 * Created by jesper on 29/11/14.
 */
public class Sort {

    public static enum Method {
        PRICE, TITLE, AUTHOR, DATE, UNSORTED
    }

    public static void onPrice(ArrayAdapter arrayAdapter) {

        arrayAdapter.sort(new Comparator<Book>() {
            @Override
            public int compare(Book book1, Book book2) {

                if (book1.getPrice() < book2.getPrice()) {
                    return -1;
                } else if (book1.getPrice() > book2.getPrice()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
    }

    public static void onPriceReversed(ArrayAdapter arrayAdapter) {

        arrayAdapter.sort(new Comparator<Book>() {
            @Override
            public int compare(Book book1, Book book2) {

                if (book1.getPrice() > book2.getPrice()) {
                    return -1;
                } else if (book1.getPrice() < book2.getPrice()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
    }

    public static void onTitle(ArrayAdapter arrayAdapter) {

        arrayAdapter.sort(new Comparator<Book>() {
            @Override
            public int compare(Book book1, Book book2) {

                return book1.getTitle().compareTo(book2.getTitle());
            }
        });
    }

    public static void onTitleReversed(ArrayAdapter arrayAdapter) {

        arrayAdapter.sort(new Comparator<Book>() {
            @Override
            public int compare(Book book1, Book book2) {

                return book2.getTitle().compareTo(book1.getTitle());
            }
        });
    }

    public static void onAuthor(ArrayAdapter arrayAdapter) {

        arrayAdapter.sort(new Comparator<Book>() {
            @Override
            public int compare(Book book1, Book book2) {

                return book1.getAuthor().compareTo(book2.getAuthor());
            }
        });
    }

    public static void onAuthorReversed(ArrayAdapter arrayAdapter) {

        arrayAdapter.sort(new Comparator<Book>() {
            @Override
            public int compare(Book book1, Book book2) {

                return book2.getAuthor().compareTo(book1.getAuthor());
            }
        });
    }

}
