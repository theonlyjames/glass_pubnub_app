package com.lgsvl.example.glassapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.glass.app.Card;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import java.util.ArrayList;
import java.util.List;

public class DisplayMessageActivity extends Activity {

    private List<Card> mCards;
    private CardScrollView mCardScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Make sure we're running on Honeycomb or higher to use ActionBar APIs
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            //getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        String message = intent.getStringExtra(HomeActivity.EXTRA_MESSAGE);

        createCards(message);

        //mCardScrollView = new TuggableView(this, R.layout.activity_display_message);

        // was: setContentView(R.layout.main_activity);
        //setContentView(new TuggableView(this, R.layout.activity_home));
        //setContentView(mCardScrollView);

        mCardScrollView = new CardScrollView(this);
        CatagoryCardScrollAdapter adapter = new CatagoryCardScrollAdapter();
        mCardScrollView.setAdapter(adapter);
        mCardScrollView.activate();
        setContentView(mCardScrollView);

//        TextView textDisplay = (TextView) findViewById(R.id.textDisplay);
//        textDisplay.setText(message);
    }

    private void createCards(String message) {
        mCards = new ArrayList<Card>();

        Card card;
        // Create new card view
        card = new Card(this);
        card.setText();
        card.setFootnote(message);
        //card.toView();
        mCards.add(card);

        card = new Card(this);
        card.setText("JAMES WAS HERE CARD 2");
        //card.toView();
        mCards.add(card);

        card = new Card(this);
        card.setText("JAMES WAS HERE CARD 3");
        //card.toView();
        mCards.add(card);
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

    private class CatagoryCardScrollAdapter extends CardScrollAdapter {

        @Override
        public int findIdPosition(Object id) {
            return -1;
        }

        @Override
        public int findItemPosition(Object item) {
            return mCards.indexOf(item);
        }

        @Override
        public int getCount() {
            return mCards.size();
        }

        @Override
        public Object getItem(int position) {
            return mCards.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return mCards.get(position).toView();
        }
    }

}
