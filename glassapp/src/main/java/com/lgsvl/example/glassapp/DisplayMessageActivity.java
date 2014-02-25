package com.lgsvl.example.glassapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.glass.app.Card;

public class DisplayMessageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Make sure we're running on Honeycomb or higher to use ActionBar APIs
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            //getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        //String message = intent.getStringExtra(HomeActivity.EXTRA_MESSAGE);
        String message = "JAMES TEST STRING";

        // Create the text view
//        TextView textView = new TextView(this);
//        textView.setTextSize(40);
//        textView.setText(message);
//
//        setContentView(textView);

        setContentView(R.layout.activity_display_message);
        // Create new card view
        Card cardView = new Card(this);
        cardView.setText(message);
        cardView.setFootnote(message);
        cardView.toView();

        TextView textDisplay = (TextView) findViewById(R.id.textDisplay);
        textDisplay.setText(message);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
