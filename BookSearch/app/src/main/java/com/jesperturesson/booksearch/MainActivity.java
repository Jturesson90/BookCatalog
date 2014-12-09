package com.jesperturesson.booksearch;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.transition.Scene;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jesperturesson.booksearch.models.Book;
import com.jesperturesson.booksearch.models.Catalog;
import com.jesperturesson.booksearch.models.Sort;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    ProgressDialog progressDialog;
    Catalog catalog;

    ViewGroup rootContainer;
    Scene detailScene;
    Scene mainScene;
    Scene currentScene;

    Menu menu;
    ListView listView;
    BookAdapter bookAdapter;
    Sort.Method sortedBy = Sort.Method.UNSORTED;

    AlphaAnimation fadeIn;
    AlphaAnimation fadeOut;

    View detailView;
    View mainView;
    boolean fadedMain = false;
    boolean connectionIsFine;
    View selectedTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime));
        fadeOut = new AlphaAnimation(1.0f, 0.0f);
        fadeOut.setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime));

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                if (currentScene.equals(mainScene) && !fadedMain) {
                    keepSelectedTextViewAlpha();
                    mainView.startAnimation(fadeOut);
                    fadedMain = true;
                } else if (currentScene.equals(mainScene) && fadedMain) {
                    enterScene(detailScene);
                    fadedMain = false;
                } else {
                    setAlphaToAllTextView(1.0f);
                    enterScene(mainScene);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        rootContainer = (ViewGroup) findViewById(R.id.root_container);
        detailView = getLayoutInflater().inflate(R.layout.activity_detail_actvity, rootContainer, false);
        mainView = getLayoutInflater().inflate(R.layout.activity_main, rootContainer, false);
        mainScene = new Scene(rootContainer, (ViewGroup) mainView);
        detailScene = new Scene(rootContainer, (ViewGroup) detailView);
        progressDialog = ProgressDialog.show(MainActivity.this, "Please wait ...", "Downloading Books ...", true);
        listView = (ListView) mainView.findViewById(R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book = (Book) parent.getItemAtPosition(position);
                selectedTextView = view;
                setDetailViewInfo(book);
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                connectionIsFine = false;
                ServerConnection connection = new ServerConnection();
                JsonParser parser = new JsonParser();
                if (connection
                        .jsonGet(getString(R.string.url), parser)) {
                    connectionIsFine = true;
                }
                if (connectionIsFine) {
                    catalog = new Catalog(parser.getRoot());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bookAdapter = new BookAdapter(getApplication().getApplicationContext());
                            listView.setAdapter(bookAdapter);
                            for (Book book : catalog.getBooks()) {
                                bookAdapter.add(book);
                            }
                            bookAdapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                            enterScene(mainScene);
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            progressDialog.dismiss();
                            enterScene(mainScene);
                            Toast.makeText(getApplicationContext(), R.string.network_failed, Toast.LENGTH_LONG).show();
                            return;
                        }

                    });
                }
            }
        }).start();
    }

    private void setDetailViewInfo(Book book) {

        TextView detailTitle = (TextView) detailView.findViewById(R.id.detail_title);
        TextView detailAuthor = (TextView) detailView.findViewById(R.id.detail_author);
        TextView detailGenre = (TextView) detailView.findViewById(R.id.detail_genre);
        TextView detailPrice = (TextView) detailView.findViewById(R.id.detail_price);
        TextView detailPublishDate = (TextView) detailView.findViewById(R.id.detail_publish_date);
        TextView detailDescription = (TextView) detailView.findViewById(R.id.detail_description);

        detailTitle.setText(book.getTitle());
        detailAuthor.setText(book.getAuthor());
        detailGenre.setText(book.getGenre());
        detailPrice.setText("Price " + book.getPrice());
        detailPublishDate.setText(book.getPublishDate());
        detailDescription.setText(book.getDescription());

        fadeOutAllBooksExceptSelected();
    }

    private void fadeOutAllBooksExceptSelected() {

        for (int i = 0; i < listView.getChildCount(); i++) {
            if (!isTheSelectedTextView(i)) {
                listView.getChildAt(i).startAnimation(fadeOut);
            }
        }
    }
    private boolean isTheSelectedTextView(int i){
        if(listView.getChildAt(i).equals(selectedTextView)){
            return true;
        }else {
            return false;
        }
    }
    private void keepSelectedTextViewAlpha() {

        for (int i = 0; i < listView.getChildCount(); i++) {
            if (listView.getChildAt(i).equals(selectedTextView)) continue;
            listView.getChildAt(i).setAlpha(0);
        }
    }

    private void setAlphaToAllTextView(float alpha) {

        for (int i = 0; i < listView.getChildCount(); i++) {
            listView.getChildAt(i).setAlpha(alpha);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (shouldSort(id)) {
            sort(id);
            return true;
        }
        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    private boolean shouldSort(int id) {

        if (id == R.id.action_authorSort || id == R.id.action_priceSort || id == R.id.action_titleSort) {
            return true;
        }
        return false;
    }

    private void sort(int id) {

        if (!connectionIsFine) {
            return;
        }
        switch (id) {

            case R.id.action_authorSort:

                if (sortedBy != Sort.Method.AUTHOR) {
                    sortedBy = Sort.Method.AUTHOR;
                    Sort.onAuthor(bookAdapter);

                } else if (sortedBy == Sort.Method.AUTHOR) {
                    sortedBy = Sort.Method.UNSORTED;
                    Sort.onAuthorReversed(bookAdapter);
                }
                bookAdapter.notifyDataSetChanged();
                break;
            case R.id.action_titleSort:

                if (sortedBy != Sort.Method.TITLE) {
                    sortedBy = Sort.Method.TITLE;
                    Sort.onTitle(bookAdapter);
                } else if (sortedBy == Sort.Method.TITLE) {
                    sortedBy = Sort.Method.UNSORTED;
                    Sort.onTitleReversed(bookAdapter);
                }
                bookAdapter.notifyDataSetChanged();
                break;
            case R.id.action_priceSort:

                if (sortedBy != Sort.Method.PRICE) {
                    sortedBy = Sort.Method.PRICE;
                    Sort.onPrice(bookAdapter);
                } else if (sortedBy == Sort.Method.PRICE) {
                    sortedBy = Sort.Method.UNSORTED;
                    Sort.onPriceReversed(bookAdapter);
                }
                bookAdapter.notifyDataSetChanged();
                break;
        }

    }

    @Override
    public void onBackPressed() {

        if (currentScene != null) {
            if (currentScene.equals(detailScene)) {
                detailView.startAnimation(fadeOut);
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    private void enterScene(Scene scene) {

        currentScene = scene;
        scene.enter();
        if (currentScene.equals(mainScene)) {
            mainView.startAnimation(fadeIn);
        } else {
            detailView.startAnimation(fadeIn);
        }
    }
}

class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Context context) {

        super(context, 0);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.book_info_row, null);
        }
        TextView titleText = (TextView) convertView.findViewById(R.id.textview_title);
        TextView authorText = (TextView) convertView.findViewById(R.id.textview_author);
        TextView priceText = (TextView) convertView.findViewById(R.id.textview_price);
        titleText.setText(getItem(position).getTitle());
        authorText.setText(getItem(position).getAuthor());
        priceText.setText("" + getItem(position).getPrice());
        return convertView;
    }

}
