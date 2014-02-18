package com.lgsvl.example.glassapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;

import org.json.JSONException;
import org.json.JSONObject;


public class HomeActivity extends Activity {
    public static String TAG = "VL-VoiceLayerGlass";

    Pubnub pubnub = new Pubnub("pub-c-ffcc3163-7fa4-419e-b464-52fcefdd15d9",
         "sub-c-b2d0c1d8-952b-11e3-8d39-02ee2ddab7fe",
         "",
         false);

    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        mGestureDetector = createGestureDetector(this);
 
        try {
          pubnub.subscribe("control_channel", new Callback() {

              @Override
              public void connectCallback(String channel, Object message) {
                  Log.d("PUBNUB","SUBSCRIBE : CONNECT on channel:" + channel
                             + " : " + message.getClass() + " : "
                             + message.toString());
              }

              @Override
              public void disconnectCallback(String channel, Object message) {
                  Log.d("PUBNUB","SUBSCRIBE : DISCONNECT on channel:" + channel
                             + " : " + message.getClass() + " : "
                             + message.toString());
              }

              public void reconnectCallback(String channel, Object message) {
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
                              Log.e(TAG, "Got message " + message);

                      }
                  } catch (Exception e) {
                      Log.e(TAG, "exception in notifyMessage " + e.toString());
                  }

              }

              @Override
              public void errorCallback(String channel, PubnubError error) {
                  Log.d("PUBNUB","SUBSCRIBE : ERROR on channel " + channel
                             + " : " + error.toString());
              }
            }
          );
        } catch (Exception e) {
          Log.d("PUBNUB",e.toString());
        }   

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
                        String message = "{ \"message\" : \"" + "my message" + "\" }";
                        try {
                            JSONObject jso = new JSONObject(message);
                            // Publish message with PubNub
                            //pubnub.publish("Everyone", jso , callback);

                            // JAMES FROM PUB ORIG FILES
                            pubnub.publish("control_channel", jso, new Callback() {
                                @Override
                                public void successCallback(String channel, Object message) {
                                    //int i = Log.i(String.format("PUBLISH : %s", message));
                                }
                                @Override
                                public void errorCallback(String channel, PubnubError error) {
                                    //notifyUser("PUBLISH : " + error);
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
