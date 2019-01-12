package com.aware.plugin.emotionsAML_AGH;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.aware.utils.IContextCard;

public class ContextCard implements IContextCard {

    //Constructor used to instantiate this card
    public ContextCard() {
    }

    static View card = null;
    static View detail = null;
    static View result = null;

    @Override
    public View getContextCard(Context context) {

        card = LayoutInflater.from(context).inflate(R.layout.card, null);
        detail = card.findViewById(R.id.detail);
        result = card.findViewById(R.id.result);


        Button clickButton = card.findViewById(R.id.button4);
        clickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (detail.getVisibility()==View.GONE) {
                    card.findViewById(R.id.detail).setVisibility(View.VISIBLE);
                    result.setVisibility(View.GONE);
                } else {
                    card.findViewById(R.id.detail).setVisibility(View.GONE);
                    result.setVisibility(View.VISIBLE);
                }
            }
        });



        //Register the broadcast receiver that will update the UI from the background service (Plugin)
        IntentFilter filter = new IntentFilter("ACCELEROMETER_DATA");
        context.registerReceiver(accelerometerObserver, filter);

        //Return the card to AWARE/apps
        return card;
    }

    //This broadcast receiver is auto-unregistered because it's not static.
    private AccelerometerObserver accelerometerObserver = new AccelerometerObserver();

    public class AccelerometerObserver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("DATA")) {
                /*ContentValues data = intent.getParcelableExtra("data");
                ((TextView) card.findViewById(R.id.file)).setText(data.getAsString(EmotionsVariables.photo));
                ((TextView) card.findViewById(R.id.anger)).setText("Anger: " + data.getAsString(EmotionsVariables.anger));
                ((TextView) card.findViewById(R.id.contempt)).setText("Contempt: " + data.getAsString(EmotionsVariables.contempt));
                ((TextView) card.findViewById(R.id.disgust)).setText("Disgust: " + data.getAsString(EmotionsVariables.disgust));
                ((TextView) card.findViewById(R.id.fear)).setText("Fear: " + data.getAsString(EmotionsVariables.fear));
                ((TextView) card.findViewById(R.id.happiness)).setText("Happiness: " + data.getAsString(EmotionsVariables.happiness));
                ((TextView) card.findViewById(R.id.neutral)).setText("Neutral: " + data.getAsString(EmotionsVariables.neutral));
                ((TextView) card.findViewById(R.id.sadness)).setText("Sadness: " + data.getAsString(EmotionsVariables.sadness));
                ((TextView) card.findViewById(R.id.surprise)).setText("Surprise: " + data.getAsString(EmotionsVariables.surprise));*/
            }
        }
    }

}
