package com.aware.plugin.emotionsAML_AGH;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aware.utils.IContextCard;

public class ContextCard implements IContextCard {

    //Constructor used to instantiate this card
    public ContextCard() {
    }

    static View card = null;
    static View detail = null;
    static View result = null;
    static View summary = null;

    @Override
    public View getContextCard(Context context) {

        card = LayoutInflater.from(context).inflate(R.layout.card, null);
        detail = card.findViewById(R.id.detail);
        result = card.findViewById(R.id.result);
        summary = card.findViewById(R.id.summary);


        Button clickButton = card.findViewById(R.id.button4);
        clickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (detail.getVisibility() == View.GONE) {
                    card.findViewById(R.id.detail).setVisibility(View.VISIBLE);
                    result.setVisibility(View.GONE);
                    ((TextView) card.findViewById(R.id.hello)).setText("DETAILS");
                } else {
                    card.findViewById(R.id.detail).setVisibility(View.GONE);
                    result.setVisibility(View.VISIBLE);
                    ((TextView) card.findViewById(R.id.hello)).setText("LAST MEASUREMENT");
                }
            }
        });

        Button sumBtn = result.findViewById(R.id.button);
        sumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card.findViewById(R.id.cardLayout).setVisibility(View.GONE);
                result.setVisibility(View.GONE);
                summary.setVisibility(View.VISIBLE);
            }
        });

        Button back = summary.findViewById(R.id.backBtn);
        back.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                summary.setVisibility(View.GONE);
                card.findViewById(R.id.cardLayout).setVisibility(View.VISIBLE);
                result.setVisibility(View.VISIBLE);
            }
        });
        advancedListeners();



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

    public void advancedListeners() {
        summary.findViewById(R.id.angryBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summary.findViewById(R.id.button8).setBackgroundColor(Color.parseColor("#FA8072"));
            }
        });

        summary.findViewById(R.id.contemptBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summary.findViewById(R.id.button8).setBackgroundColor(Color.parseColor("#D8BFD8"));
            }
        });

        summary.findViewById(R.id.disgustBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summary.findViewById(R.id.button8).setBackgroundColor(Color.parseColor("#DEB887"));
            }
        });

        summary.findViewById(R.id.fearBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summary.findViewById(R.id.button8).setBackgroundColor(Color.parseColor("#8FBC8F"));
            }
        });

        summary.findViewById(R.id.happinessBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summary.findViewById(R.id.button8).setBackgroundColor(Color.parseColor("#90EE90"));
            }
        });

        summary.findViewById(R.id.neutralBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summary.findViewById(R.id.button8).setBackgroundColor(Color.parseColor("#B0E0E6"));
            }
        });

        summary.findViewById(R.id.sadnessBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summary.findViewById(R.id.button8).setBackgroundColor(Color.parseColor("#87CEEB"));
            }
        });

        summary.findViewById(R.id.surpriseBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summary.findViewById(R.id.button8).setBackgroundColor(Color.parseColor("#FFDEAD"));
            }
        });

    }

}
