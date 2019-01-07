package com.aware.plugin.emotionsAML_AGH;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aware.plugin.emotionsAML_AGH.results.EmotionsVariables;
import com.aware.plugin.emotionsAML_AGH.results.MSFaceApi;
import com.aware.utils.IContextCard;

public class ContextCard implements IContextCard {

    //Constructor used to instantiate this card
    public ContextCard() {
    }

    private static MSFaceApi faceApi = new MSFaceApi();

    static View card = null;

    @Override
    public View getContextCard(Context context) {
        //Load card layout
        card = LayoutInflater.from(context).inflate(R.layout.card, null);

        Button clickButton = (Button) card.findViewById(R.id.button);
        clickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Button) card.findViewById(R.id.button)).setText("BACK");
            }
        });

        //Register the broadcast receiver that will update the UI from the background service (Plugin)
        IntentFilter filter = new IntentFilter("ACCELEROMETER_DATA");
        context.registerReceiver(accelerometerObserver, filter);

        IntentFilter filter2 = new IntentFilter("LOCATIONS_DATA");
        context.registerReceiver(locatonObserver, filter2);

        //Return the card to AWARE/apps
        return card;
    }

    //This broadcast receiver is auto-unregistered because it's not static.
    private AccelerometerObserver accelerometerObserver = new AccelerometerObserver();

    public class AccelerometerObserver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("ACCELEROMETER_DATA")) {
                ContentValues data = intent.getParcelableExtra("data");
                ((TextView) card.findViewById(R.id.file)).setText(data.getAsString(EmotionsVariables.photo));
                ((TextView) card.findViewById(R.id.anger)).setText("Anger: " + data.getAsString(EmotionsVariables.anger));
                ((TextView) card.findViewById(R.id.contempt)).setText("Contempt: " + data.getAsString(EmotionsVariables.contempt));
                ((TextView) card.findViewById(R.id.disgust)).setText("Disgust: " + data.getAsString(EmotionsVariables.disgust));
                ((TextView) card.findViewById(R.id.fear)).setText("Fear: " + data.getAsString(EmotionsVariables.fear));
                ((TextView) card.findViewById(R.id.happiness)).setText("Happiness: " + data.getAsString(EmotionsVariables.happiness));
                ((TextView) card.findViewById(R.id.neutral)).setText("Neutral: " + data.getAsString(EmotionsVariables.neutral));
                ((TextView) card.findViewById(R.id.sadness)).setText("Sadness: " + data.getAsString(EmotionsVariables.sadness));
                ((TextView) card.findViewById(R.id.surprise)).setText("Surprise: " + data.getAsString(EmotionsVariables.surprise));
            }
        }
    }

    /*TODO In future check what with localization*/
    private LocationObserver locatonObserver = new LocationObserver();

    public class LocationObserver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("LOCATIONS_DATA")) {
                ContentValues data = intent.getParcelableExtra("data");
            }
        }
    }

    private static void updateLayout() {
        TextView textView = card.findViewById(R.id.file);
        Log.d("Update", "-----------------------------------------");
        //((TextView) card.findViewById(R.id.hello)).setText("ACCELEROMETER_DATA " + c + EmotionsVariables.face.getPhoto());
        //c++;

        /*Face face = EmotionsVariables.face;
        textView.setText(face.getPhoto());
        ((TextView) card.findViewById(R.id.anger)).setText("Anger: " + face.getAnger());
        ((TextView) card.findViewById(R.id.contempt)).setText("Contempt: " + face.getContempt());
        ((TextView) card.findViewById(R.id.disgust)).setText("Disgust: " + face.getDisgust());
        ((TextView) card.findViewById(R.id.fear)).setText("Fear: " + face.getFear());
        ((TextView) card.findViewById(R.id.happiness)).setText("Happiness: " + face.getHappiness());
        ((TextView) card.findViewById(R.id.neutral)).setText("Neutral: " + face.getNeutral());
        ((TextView) card.findViewById(R.id.sadness)).setText("Sadness: " + face.getSadness());
        ((TextView) card.findViewById(R.id.surprise)).setText("Surprise: " + face.getSurprise());*/


    }


}
