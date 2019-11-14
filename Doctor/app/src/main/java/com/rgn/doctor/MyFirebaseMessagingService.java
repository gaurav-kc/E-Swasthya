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
package com.rgn.doctor;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFMService";
    private DatabaseHelper databaseHelper;
    private final int DATABASE_VERSION = 1;
    private final String DATABASE_NAME="Doctor";
    private GetterSetterPendingAppointments getterSetterPendingAppointments;
    private final String notification_type = "NOTIFICATION_TYPE";
    private int notification;
    private String data;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Handle data payload of FCM messages.
        Log.d(TAG, "FCM Message Id: " + remoteMessage.getMessageId());
        Log.d(TAG, "FCM Data Message: " + remoteMessage.getData());
        //getData returns the json object. The parsing stuff now comes into the picture
        databaseHelper = new DatabaseHelper(getApplicationContext(),DATABASE_NAME, null, DATABASE_VERSION);
        data = remoteMessage.getData().get("data");
        Log.d("IN MyFirebaseMessagin","yolo "+data);
        try {
            notification = getNotificationType(data);

            if (notification == 0) {
                handlePatientAppointmentRequest(data);
            }
            if (notification == 2) {
                handleAppointmentCancellationByPatient(data);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
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
    private void handlePatientAppointmentRequest(String data) throws JSONException
    {
        JSONObject dataobj = new JSONObject(data);
        JSONObject payload = dataobj.getJSONObject("payload");
        getterSetterPendingAppointments = new GetterSetterPendingAppointments();
        getterSetterPendingAppointments.setAllValues(
                payload.getInt("PAT_ID"),
                payload.getString("PAT_NAME"),
                payload.getString("PAT_FCMID"),
                payload.getString("PAT_DATE"),
                payload.getInt("PAT_HR"),
                payload.getInt("PAT_MIN"),
                payload.getString("PAT_SS"),
                payload.getString("PAT_GENDER")

        );
        databaseHelper.insertInPA(getterSetterPendingAppointments);
        sendNotification("New Appointment request has been received", "Tap for more details");
    }
    private int getNotificationType(String data) throws JSONException
    {
        Log.d("MSGING SERVICE","getting notification type");
        JSONObject dataobj = new JSONObject(data);
        JSONObject payload = dataobj.getJSONObject("payload");
        return payload.getInt(notification_type);
    }
    private void handleAppointmentCancellationByPatient(String data) throws JSONException{
        JSONObject dataobj = new JSONObject(data);
        JSONObject payload = dataobj.getJSONObject("payload");
        int pat_id = payload.getInt("PAT_ID");
        databaseHelper.removefromA(pat_id);
        sendNotification("Appointment cancelled by "+payload.getString("PAT_NAME"), "Appointment list has been updated");
    }
}
