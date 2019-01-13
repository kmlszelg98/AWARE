package com.aware.plugin.emotionsAML_AGH;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aware.plugin.emotionsAML_AGH.results.EmotionsVariables;
import com.aware.utils.IContextCard;

public class ContextCard implements IContextCard {

    //Constructor used to instantiate this card
    public ContextCard() {
    }

    static View card = null;
    static View detail = null;
    static View result = null;
    static View summary = null;
    static ContentValues contentValues = new ContentValues();

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
                ((Button) summary.findViewById(R.id.button8)).setText(EmotionsVariables.neutral + " "
                        + contentValues.getAsInteger(EmotionsVariables.neutral + "_SUM"));
            }
        });

        Button back = summary.findViewById(R.id.backBtn);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                summary.setVisibility(View.GONE);
                card.findViewById(R.id.cardLayout).setVisibility(View.VISIBLE);
                result.setVisibility(View.VISIBLE);
            }
        });
        advancedListeners();


        //Register the broadcast receiver that will update the UI from the background service (Plugin)
        IntentFilter filter = new IntentFilter("DATA");
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
                ContentValues data = intent.getParcelableExtra("data");
                contentValues = new ContentValues(data);
                updateBasedOnMeasure(data);
            }
        }
    }

    public void advancedListeners() {
        summary.findViewById(R.id.angryBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summary.findViewById(R.id.button8).setBackgroundColor(Color.parseColor("#FA8072"));
                ((Button) summary.findViewById(R.id.button8)).setText(EmotionsVariables.anger+" "
                        +contentValues.getAsInteger(EmotionsVariables.anger+"_SUM"));
            }
        });

        summary.findViewById(R.id.contemptBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summary.findViewById(R.id.button8).setBackgroundColor(Color.parseColor("#D8BFD8"));
                ((Button) summary.findViewById(R.id.button8)).setText(EmotionsVariables.contempt+" "
                        +contentValues.getAsInteger(EmotionsVariables.contempt+"_SUM"));
            }
        });

        summary.findViewById(R.id.disgustBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summary.findViewById(R.id.button8).setBackgroundColor(Color.parseColor("#DEB887"));
                ((Button) summary.findViewById(R.id.button8)).setText(EmotionsVariables.disgust+" "
                        +contentValues.getAsInteger(EmotionsVariables.disgust+"_SUM"));
            }
        });

        summary.findViewById(R.id.fearBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summary.findViewById(R.id.button8).setBackgroundColor(Color.parseColor("#8FBC8F"));
                ((Button) summary.findViewById(R.id.button8)).setText(EmotionsVariables.fear+" "
                        +contentValues.getAsInteger(EmotionsVariables.fear+"_SUM"));
            }
        });

        summary.findViewById(R.id.happinessBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summary.findViewById(R.id.button8).setBackgroundColor(Color.parseColor("#90EE90"));
                ((Button) summary.findViewById(R.id.button8)).setText(EmotionsVariables.happiness+" "
                        +contentValues.getAsInteger(EmotionsVariables.happiness+"_SUM"));
            }
        });

        summary.findViewById(R.id.neutralBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summary.findViewById(R.id.button8).setBackgroundColor(Color.parseColor("#B0E0E6"));
                ((Button) summary.findViewById(R.id.button8)).setText(EmotionsVariables.neutral+" "
                        +contentValues.getAsInteger(EmotionsVariables.neutral+"_SUM"));
            }
        });

        summary.findViewById(R.id.sadnessBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summary.findViewById(R.id.button8).setBackgroundColor(Color.parseColor("#87CEEB"));
                ((Button) summary.findViewById(R.id.button8)).setText(EmotionsVariables.sadness+" "
                        +contentValues.getAsInteger(EmotionsVariables.sadness+"_SUM"));
            }
        });

        summary.findViewById(R.id.surpriseBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summary.findViewById(R.id.button8).setBackgroundColor(Color.parseColor("#FFDEAD"));
                ((Button) summary.findViewById(R.id.button8)).setText(EmotionsVariables.surprise+" "
                        +contentValues.getAsInteger(EmotionsVariables.surprise+"_SUM"));
            }
        });

    }

    public void updateBasedOnMeasure(ContentValues contentValues) {
        setImageColor(contentValues.getAsString("EMOTION"));
        updateDetails(contentValues);
    }

    public void updateDetails(ContentValues data) {
        ((TextView) detail.findViewById(R.id.angryBtn)).setText("Anger \n " + data.getAsString(EmotionsVariables.anger));
        ((TextView) detail.findViewById(R.id.contemptBtn)).setText("Contempt \n " + data.getAsString(EmotionsVariables.contempt));
        ((TextView) detail.findViewById(R.id.disgustBtn)).setText("Disgust \n " + data.getAsString(EmotionsVariables.disgust));
        ((TextView) detail.findViewById(R.id.fearBtn)).setText("Fear \n " + data.getAsString(EmotionsVariables.fear));
        ((TextView) detail.findViewById(R.id.happinessBtn)).setText("Happiness \n " + data.getAsString(EmotionsVariables.happiness));
        ((TextView) detail.findViewById(R.id.neutralBtn)).setText("Neutral \n " + data.getAsString(EmotionsVariables.neutral));
        ((TextView) detail.findViewById(R.id.sadnessBtn)).setText("Sadness \n " + data.getAsString(EmotionsVariables.sadness));
        ((TextView) detail.findViewById(R.id.surpriseBtn)).setText("Surprise \n " + data.getAsString(EmotionsVariables.surprise));
    }

    public void setImageColor(String key) {
        if (key.equals(EmotionsVariables.anger)) {
            result.findViewById(R.id.button2).setBackgroundColor(Color.parseColor("#FA8072"));
            ((Button) result.findViewById(R.id.button2)).setText(EmotionsVariables.anger);
        } else if (key.equals(EmotionsVariables.contempt)) {
            result.findViewById(R.id.button2).setBackgroundColor(Color.parseColor("#D8BFD8"));
            ((Button) result.findViewById(R.id.button2)).setText(EmotionsVariables.contempt);
        } else if (key.equals(EmotionsVariables.disgust)) {
            result.findViewById(R.id.button2).setBackgroundColor(Color.parseColor("#DEB887"));
            ((Button) result.findViewById(R.id.button2)).setText(EmotionsVariables.disgust);
        } else if (key.equals(EmotionsVariables.fear)) {
            result.findViewById(R.id.button2).setBackgroundColor(Color.parseColor("#8FBC8F"));
            ((Button) result.findViewById(R.id.button2)).setText(EmotionsVariables.fear);
        } else if (key.equals(EmotionsVariables.happiness)) {
            result.findViewById(R.id.button2).setBackgroundColor(Color.parseColor("#90EE90"));
            ((Button) result.findViewById(R.id.button2)).setText(EmotionsVariables.happiness);
        } else if (key.equals(EmotionsVariables.neutral)) {
            result.findViewById(R.id.button2).setBackgroundColor(Color.parseColor("#B0E0E6"));
            ((Button) result.findViewById(R.id.button2)).setText(EmotionsVariables.neutral);
        } else if (key.equals(EmotionsVariables.sadness)) {
            result.findViewById(R.id.button2).setBackgroundColor(Color.parseColor("#87CEEB"));
            ((Button) result.findViewById(R.id.button2)).setText(EmotionsVariables.sadness);
        } else {
            result.findViewById(R.id.button2).setBackgroundColor(Color.parseColor("#FFDEAD"));
            ((Button) result.findViewById(R.id.button2)).setText(EmotionsVariables.surprise);
        }

    }

}
