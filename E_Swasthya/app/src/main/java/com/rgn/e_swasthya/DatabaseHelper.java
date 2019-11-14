package com.rgn.e_swasthya;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 4/14/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="Doctor";
    private static final int DATABASE_VERSION=1;

    private static final String TABLE_APPOINTMENTS="Appointments";
    private static final String TABLE_CURRENT_TREATMENT="Current_treatment";
    private static final String TABLE_HISTORY="History";
    private static final String TABLE_SYMPTOM_STRINGS="Symptom_string";
    private static final String TABLE_MEDICINES="Medicines";

    private static final String COL_A_DOC_ID="Doc_id";
    private static final String COL_A_DOC_NAME="Doc_name";
    private static final String COL_A_FCM_KEY="fcm_key";
    private static final String COL_A_LATITUDE="Latitude";
    private static final String COL_A_LONGITUDE="longitude";
    private static final String COL_A_DATE="date";
    private static final String COL_A_APP_HR="hr";
    private static final String COL_A_APP_MIN="min";
    private static final String COL_A_TYPE="type";
    private static final String COL_A_RATING="rating";

    private static final String COL_CT_DOC_ID="Doc_id";
    private static final String COL_CT_DOC_NAME="Doc_name";
    private static final String COL_CT_DOC_FCM_KEY="fcm_key";
    private static final String COL_CT_START_DATE="start_date";
    private static final String COL_CT_DURATION="duration";

    private static final String COL_H_ID="h_id";
    private static final String COL_H_DOC_NAME="Doc_name";
    private static final String COL_H_DURATION="duration";
    private static final String COL_H_COST="cost";


    private static final String COL_SS_ID="ss_id";
    private static final String COL_SS_STRING="s_string";
    private static final String COL_SS_DOC_TYPE="doc_type";

    private static final String COL_M_ID="med_id";
    private static final String COL_M_DOC_ID="doc_id";
    private static final String COL_M_NAME="med_name";
    private static final String COL_M_DOSAGE="med_dose";
    private static final String COL_M_TIMING="med_timing";
    private static final String COL_M_TYPE="med_type";
    private static final String COL_M_BASECOST="med_base_cost";

    private static final String CREATE_TABLE_A="CREATE TABLE IF NOT EXISTS "+TABLE_APPOINTMENTS+" ("+
            COL_A_DOC_ID+" INTEGER PRIMARY KEY,"+
            COL_A_DOC_NAME+" TEXT,"+
            COL_A_FCM_KEY+" VARCHAR(400),"+
            COL_A_LATITUDE+" DECIMAL(10,8), "+
            COL_A_LONGITUDE+" DECIMAL(11,8), "+
            COL_A_TYPE+" VARCHAR(50), "+
            COL_A_RATING+" VARCHAR(5), "+
            COL_A_DATE+" DATE, "+
            COL_A_APP_HR+" INTEGER, "+
            COL_A_APP_MIN+" INTEGER);";
    private static final String CREATE_TABLE_CT="CREATE TABLE IF NOT EXISTS "+TABLE_CURRENT_TREATMENT+" ("+
            COL_CT_DOC_ID+" INTEGER PRIMARY KEY,"+
            COL_CT_DOC_NAME+" TEXT,"+
            COL_CT_DOC_FCM_KEY+" VARCHAR(400), "+
            COL_CT_DURATION+" VARCHAR(400), "+
            COL_CT_START_DATE+" DATE);";
    private static final String CREATE_TABLE_H="CREATE TABLE IF NOT EXISTS "+TABLE_HISTORY+" ("+
            COL_H_ID+" INTEGER PRIMARY KEY,"+
            COL_H_DOC_NAME+" TEXT,"+
            COL_H_DURATION+" INTEGER,"+
            COL_H_COST+" INTEGER);";
    private static final String CREATE_TABLE_SS="CREATE TABLE IF NOT EXISTS "+TABLE_SYMPTOM_STRINGS+" ("+
            COL_SS_ID+" INTEGER PRIMARY KEY,"+
            COL_SS_STRING+" TEXT,"+
            COL_SS_DOC_TYPE+" TEXT);";
    private static final String CREATE_TABLE_MEDICINES="CREATE TABLE IF NOT EXISTS "+TABLE_MEDICINES+" ("+
            COL_M_ID+" INTEGER PRIMARY KEY,"+
            COL_M_DOC_ID+" INTEGER,"+
            COL_M_NAME+" TEXT,"+
            COL_M_DOSAGE+" INTEGER,"+
            COL_M_TIMING+" INTEGER,"+
            COL_M_TYPE+" VARCHAR(50), "+
            COL_M_BASECOST+" INTEGER);";



    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_A);
        db.execSQL(CREATE_TABLE_CT);
        db.execSQL(CREATE_TABLE_H);
        db.execSQL(CREATE_TABLE_MEDICINES);
        db.execSQL(CREATE_TABLE_SS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_APPOINTMENTS);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CURRENT_TREATMENT);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_MEDICINES);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_HISTORY);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_SYMPTOM_STRINGS);
    }
    public boolean insertinA(GetterSetterAppointments getterSetterAppointments)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_A_DOC_ID,getterSetterAppointments.getDoc_id());
        contentValues.put(COL_A_DOC_NAME,getterSetterAppointments.getDoc_name());
        contentValues.put(COL_A_FCM_KEY,getterSetterAppointments.getFcm_key());
        contentValues.put(COL_A_LATITUDE,getterSetterAppointments.getLatitude());
        contentValues.put(COL_A_LONGITUDE,getterSetterAppointments.getLongitude());
        contentValues.put(COL_A_DATE,getterSetterAppointments.getDate());
        contentValues.put(COL_A_TYPE,getterSetterAppointments.getType());
        contentValues.put(COL_A_RATING,getterSetterAppointments.getRating());
        contentValues.put(COL_A_APP_HR,getterSetterAppointments.getHr());
        contentValues.put(COL_A_APP_MIN,getterSetterAppointments.getMin());
        if(db!=null && contentValues!=null)
        {
            db.insert(TABLE_APPOINTMENTS,null,contentValues);
            db.close();
            return true;
        }
        return false;
    }
    public boolean insertinCT(GetterSetterCurrentTreatments getterSetterCurrentTreatments)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_CT_DOC_ID,getterSetterCurrentTreatments.getDoc_id());
        contentValues.put(COL_CT_DOC_NAME,getterSetterCurrentTreatments.getDoc_name());
        contentValues.put(COL_CT_DOC_FCM_KEY,getterSetterCurrentTreatments.getFcm_key());
        contentValues.put(COL_CT_START_DATE,getterSetterCurrentTreatments.getStart_date());
        contentValues.put(COL_CT_DURATION,getterSetterCurrentTreatments.getDuration());
        if(db!=null && contentValues!=null)
        {
            db.insert(TABLE_CURRENT_TREATMENT,null,contentValues);
            db.close();
            return true;
        }
        return false;
    }
    public boolean insertinH(GetterSetterHistory getterSetterHistory)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_H_ID,getterSetterHistory.getH_id());
        contentValues.put(COL_H_DOC_NAME,getterSetterHistory.getDoc_name());
        contentValues.put(COL_H_DURATION,getterSetterHistory.getDuration());
        contentValues.put(COL_H_COST,getterSetterHistory.getCost());
        if(db!=null && contentValues!=null)
        {
            db.insert(TABLE_HISTORY,null,contentValues);
            db.close();
            return true;
        }
        return false;
    }
    public boolean insertinM(GetterSetterMedicines getterSetterMedicines)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_M_ID,getterSetterMedicines.getMed_id());
        contentValues.put(COL_M_DOC_ID,getterSetterMedicines.getDoc_id());
        contentValues.put(COL_M_NAME,getterSetterMedicines.getMed_name());
        contentValues.put(COL_M_DOSAGE,getterSetterMedicines.getMed_dose());
        contentValues.put(COL_M_TIMING,getterSetterMedicines.getMed_timing());
        contentValues.put(COL_M_TYPE,getterSetterMedicines.getMed_type());
        contentValues.put(COL_M_BASECOST,getterSetterMedicines.getMed_base_cost());
        if(db!=null && contentValues!=null)
        {
            db.insert(TABLE_MEDICINES,null,contentValues);
            db.close();
            return true;
        }
        return false;
    }
    public boolean insertinS(GetterSetterSymtoms getterSetterSymtoms)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_SS_ID,getterSetterSymtoms.getSs_id());
        contentValues.put(COL_SS_STRING,getterSetterSymtoms.getS_string());
        contentValues.put(COL_SS_DOC_TYPE,getterSetterSymtoms.getDoc_type());
        if(db!=null && contentValues!=null)
        {
            db.insert(TABLE_SYMPTOM_STRINGS,null,contentValues);
            db.close();
            return true;
        }
        return false;
    }
    public GetterSetterAppointments getDetailsA(int id)
    {
        GetterSetterAppointments getterSetterAppointments = new GetterSetterAppointments();
        String query="SELECT * FROM "+TABLE_APPOINTMENTS+" WHERE "+COL_A_DOC_ID+" = "+id+";";
        SQLiteDatabase db = this.getReadableDatabase();
        if(db!=null)
        {
            Cursor cursor = db.rawQuery(query,null);
            if(cursor!=null)
            {
                cursor.moveToFirst();
            }
            getterSetterAppointments.setDoc_id(cursor.getInt(cursor.getColumnIndex(COL_A_DOC_ID)));
            getterSetterAppointments.setDoc_name(cursor.getString(cursor.getColumnIndex(COL_A_DOC_NAME)));
            getterSetterAppointments.setFcm_key(cursor.getString(cursor.getColumnIndex(COL_A_FCM_KEY)));
            getterSetterAppointments.setLatitude(cursor.getDouble(cursor.getColumnIndex(COL_A_LATITUDE)));
            getterSetterAppointments.setLongitude(cursor.getDouble(cursor.getColumnIndex(COL_A_LONGITUDE)));
            getterSetterAppointments.setDate(cursor.getString(cursor.getColumnIndex(COL_A_DATE)));
            getterSetterAppointments.setType(cursor.getString(cursor.getColumnIndex(COL_A_TYPE)));
            getterSetterAppointments.setRating(cursor.getString(cursor.getColumnIndex(COL_A_RATING)));
            getterSetterAppointments.setHr(cursor.getInt(cursor.getColumnIndex(COL_A_APP_HR)));
            getterSetterAppointments.setMin(cursor.getInt(cursor.getColumnIndex(COL_A_APP_MIN)));
            cleanup(cursor,db);
            return getterSetterAppointments;
        }
        return null;
    }
    public GetterSetterCurrentTreatments getDetailsCT(int id)
    {
        GetterSetterCurrentTreatments getterSetterCurrentTreatments = new GetterSetterCurrentTreatments();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+TABLE_CURRENT_TREATMENT+" WHERE "+COL_CT_DOC_ID+" = "+id+";";
        if(db!=null)
        {
            Cursor cursor = db.rawQuery(query,null);
            if(cursor!=null)
            {
                cursor.moveToFirst();
            }
            getterSetterCurrentTreatments.setDoc_id(cursor.getInt(cursor.getColumnIndex(COL_CT_DOC_ID)));
            getterSetterCurrentTreatments.setDoc_name(cursor.getString(cursor.getColumnIndex(COL_CT_DOC_NAME)));
            getterSetterCurrentTreatments.setFcm_key(cursor.getString(cursor.getColumnIndex(COL_CT_DOC_FCM_KEY)));
            getterSetterCurrentTreatments.setStart_date(cursor.getString(cursor.getColumnIndex(COL_CT_START_DATE)));
            getterSetterCurrentTreatments.setDuration(cursor.getInt(cursor.getColumnIndex(COL_CT_DURATION)));
            cleanup(cursor,db);
            return getterSetterCurrentTreatments;
        }
        return null;
    }
    public GetterSetterMedicines getDetailsM(int id)
    {
        GetterSetterMedicines getterSetterMedicines = new GetterSetterMedicines();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+TABLE_MEDICINES+" WHERE "+COL_M_ID+" = "+id+";";
        if(db!=null)
        {
            Cursor cursor = db.rawQuery(query,null);
            if(cursor!=null)
            {
                cursor.moveToFirst();
            }
            getterSetterMedicines.setMed_id(cursor.getInt(cursor.getColumnIndex(COL_M_ID)));
            getterSetterMedicines.setDoc_id(cursor.getInt(cursor.getColumnIndex(COL_M_DOC_ID)));
            getterSetterMedicines.setMed_name(cursor.getString(cursor.getColumnIndex(COL_M_NAME)));
            getterSetterMedicines.setMed_dose(cursor.getInt(cursor.getColumnIndex(COL_M_DOSAGE)));
            getterSetterMedicines.setMed_timing(cursor.getInt(cursor.getColumnIndex(COL_M_TIMING)));
            getterSetterMedicines.setMed_type(cursor.getString(cursor.getColumnIndex(COL_M_TYPE)));
            getterSetterMedicines.setMed_base_cost(cursor.getInt(cursor.getColumnIndex(COL_M_BASECOST)));
            cleanup(cursor,db);
            return getterSetterMedicines;
        }
        return null;
    }
    public GetterSetterHistory getDetailsH(int id)
    {
        GetterSetterHistory getterSetterHistory = new GetterSetterHistory();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+TABLE_HISTORY+" WHERE "+COL_H_ID+" = "+id+";";
        if(db!=null)
        {
            Cursor cursor = db.rawQuery(query,null);
            if(cursor!=null)
            {
                cursor.moveToFirst();
            }
            getterSetterHistory.setH_id(cursor.getInt(cursor.getColumnIndex(COL_H_ID)));
            getterSetterHistory.setDoc_name(cursor.getString(cursor.getColumnIndex(COL_H_DOC_NAME)));
            getterSetterHistory.setDuration(cursor.getString(cursor.getColumnIndex(COL_H_DURATION)));
            getterSetterHistory.setCost(cursor.getInt(cursor.getColumnIndex(COL_H_COST)));
            cleanup(cursor,db);
            return getterSetterHistory;
        }
        return null;
    }
    public GetterSetterSymtoms getDetailsSS(int id)
    {
        GetterSetterSymtoms getterSetterSymtoms = new GetterSetterSymtoms();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+TABLE_SYMPTOM_STRINGS+" WHERE "+COL_SS_ID+" = "+id+";";
        if(db!=null)
        {
            Cursor cursor = db.rawQuery(query,null);
            if(cursor!=null)
            {
                cursor.moveToFirst();
            }
            getterSetterSymtoms.setSs_id(cursor.getInt(cursor.getColumnIndex(COL_SS_ID)));
            getterSetterSymtoms.setS_string(cursor.getString(cursor.getColumnIndex(COL_SS_STRING)));
            getterSetterSymtoms.setDoc_type(cursor.getString(cursor.getColumnIndex(COL_SS_DOC_TYPE)));
            cleanup(cursor,db);
            return getterSetterSymtoms;
        }
        return null;
    }
    private void cleanup(Cursor cursor,SQLiteDatabase db)
    {
        cursor.close();
        db.close();
    }
    public Cursor getAllA(){
        String details = "SELECT "+COL_A_DOC_ID+","+COL_A_DOC_NAME+" FROM "+TABLE_APPOINTMENTS+";";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(details,null);
        return cursor;
    }
    public Cursor getAllCT(){
        String details = "SELECT "+COL_CT_DOC_ID+","+COL_CT_DOC_NAME+" FROM "+TABLE_CURRENT_TREATMENT+";";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(details,null);
        return cursor;

    }
    public Cursor getAllH(){
        String details = "SELECT "+COL_H_ID+","+COL_H_DOC_NAME+" FROM "+TABLE_HISTORY+";";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(details,null);
        return cursor;

    }
    public Cursor getAllM(){
        String details = "SELECT "+COL_M_ID+","+COL_M_NAME+","+COL_M_DOC_ID+" FROM "+TABLE_MEDICINES+";";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(details,null);
        return cursor;

    }
    public void moveFromAppointmentToCT(int id) {
        String deletequery = "DELETE FROM " + TABLE_APPOINTMENTS + " WHERE " + COL_A_DOC_ID + " = " + id + ";";
        String getdetailsquery = "SELECT * FROM " + TABLE_APPOINTMENTS + " WHERE " + COL_A_DOC_ID + " = " + id + ";";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(getdetailsquery, null);
        if (cursor != null)
        {
            cursor.moveToFirst();
            GetterSetterCurrentTreatments getterSetterCurrentTreatments = new GetterSetterCurrentTreatments();
            getterSetterCurrentTreatments.setDoc_id(cursor.getInt(cursor.getColumnIndex(COL_A_DOC_ID)));
            getterSetterCurrentTreatments.setDoc_name(cursor.getString(cursor.getColumnIndex(COL_A_DOC_NAME)));
            getterSetterCurrentTreatments.setFcm_key(cursor.getString(cursor.getColumnIndex(COL_A_FCM_KEY)));
            getterSetterCurrentTreatments.setStart_date(cursor.getString(cursor.getColumnIndex(COL_A_DATE)));
            getterSetterCurrentTreatments.setDuration(9);
            insertinCT(getterSetterCurrentTreatments);
            db.execSQL(deletequery);
            cursor.close();
        }
    }
    public void moveFromCTtoH(int id,String duration,int cost){
        String deletequery = "DELETE FROM " + TABLE_CURRENT_TREATMENT + " WHERE " + COL_CT_DOC_ID + " = " + id + ";";
        String getdetailsquery = "SELECT * FROM " + TABLE_CURRENT_TREATMENT + " WHERE " + COL_CT_DOC_ID + " = " + id + ";";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(getdetailsquery, null);
        if (cursor != null)
        {
            cursor.moveToFirst();
            GetterSetterHistory getterSetterHistory = new GetterSetterHistory();
            getterSetterHistory.setH_id(cursor.getInt(cursor.getColumnIndex(COL_CT_DOC_ID)));
            getterSetterHistory.setDoc_name(cursor.getString(cursor.getColumnIndex(COL_CT_DOC_NAME)));
            getterSetterHistory.setDuration(duration);
            getterSetterHistory.setCost(cost);
            insertinH(getterSetterHistory);
            db.execSQL(deletequery);
            cursor.close();
        }
    }
    public void removeAppointment(int id){
        String deletequery = "DELETE FROM " + TABLE_APPOINTMENTS + " WHERE " + COL_A_DOC_ID + " = " + id + ";";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(deletequery);
        db.close();
    }
    public GetterSetterAppointments getCurrentDoctor(){
        SQLiteDatabase db = getReadableDatabase();
        List<temp> tempList = new ArrayList<temp>();
        String getDate="SELECT "+COL_A_APP_HR+","+COL_A_APP_MIN+","+COL_A_DOC_ID+",MAX("+COL_A_DATE+") FROM "+TABLE_APPOINTMENTS+";";
        Cursor cursor1 = db.rawQuery(getDate,null);
        if(cursor1!=null)
        {
            cursor1.moveToFirst();
            while(!cursor1.isAfterLast()){
                Log.d("DATABASE HELPER"," in main loop");
                temp t = new temp();
                t.setHr(cursor1.getInt(cursor1.getColumnIndex(COL_A_APP_HR)));
                t.setMin(cursor1.getInt(cursor1.getColumnIndex(COL_A_APP_MIN)));
                t.setId(cursor1.getInt(cursor1.getColumnIndex(COL_A_DOC_ID)));
                tempList.add(t);
                cursor1.moveToNext();
            }
            temp latest = getLatest(tempList);
            GetterSetterAppointments gettersetter = this.getDetailsA(latest.getId());
            cursor1.close();
            db.close();
            return gettersetter;
        }
        return null;
    }
    class temp{
        private int hr,min,id;

        public int getHr() {
            return hr;
        }

        public void setHr(int hr) {
            this.hr = hr;
        }

        public int getMin() {
            return min;
        }

        public void setMin(int min) {
            this.min = min;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
    private temp getLatest(List<temp> tempList){
        int temphr=24,tempmin=60,tempid=1;
        temp temporary = new temp();
        for(int i=0;i<tempList.size();i++){
            temporary = tempList.get(i);
            if(temporary.getHr()<temphr)
                temphr = temporary.getHr();
        }
        for(int i=0;i<tempList.size();i++) {
            temporary = tempList.get(i);
            if (temporary.getHr() == temphr && temporary.getMin() < tempmin) {
                tempmin = temporary.getMin();
                tempid=temporary.getId();
            }
        }
        temporary.setHr(temphr);
        temporary.setMin(tempmin);
        temporary.setId(tempid);
        return temporary;
    }
}
