package com.rgn.e_swasthya;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Time;
import java.util.Calendar;

public class sendNotification extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {

    Button datebutton,timebutton,donebutton;
    EditText agebox;
    RadioButton maleradio,femaleradio;
    String age,date,symptoms,gender;
    int hr,min,docid,patid;
    private static final String SHAREDPREFERENCE = "SharedPref";
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification);
        datebutton = (Button)findViewById(R.id.selectdatebutton);
        timebutton = (Button)findViewById(R.id.selecttimebutton);
        donebutton = (Button)findViewById(R.id.sendnotification);
        agebox = (EditText)findViewById(R.id.enterage);
        maleradio = (RadioButton)findViewById(R.id.radiomale);
        femaleradio = (RadioButton)findViewById(R.id.radiofemale);
        Intent i = getIntent();
        symptoms = i.getStringExtra("Symptoms");
        docid = i.getIntExtra("Doc_id",0);
        sharedPreferences = getSharedPreferences(SHAREDPREFERENCE,MODE_PRIVATE);
        patid = sharedPreferences.getInt("Pat_id",0);
        datebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(sendNotification.this,sendNotification.this,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        timebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                TimePickerDialog timePickerDialog = new TimePickerDialog(sendNotification.this,sendNotification.this,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false);
                timePickerDialog.show();
            }
        });
        maleradio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(femaleradio.isChecked())
                    femaleradio.setChecked(false);
                gender = "Male";
            }
        });
        femaleradio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(maleradio.isChecked())
                    maleradio.setChecked(false);
                gender="Female";
            }
        });
        donebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = "http://gauravkc.pe.hu/send_appointment_request_to_doctor.php?"+
                        "doc_id="+docid+"&"+
                        "symptomstring="+symptoms+"&"+
                        "date="+date+"&"+
                        "pat_id="+patid+"&"+
                        "age="+agebox.getText().toString()+"&"+
                        "hr="+hr+"&"+
                        "min="+min+"&"+
                        "gender="+gender;
                Log.d("Link is ",link);
                new DownloadRawData().execute(link);
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        date = String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(dayOfMonth);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        hr = hourOfDay;
        min = minute;
    }

    private class DownloadRawData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String link = params[0];
            try {
                URL url = new URL(link);
                InputStream is = url.openConnection().getInputStream();
                StringBuffer buffer = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String res) {
            Log.d("In Send Notification", "Message "+res);
        }
    }

}
