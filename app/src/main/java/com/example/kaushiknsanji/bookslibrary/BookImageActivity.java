package com.example.kaushiknsanji.bookslibrary;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.kaushiknsanji.bookslibrary.workers.ImageDownloaderFragment;

/**
 * Activity class that displays the Image of the {@link com.example.kaushiknsanji.bookslibrary.models.BookInfo}
 * Book item being viewed in the {@link BookDetailActivity}
 *
 * @author Kaushik N Sanji
 */
public class BookImageActivity extends AppCompatActivity {

    //Bundle Key used for grabbing the Intent's data
    public static final String BOOK_INFO_ITEM_IMAGE_STR_KEY = "BookInfo.Item.Image.Data";
    //Constant used for Logs
    private static final String LOG_TAG = BookImageActivity.class.getSimpleName();

    //Method invoked by the system to create and setup the layout 'R.layout.activity_book_image'
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_image);

        //Displaying the Up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Finding the ImageView
        ImageView bookImageView = (ImageView) findViewById(R.id.book_image_id);

        //Loading the Image to be shown from the Intent received
        Intent bookImageIntent = getIntent();
        String imageLinkForBookImageInfo = bookImageIntent.getStringExtra(BOOK_INFO_ITEM_IMAGE_STR_KEY);
        if (!TextUtils.isEmpty(imageLinkForBookImageInfo)) {
            //Loading the Image when link is present
            ImageDownloaderFragment
                    .newInstance(getSupportFragmentManager(), bookImageView.getId())
                    .executeAndUpdate(bookImageView,
                            imageLinkForBookImageInfo,
                            bookImageView.getId());
        } else {
            //Resetting to the default Book image when link is absent
            //(This case can only occur if there is a problem with network connectivity)
            bookImageView.setImageResource(R.drawable.ic_book);
        }

    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Executing based on MenuItem's Id
        switch (item.getItemId()) {
            case android.R.id.home:
                //Handling the action bar's home/up button
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
