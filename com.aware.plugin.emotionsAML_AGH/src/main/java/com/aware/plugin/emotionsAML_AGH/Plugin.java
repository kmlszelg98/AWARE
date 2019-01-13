package com.aware.plugin.emotionsAML_AGH;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.util.Log;

import com.aware.Accelerometer;
import com.aware.Aware;
import com.aware.Aware_Preferences;
import com.aware.Screen;
import com.aware.plugin.emotionsAML_AGH.database.DBManager;
import com.aware.plugin.emotionsAML_AGH.results.EmotionsVariables;
import com.aware.plugin.emotionsAML_AGH.results.MSFaceApi;
import com.aware.utils.Aware_Plugin;

import org.json.JSONException;

import java.io.IOException;

public class Plugin extends Aware_Plugin {

    DirectoryFileObserver directoryFileObserver;
    private static DBManager dbManager;
    private static ContentValues copy = new ContentValues();
    private static ContentValues currentContent = new ContentValues();

    @Override
    public void onCreate() {
        super.onCreate();

        //This allows plugin data to be synced on demand from broadcast Aware#ACTION_AWARE_SYNC_DATA
        AUTHORITY = Provider.getAuthority(this);

        TAG = "AWARE::" + getResources().getString(R.string.app_name);

        /**
         * Plugins share their current status, i.e., context using this method.
         * This method is called automatically when triggering
         * {@link Aware#ACTION_AWARE_CURRENT_CONTEXT}
         **/
        CONTEXT_PRODUCER = new ContextProducer() {
            @Override
            public void onContext() {
                //Broadcast your context here
            }
        };

        directoryFileObserver = new DirectoryFileObserver(getGalleryPath());
        directoryFileObserver.startWatching();

        dbManager = new DBManager(this);
        dbManager.open();

        currentContent = dbManager.get();
        System.out.println("CURRENT VALUE");
        System.out.println(currentContent);
        copy = new ContentValues(currentContent);


        //Add permissions you need (Android M+).
        //By default, AWARE asks access to the #Manifest.permission.WRITE_EXTERNAL_STORAGE

        //REQUIRED_PERMISSIONS.add(Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    private static String getGalleryPath() {
        return Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DCIM + "/Camera/";
    }

    public static class DirectoryFileObserver extends FileObserver {
        String aboslutePath = "path to your directory";

        public DirectoryFileObserver(String path) {
            super(path, FileObserver.CREATE);
            aboslutePath = path;
        }

        @Override
        public void onEvent(int event, String path) {
            if (path != null) {
                Log.e("FileObserver: ", "File Created");
                Log.e("FileObserver: ", path);
                try {
                    MSFaceApi faceApi = new MSFaceApi();
                    currentContent = faceApi.detect(aboslutePath+path);
                    insert();
                    updateContent(currentContent.getAsString("EMOTION"));
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Allow callback to other applications when data is stored in provider
     */
    private static AWARESensorObserver awareSensor;

    public static void setSensorObserver(AWARESensorObserver observer) {
        awareSensor = observer;
    }

    public static AWARESensorObserver getSensorObserver() {
        return awareSensor;
    }

    public interface AWARESensorObserver {
        void onDataChanged(ContentValues data);
    }

    //This function gets called every 5 minutes by AWARE to make sure this plugin is still running.
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        if (PERMISSIONS_OK) {

            DEBUG = Aware.getSetting(this, Aware_Preferences.DEBUG_FLAG).equals("true");

            //Initialize our plugin's settings
            Aware.setSetting(this, Settings.STATUS_PLUGIN_TEMPLATE, true);

            /**
             * Example of how to enable accelerometer sensing and how to access the data in real-time for your app.
             * In this particular case, we are sending a broadcast that the ContextCard listens to and updates the UI in real-time.
             */
            Aware.startAccelerometer(this);
            Accelerometer.setSensorObserver(new Accelerometer.AWARESensorObserver() {
                @Override
                public void onAccelerometerChanged(ContentValues contentValues) {
                    sendBroadcast(new Intent("DATA").putExtra("data", currentContent));
                }
            });

            Aware.startScreen(this);
            Screen.setSensorObserver(new Screen.AWARESensorObserver() {
                @Override
                public void onScreenOn() {

                }

                @Override
                public void onScreenOff() {

                }

                @Override
                public void onScreenLocked() {

                }

                @Override
                public void onScreenUnlocked() {

                }
            });

            //Enable our plugin's sync-adapter to upload the data to the server if part of a study
            if (Aware.getSetting(this, Aware_Preferences.FREQUENCY_WEBSERVICE).length() >= 0 && !Aware.isSyncEnabled(this, Provider.getAuthority(this)) && Aware.isStudy(this) && getApplicationContext().getPackageName().equalsIgnoreCase("com.aware.phone") || getApplicationContext().getResources().getBoolean(R.bool.standalone)) {
                ContentResolver.setIsSyncable(Aware.getAWAREAccount(this), Provider.getAuthority(this), 1);
                ContentResolver.addPeriodicSync(
                        Aware.getAWAREAccount(this),
                        Provider.getAuthority(this),
                        Bundle.EMPTY,
                        Long.parseLong(Aware.getSetting(this, Aware_Preferences.FREQUENCY_WEBSERVICE)) * 60
                );
            }

            //Initialise AWARE instance in plugin
            Aware.startAWARE(this);
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Turn off the sync-adapter if part of a study
        if (Aware.isStudy(this) && (getApplicationContext().getPackageName().equalsIgnoreCase("com.aware.phone") || getApplicationContext().getResources().getBoolean(R.bool.standalone))) {
            ContentResolver.removePeriodicSync(
                    Aware.getAWAREAccount(this),
                    Provider.getAuthority(this),
                    Bundle.EMPTY
            );
        }

        Aware.setSetting(this, Settings.STATUS_PLUGIN_TEMPLATE, false);

        //Stop AWARE instance in plugin
        Aware.stopAWARE(this);
    }

    public static void insert() {
        dbManager.insert(currentContent);
    }

    public static DBManager getDbManager() {
        return dbManager;
    }

    public static void  updateContent(String key) {
        int value = copy.getAsInteger(key+"_SUM");
        copy.remove(key+"_SUM");
        copy.put(key+"_SUM",value+1);

        currentContent.put(EmotionsVariables.anger+"_SUM",copy.getAsInteger(EmotionsVariables.anger+"_SUM"));
        currentContent.put(EmotionsVariables.contempt+"_SUM",copy.getAsInteger(EmotionsVariables.contempt+"_SUM"));
        currentContent.put(EmotionsVariables.disgust+"_SUM",copy.getAsInteger(EmotionsVariables.disgust+"_SUM"));
        currentContent.put(EmotionsVariables.fear+"_SUM",copy.getAsInteger(EmotionsVariables.fear+"_SUM"));
        currentContent.put(EmotionsVariables.happiness+"_SUM",copy.getAsInteger(EmotionsVariables.happiness+"_SUM"));
        currentContent.put(EmotionsVariables.neutral+"_SUM",copy.getAsInteger(EmotionsVariables.neutral+"_SUM"));
        currentContent.put(EmotionsVariables.sadness+"_SUM",copy.getAsInteger(EmotionsVariables.sadness+"_SUM"));
        currentContent.put(EmotionsVariables.surprise+"_SUM",copy.getAsInteger(EmotionsVariables.surprise+"_SUM"));

        copy = new ContentValues(currentContent);
    }
}
