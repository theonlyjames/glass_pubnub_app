package com.lgsvl.example.glassapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.connectsdk.core.LaunchSession;
import com.connectsdk.device.ConnectableDevice;
import com.connectsdk.device.ConnectableDeviceListener;
import com.connectsdk.device.DevicePicker;
import com.connectsdk.discovery.DiscoveryManager;
import com.connectsdk.service.DeviceService;
import com.connectsdk.service.capability.listeners.LaunchListener;
import com.connectsdk.service.command.ServiceCommandError;
import com.google.android.glass.app.Card;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import java.util.ArrayList;
import java.util.List;

public class DisplayMessageActivity extends Activity {

    private List<Card> mCards;
    private CardScrollView mCardScrollView;

    // DISCOVER DEVICE
    DiscoveryManager _discoveryManager;
    Dialog _pickerDialog;
    ConnectableDevice _device;
    TextView _statusTextView;

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

        DiscoveryManager.getInstance(getApplicationContext()).start();

        setupPicker();

        _statusTextView = (TextView) this.findViewById(R.id.statusTextView);
        Button shareImageButton = (Button) this.findViewById(R.id.shareImageButton);

        shareImageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                _pickerDialog.show();
            }
        });

//        TextView textDisplay = (TextView) findViewById(R.id.textDisplay);
//        textDisplay.setText(message);
    }

    private void createCards(String message) {
        mCards = new ArrayList<Card>();

        Card card;
        // Create new card view
        card = new Card(this);
        card.setText("YOU ARE SO AWESOME");
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

    // START OF CONNCECT DEVICE
    private void setupPicker()
    {
        DiscoveryManager.getInstance(getApplicationContext()).registerDefaultDeviceTypes();

        DevicePicker dp = new DevicePicker(this);

        _pickerDialog = dp.getPickerDialog("Device List", new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                DiscoveryManager.getInstance(getApplicationContext()).stop();

                _device = (ConnectableDevice)arg0.getItemAtPosition(arg2);
                _device.addListener(deviceListener);
                _device.connect();
            }
        });
    }

    private ConnectableDeviceListener deviceListener = new com.connectsdk.device.ConnectableDeviceListener() {

        @Override
        public void onPairingRequired(DeviceService.PairingType pairingType) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onDeviceReady() {
            _device.getMediaPlayer().displayImage(
                    "http://www.freesoftwaremagazine.com/files/nodes/3466/fig_sintel_style_study.jpg",
                    "image/png",
                    "Sintel",
                    "Blender Open Movie Project",
                    null,
                    new LaunchListener() {

                        @Override
                        public void onLaunchSuccess(LaunchSession launchSession) {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    _statusTextView.setText("Successfully displayed image!");
                                }
                            });
                        }

                        @Override
                        public void onLaunchFailed(ServiceCommandError error) {
                            final String errorDesc = error.getDesc();

                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    _statusTextView.setText("An error occured while displaying image: " + errorDesc);
                                }
                            });
                        }
                    });
        }

        @Override
        public void onDeviceDisconnected() {
            // TODO Auto-generated method stub

        }

        @Override
        public void onConnectionFailed(ServiceCommandError error) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onCapabilityUpdated(List<String> added, List<String> removed) {
            // TODO Auto-generated method stub

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

}
