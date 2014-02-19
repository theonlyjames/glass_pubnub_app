package com.lgsvl.example.glassapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;
import android.widget.EditText;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;


public class HomeActivity extends Activity {
    public static String TAG = "VL-VoiceLayerGlass";

    Pubnub pubnub = new Pubnub("pub-c-ffcc3163-7fa4-419e-b464-52fcefdd15d9", "sub-c-b2d0c1d8-952b-11e3-8d39-02ee2ddab7fe", "", false);

    //public String channel = "control_channel";

    private GestureDetector mGestureDetector;

    // ALL FROM PUBANO
    private void notifyUser(Object message) {
        try {
            if (message instanceof JSONObject) {
                final JSONObject obj = (JSONObject) message;
                this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), obj.toString(),
                                Toast.LENGTH_LONG).show();

                        Log.i("Received msg : ", String.valueOf(obj));
                    }
                });

            } else if (message instanceof String) {
                final String obj = (String) message;
                this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), obj,
                                Toast.LENGTH_LONG).show();
                        Log.i("Received msg : ", obj.toString());
                    }
                });

            } else if (message instanceof JSONArray) {
                final JSONArray obj = (JSONArray) message;
                this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), obj.toString(),
                                Toast.LENGTH_LONG).show();
                        Log.i("Received msg : ", obj.toString());
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // new from PUBANO
        setContentView(R.layout.usage);
        this.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent intent) {
                pubnub.disconnectAndResubscribe();

            }

        }, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        // end

        setContentView(R.layout.activity_home);

        mGestureDetector = createGestureDetector(this);

        Hashtable args = new Hashtable(1);

        args.put("channel", "control_channel");
 
        try {
          pubnub.subscribe(args, new Callback() {

              @Override
              public void connectCallback(String channel, Object message) {
                  notifyUser("PUBLISH : " + message);
                  Log.d("PUBNUB","SUBSCRIBE : CONNECT on channel:" + channel
                             + " : " + message.getClass() + " : "
                             + message.toString());
              }

              @Override
              public void disconnectCallback(String channel, Object message) {
                  notifyUser("PUBLISH : " + message);
                  Log.d("PUBNUB","SUBSCRIBE : DISCONNECT on channel:" + channel
                             + " : " + message.getClass() + " : "
                             + message.toString());
              }

              public void reconnectCallback(String channel, Object message) {
                  notifyUser("PUBLISH : " + message);
                  Log.d("PUBNUB","SUBSCRIBE : RECONNECT on channel:" + channel
                             + " : " + message.getClass() + " : "
                             + message.toString());
              }

              @Override
              public void successCallback(String channel, Object message) {
                  try {
                      if (message instanceof JSONObject) {
                          JSONObject jso = (JSONObject)message;
                              //final String message = jso.getString("message");
                              notifyUser("PUBLISH : " + jso);
                              Log.e(TAG, "Got message " + message);

                      }
                  } catch (Exception e) {
                      notifyUser("PUBLISH : " + e.toString());
                      Log.e(TAG, "exception in notifyMessage " + e.toString());
                  }

              }

              @Override
              public void errorCallback(String channel, PubnubError error) {
                  notifyUser("PUBLISH : " + error);
                  Log.d("PUBNUB","SUBSCRIBE : ERROR on channel " + channel
                             + " : " + error.toString());
              }
            }
          );
        } catch (Exception e) {
          Log.d("PUBNUB",e.toString());
        }   

    }

    private void _publish(final String channel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Publish");
        builder.setMessage("Enter message");
        final EditText etMessage = new EditText(this);
        builder.setView(etMessage);
        builder.setPositiveButton("Publish",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Hashtable args = new Hashtable(2);

                        String message = etMessage.getText().toString();

                        if (args.get("message") == null) {
                            try {
                                Integer i = Integer.parseInt(message);
                                args.put("message", i);
                            } catch (Exception e) {

                            }
                        }
                        if (args.get("message") == null) {
                            try {
                                Double d = Double.parseDouble(message);
                                args.put("message", d);
                            } catch (Exception e) {

                            }
                        }
                        if (args.get("message") == null) {
                            try {
                                JSONArray js = new JSONArray(message);
                                args.put("message", js);
                            } catch (Exception e) {

                            }
                        }
                        if (args.get("message") == null) {
                            try {
                                JSONObject js = new JSONObject(message);
                                args.put("message", js);
                            } catch (Exception e) {

                            }
                        }
                        if (args.get("message") == null) {
                            args.put("message", message);
                        }

                        // Publish Message

                        args.put("channel", channel); // Channel Name

                        pubnub.publish(args, new Callback() {
                            @Override
                            public void successCallback(String channel,
                                                        Object message) {
                                notifyUser("PUBLISH : " + message);
                            }
                            @Override
                            public void errorCallback(String channel,
                                                      PubnubError error) {
                                notifyUser("PUBLISH : " + error);
                            }
                        });
                    }

                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void publish() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Publish ");
        builder.setMessage("Enter channel name");
        final EditText etChannel = new EditText(this);
        builder.setView(etChannel);
        builder.setPositiveButton("Done",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        _publish(etChannel.getText().toString());
                    }

                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void presence() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Presence");
        builder.setMessage("Enter channel name");
        final EditText input = new EditText(this);
        builder.setView(input);
        builder.setPositiveButton("Subscribe For Presence",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String channel = input.getText().toString();

                        try {
                            pubnub.presence(channel, new Callback() {
                                @Override
                                public void successCallback(String channel,
                                                            Object message) {
                                    notifyUser("PRESENCE : " + channel + " : "
                                            + message.getClass() + " : "
                                            + message.toString());
                                }
                                @Override
                                public void errorCallback(String channel,
                                                          PubnubError error) {
                                    notifyUser("PRESENCE : ERROR on channel "
                                            + channel + " : "
                                            + error.toString());
                                }
                            });

                        } catch (Exception e) {

                        }
                    }

                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    private GestureDetector createGestureDetector(Context context)
    {
        GestureDetector gestureDetector = new GestureDetector(context);

        gestureDetector.setBaseListener(new GestureDetector.BaseListener()
        {
            @Override
            public boolean onGesture(Gesture gesture)
            {
            switch (gesture)
            {
                case TAP:
                        // compose JSON message
                        String message = "{ \"message\" : \"" + "my message JAMES WAS HERE DROID" + "\" }";
                        //JSONObject obj = (JSONObject) message;
                        String channel = "control_channel";
                        try {
                            JSONObject jso = new JSONObject(message);
                            // Publish message with PubNub
                            //pubnub.publish("Everyone", jso , callback);

                            // JAMES FROM PUB ORIG FILES
                            Hashtable args = new Hashtable(2);

                            args.put("message", jso);
                            args.put("message", message);

                            pubnub.publish(channel, jso, new Callback() {
                                @Override
                                public void successCallback(String channel, Object message) {
                                    notifyUser("PUBLISH : " + message);
                                }
                                @Override
                                public void errorCallback(String channel, PubnubError error) {
                                    notifyUser("PUBLISH : " + error);
                                }
                            });

                        } catch (JSONException e) {
                            Log.e(TAG,  "JSON exception " + e.toString());
                        }

                    break;
                case TWO_TAP:
                default:
                    break;
            }

            return true;
            }
        });

        return gestureDetector;
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event)
    {
        if (mGestureDetector != null)
        {
            return mGestureDetector.onMotionEvent(event);
        }

        return super.onGenericMotionEvent(event);
    }

}
