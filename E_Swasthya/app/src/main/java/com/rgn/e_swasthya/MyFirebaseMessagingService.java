/**
 * Copyright Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rgn.e_swasthya;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFMService";
    private DatabaseHelper databaseHelper;
    private static final String DATABASE_NAME="Doctor";
    private static final int DATABASE_VERSION=1;
    private GetterSetterAppointments getterSetterAppointments;
    private final String notification_type = "NOTIFICATION_TYPE";
    private int notification;
    private String data;
    private final String doc_id="DOC_ID";
    private final String doc_name="DOC_NAME";
    private final String doc_lat="DOC_LAT";
    private final String doc_lon = "DOC_LON";
    private final String doc_type = "DOC_TYPE";
    private final String doc_rating = "DOC_RATING";
    private final String doc_fcmid = "DOC_FCMID";
    private final String app_date = "APP_DATE";
    private final String app_hr = "APP_HR";
    private final String app_min = "APP_MIN";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Handle data payload of FCM messages.
        Log.d(TAG, "FCM Message Id: " + remoteMessage.getMessageId());
        Log.d(TAG, "FCM Data Message: " + remoteMessage.getData());
        //getData returns the json object. The parsing stuff now comes into the picture
        databaseHelper = new DatabaseHelper(getApplicationContext(),DATABASE_NAME, null, DATABASE_VERSION);
        data = remoteMessage.getData().get("data");
        try {
            notification = getNotificationType(data);

            switch (notification){
                case 0:handleAppointmentResultByDoctor(data);
                    break;
                case 1:handleMedicinesReceivedFromDoctor(data);
                    break;
                case 2:handleAppointmentCancellationByDoctor(data);
                    break;
                case 3:handleTreatmentInitiationByDoctor(data);
                    break;
                case 4:handleTreatmentTerminationByDoctor(data);
                    break;
                default:unHandledTypeError();
                    break;
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void handleAppointmentResultByDoctor(String data) throws JSONException {
        JSONObject dataobj = new JSONObject(data);
        JSONObject payload = dataobj.getJSONObject("payload");
        int result = payload.getInt("RESULT");
        String name = payload.getString("DOC_NAME");
        if(result==0){
            sendNotification("Appointment Rejected by "+name, "");
        }
        if(result==1){
            getterSetterAppointments.setAllValues(
                    payload.getInt(doc_id),
                    payload.getString(doc_name),
                    payload.getString(doc_fcmid),
                    payload.getDouble(doc_lat),
                    payload.getDouble(doc_lon),
                    payload.getString(app_date),
                    payload.getString(doc_type),
                    payload.getString(doc_rating),
                    payload.getInt(app_hr),
                    payload.getInt(app_min)
            );
            databaseHelper.insertinA(getterSetterAppointments);
            sendNotification("Appointment confirmed by "+name, "Check the timing of appointment");
        }
    }

    private void handleMedicinesReceivedFromDoctor(String data) throws JSONException  {
        JSONObject dataobj = new JSONObject(data);
        JSONObject payload = dataobj.getJSONObject("payload");
        JSONObject medicines = dataobj.getJSONObject("MEDICINES");
        int docid = payload.getInt(doc_id);
        int count = medicines.getInt("MED_COUNT");
        for(int i=0;i<count;i++)
        {

        }
    }

    private void handleAppointmentCancellationByDoctor(String data) throws JSONException  {
        JSONObject dataobj = new JSONObject(data);
        JSONObject payload = dataobj.getJSONObject("payload");
        int docid = payload.getInt(doc_id);
        databaseHelper.removeAppointment(docid);
        sendNotification("Appointment cancelled by "+payload.getString(doc_name),"Appointment list has been updated");
    }

    private void handleTreatmentInitiationByDoctor(String data) throws JSONException  {
        JSONObject dataobj = new JSONObject(data);
        JSONObject payload = dataobj.getJSONObject("payload");
        int docid = payload.getInt(doc_id);
        databaseHelper.moveFromAppointmentToCT(docid);
        sendNotification("Treatment Started by "+payload.getString(doc_name),"Tap for more details");
    }

    private void handleTreatmentTerminationByDoctor(String data) throws JSONException  {
        JSONObject dataobj = new JSONObject(data);
        JSONObject payload = dataobj.getJSONObject("payload");
        int docid = payload.getInt(doc_id);
        databaseHelper.moveFromCTtoH(docid,"5",300);
        sendNotification("Treatment terminated by "+payload.getString(doc_name),"Tap for more details");
    }

    private void unHandledTypeError(){
        sendNotification("An Unexpected error occurred","Failed to do task");
    }


    private void sendNotification(String messageBody,String subtitle) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_call_black_24dp)
                .setContentTitle(subtitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private int getNotificationType(String data) throws JSONException
    {
        Log.d("MSGING SERVICE","getting notification type");
        JSONObject dataobj = new JSONObject(data);
        JSONObject payload = dataobj.getJSONObject("payload");
        return payload.getInt(notification_type);
    }


}

