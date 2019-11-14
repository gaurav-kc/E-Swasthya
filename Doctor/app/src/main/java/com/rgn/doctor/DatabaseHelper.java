package com.rgn.doctor;

import android.content.ContentUris;
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

    private final static String LOG="DatabaseHelper";
    private final int DATABASE_VERSION = 1;
    private final String DATABASE_NAME="Doctor";
    private final String TABLE_PENDING_APPOINTMENT="PendingAppointment";
    private final String TABLE_MEDICINES="Medicines";
    private final String TABLE_HISTORY="History";
    private final String TABLE_CURRENT_TREATMENT="CurrentTreatment";
    private final String TABLE_APPOINTMENTS="Appointments";

    private final String COL_A_PAT_ID="Pat_id";
    private final String COL_A_PAT_NAME="Pat_name";
    private final String COL_A_DATE="Pat_app_date";
    private final String COL_A_APPHR="Pat_app_hr";
    private final String COL_A_APPMIN="Pat_app_min";
    private final String COL_A_SS="Pat_ss";
    private final String COL_A_PAT_FCM="Pat_fcm";

    private final String COL_PA_ID="Patient_id";
    private final String COL_PA_NAME="Patient_name";
    private final String COL_PA_FCM_KEY="Patient_fcm_key";
    private final String COL_PA_DATE="Patient_app_date";
    private final String COL_PA_HR="Patient_start_hr";
    private final String COL_PA_MIN="Patient_start_min";
    private final String COL_PA_SS="Patient_Symptom_string";
    private final String COL_PA_GENDER="Patient_gender";

    private final String COL_M_ID="Medicine_id";
    private final String COL_M_NAME="Medicine_name";
    private final String COL_M_TYPE="Medicine_type";
    private final String COL_M_DOSAGE="Medicine_dose";
    private final String COL_M_TIMING="Medicine_timing";
    private final String COL_M_BASECOST="Medicine_base_cost";

    private final String COL_H_ID="History_id";
    private final String COL_H_NAME="Patient_name";
    private final String COL_H_START_DATE="Treatment_start_date";
    private final String COL_H_DURATION="Treatment_Duration";
    private final String COL_H_SYMPTOMS="Patient_ss";

    private final String COL_CT_ID="Patient_id";
    private final String COL_CT_NAME="Patient_name";
    private final String COL_CT_FCM_KEY="Patient_fcm_key";
    private final String COL_CT_START_DATE="Patient_start_date";
    private final String COL_CT_SS="Patient_symptom_string";

    private final String CREATE_TABLE_PA="CREATE TABLE IF NOT EXISTS "+TABLE_PENDING_APPOINTMENT+" ("+
            COL_PA_ID+" INTEGER PRIMARY KEY, "+
            COL_PA_NAME+" TEXT,"+
            COL_PA_FCM_KEY+" VARCHAR(400), "+
            COL_PA_DATE+" TEXT,"+
            COL_PA_HR+" INTEGER,"+
            COL_PA_MIN+" INTEGER,"+
            COL_PA_GENDER+" VARCHAR(20), "+
            COL_PA_SS+" VARCHAR(20));";
    private final String CREATE_TABLE_M="CREATE TABLE IF NOT EXISTS "+TABLE_MEDICINES+" ("+
            COL_M_ID+" INTEGER PRIMARY KEY, "+
            COL_M_NAME+" TEXT,"+
            COL_M_TYPE+" VARCHAR(50), "+
            COL_M_DOSAGE+" INTEGER, "+
            COL_M_TIMING+" INTEGER, "+
            COL_M_BASECOST+" INTEGER);";
    private final String CREATE_TABLE_H="CREATE TABLE IF NOT EXISTS "+TABLE_HISTORY+" ("+
            COL_H_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            COL_H_NAME+" TEXT,"+
            COL_H_START_DATE+" VARCHAR(400), "+
            COL_H_DURATION+" INTEGER,"+
            COL_H_SYMPTOMS+" VARCHAR(20));";
    private final String CREATE_TABLE_CT="CREATE TABLE IF NOT EXISTS "+TABLE_CURRENT_TREATMENT+" ("+
            COL_CT_ID+" INTEGER PRIMARY KEY, "+
            COL_CT_NAME+" VARCHAR(50), "+
            COL_CT_FCM_KEY+" VARCHAR(400), "+
            COL_CT_START_DATE+" TEXT,"+
            COL_CT_SS+" TEXT);";
    private final String CREATE_TABLE_A="CREATE TABLE IF NOT EXISTS "+TABLE_APPOINTMENTS+" ("+
            COL_A_PAT_ID+" INTEGER PRIMARY KEY, "+
            COL_A_PAT_NAME+" VARCHAR(50), "+
            COL_A_DATE+" DATE, "+
            COL_A_APPHR+" INTEGER, "+
            COL_A_APPMIN+" INTEGER, "+
            COL_A_PAT_FCM+" VARCHAR(400), "+
            COL_A_SS+" TEXT);";


    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PA);
        db.execSQL(CREATE_TABLE_CT);
        db.execSQL(CREATE_TABLE_H);
        db.execSQL(CREATE_TABLE_M);
        db.execSQL(CREATE_TABLE_A);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CURRENT_TREATMENT);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_HISTORY);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_MEDICINES);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_PENDING_APPOINTMENT);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_APPOINTMENTS);
    }
    public boolean insertInPA(GetterSetterPendingAppointments getterSetterPendingAppointments){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_PA_ID,getterSetterPendingAppointments.getPatient_id());
        contentValues.put(COL_PA_NAME,getterSetterPendingAppointments.getPatient_name());
        contentValues.put(COL_PA_FCM_KEY,getterSetterPendingAppointments.getPatient_fcm_key());
        contentValues.put(COL_PA_DATE,getterSetterPendingAppointments.getPatient_app_date());
        contentValues.put(COL_PA_HR,getterSetterPendingAppointments.getPatient_start_hr());
        contentValues.put(COL_PA_MIN,getterSetterPendingAppointments.getPatient_start_min());
        contentValues.put(COL_PA_SS,getterSetterPendingAppointments.getPatient_Symptom_string());
        contentValues.put(COL_PA_GENDER,getterSetterPendingAppointments.getPatient_gender());
        if(db!=null && contentValues!=null) {
            db.insert(TABLE_PENDING_APPOINTMENT, null, contentValues);
            return true;
        }
        return false;
    }
    public boolean insertInCT(GetterSetterCurrentTreatments getterSetterCurrentTreatments){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_CT_ID,getterSetterCurrentTreatments.getPatient_id());
        contentValues.put(COL_CT_NAME,getterSetterCurrentTreatments.getPatient_name());
        contentValues.put(COL_CT_FCM_KEY,getterSetterCurrentTreatments.getPatient_fcm_key());
        contentValues.put(COL_CT_START_DATE,getterSetterCurrentTreatments.getPatient_start_date());
        contentValues.put(COL_CT_SS,getterSetterCurrentTreatments.getPatient_symptom_string());
        if(db!=null && contentValues!=null) {
            db.insert(TABLE_CURRENT_TREATMENT, null, contentValues);
            return true;
        }
        return false;
    }
    public boolean insertInH(GetterSetterHistory getterSetterHistory){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_H_ID,getterSetterHistory.getHistory_id());
        contentValues.put(COL_H_NAME,getterSetterHistory.getPatient_name());
        contentValues.put(COL_H_START_DATE,getterSetterHistory.getTreatment_start_date());
        contentValues.put(COL_H_DURATION,getterSetterHistory.getTreatment_Duration());
        contentValues.put(COL_H_SYMPTOMS,getterSetterHistory.getPatient_ss());
        if(db!=null && contentValues!=null) {
            db.insert(TABLE_HISTORY, null, contentValues);
            return true;
        }
        return false;
    }
    public boolean insertInM(GetterSetterMedicines getterSetterMedicines){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_M_ID,getterSetterMedicines.getMedicine_id());
        contentValues.put(COL_M_NAME,getterSetterMedicines.getMedicine_name());
        contentValues.put(COL_M_TYPE,getterSetterMedicines.getMedicine_type());
        contentValues.put(COL_M_DOSAGE,getterSetterMedicines.getMedicine_dose());
        contentValues.put(COL_M_TIMING,getterSetterMedicines.getMedicine_timing());
        contentValues.put(COL_M_BASECOST,getterSetterMedicines.getMedicine_base_cost());
        if(db!=null && contentValues!=null) {
            db.insert(TABLE_MEDICINES, null, contentValues);
            return true;
        }
        return false;
    }
    public boolean insertInA(GetterSetterAppointments getterSetterAppointments){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_A_PAT_ID,getterSetterAppointments.getPat_id());
        contentValues.put(COL_A_PAT_NAME,getterSetterAppointments.getPat_name());
        contentValues.put(COL_A_DATE,getterSetterAppointments.getPat_app_date());
        contentValues.put(COL_A_SS,getterSetterAppointments.getPat_ss());
        contentValues.put(COL_A_PAT_FCM,getterSetterAppointments.getPat_fcm());
        contentValues.put(COL_A_APPHR,getterSetterAppointments.getPat_app_hr());
        contentValues.put(COL_A_APPMIN,getterSetterAppointments.getPat_app_min());
        if(db!=null){
            db.insert(TABLE_APPOINTMENTS,null,contentValues);
            return true;
        }
        return false;
    }
    public GetterSetterPendingAppointments getDetailsPA(int id){
        String getDetails="SELECT * FROM "+TABLE_PENDING_APPOINTMENT+" WHERE "+COL_PA_ID+" = "+id+";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(getDetails,null);
        if(db!=null) {
            if (cursor != null) {
                cursor.moveToFirst();
            }
            GetterSetterPendingAppointments getterSetterPendingAppointments = new GetterSetterPendingAppointments();
            getterSetterPendingAppointments.setPatient_id(cursor.getInt(cursor.getColumnIndex(COL_PA_ID)));
            getterSetterPendingAppointments.setPatient_name(cursor.getString(cursor.getColumnIndex(COL_PA_NAME)));
            getterSetterPendingAppointments.setPatient_fcm_key(cursor.getString(cursor.getColumnIndex(COL_PA_FCM_KEY)));
            getterSetterPendingAppointments.setPatient_app_date(cursor.getString(cursor.getColumnIndex(COL_PA_DATE)));
            getterSetterPendingAppointments.setPatient_start_hr(cursor.getInt(cursor.getColumnIndex(COL_PA_HR)));
            getterSetterPendingAppointments.setPatient_start_min(cursor.getInt(cursor.getColumnIndex(COL_PA_MIN)));
            getterSetterPendingAppointments.setPatient_Symptom_string(cursor.getString(cursor.getColumnIndex(COL_PA_SS)));
            getterSetterPendingAppointments.setPatient_gender(cursor.getString(cursor.getColumnIndex(COL_PA_GENDER)));
            cleanup(cursor,db);
            return getterSetterPendingAppointments;
        }
        return null;
    }
    public GetterSetterCurrentTreatments getDetailsCT(int id){
        String getDetails="SELECT * FROM "+TABLE_CURRENT_TREATMENT+" WHERE +"+COL_CT_ID+" = "+id+";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(getDetails,null);
        if(db!=null) {
            if (cursor != null) {
                cursor.moveToFirst();
            }
            if(cursor==null || cursor.getCount()<1)
            {
                Log.d("Cursor in db ","is null");
            }
            GetterSetterCurrentTreatments getterSetterCurrentTreatments = new GetterSetterCurrentTreatments();
            getterSetterCurrentTreatments.setPatient_id(cursor.getInt(cursor.getColumnIndex(COL_CT_ID)));
            getterSetterCurrentTreatments.setPatient_name(cursor.getString(cursor.getColumnIndex(COL_CT_NAME)));
            getterSetterCurrentTreatments.setPatient_fcm_key(cursor.getString(cursor.getColumnIndex(COL_CT_FCM_KEY)));
            getterSetterCurrentTreatments.setPatient_symptom_string(cursor.getString(cursor.getColumnIndex(COL_CT_START_DATE)));
            getterSetterCurrentTreatments.setPatient_symptom_string(cursor.getString(cursor.getColumnIndex(COL_CT_SS)));
            cleanup(cursor,db);
            return getterSetterCurrentTreatments;
        }
        return null;
    }
    public GetterSetterMedicines getDetailsM(int id){
        String getDetails="SELECT * FROM "+TABLE_MEDICINES+" WHERE "+COL_M_ID+" = "+id+";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(getDetails,null);
        if(db!=null) {
            if (cursor != null) {
                cursor.moveToFirst();
            }
            GetterSetterMedicines getterSetterMedicines = new GetterSetterMedicines();
            getterSetterMedicines.setMedicine_id(cursor.getInt(cursor.getColumnIndex(COL_M_ID)));
            getterSetterMedicines.setMedicine_dose(cursor.getInt(cursor.getColumnIndex(COL_M_DOSAGE)));
            getterSetterMedicines.setMedicine_name(cursor.getString(cursor.getColumnIndex(COL_M_NAME)));
            getterSetterMedicines.setMedicine_type(cursor.getString(cursor.getColumnIndex(COL_M_TYPE)));
            getterSetterMedicines.setMedicine_timing(cursor.getInt(cursor.getColumnIndex(COL_M_TIMING)));
            getterSetterMedicines.setMedicine_base_cost(cursor.getInt(cursor.getColumnIndex(COL_M_BASECOST)));
            cleanup(cursor,db);
            return getterSetterMedicines;
        }
        return null;
    }
    public Cursor getDetailsfromChaseq(String s)
    {
        String getDetails="SELECT "+COL_M_ID+","+COL_M_NAME+" FROM "+TABLE_MEDICINES+" WHERE "+COL_M_NAME+" LIKE '%"+s+"%';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(getDetails,null);
            if (cursor != null) {
                cursor.moveToFirst();

                return cursor;
            }
        return null;
    }
    public GetterSetterHistory getDetailsH(int id){
        String getDetails="SELECT * FROM "+TABLE_HISTORY+" WHERE "+COL_H_ID+" = "+id+";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(getDetails,null);
        if(db!=null) {
            if (cursor != null) {
                cursor.moveToFirst();
            }
            GetterSetterHistory getterSetterHistory = new GetterSetterHistory();
            getterSetterHistory.setHistory_id(cursor.getInt(cursor.getColumnIndex(COL_H_ID)));
            getterSetterHistory.setPatient_name(cursor.getString(cursor.getColumnIndex(COL_H_NAME)));
            getterSetterHistory.setTreatment_Duration(cursor.getInt(cursor.getColumnIndex(COL_H_DURATION)));
            getterSetterHistory.setPatient_ss(cursor.getString(cursor.getColumnIndex(COL_H_SYMPTOMS)));
            getterSetterHistory.setTreatment_start_date(cursor.getString(cursor.getColumnIndex(COL_H_START_DATE)));
            cleanup(cursor,db);
            return getterSetterHistory;
        }
        return null;
    }
    public GetterSetterAppointments getDetailsA(int id){
        String getDetails="SELECT * FROM "+TABLE_APPOINTMENTS+" WHERE "+COL_A_PAT_ID+" = "+id+";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(getDetails,null);
        if(db!=null){
            if(cursor!=null) {
                cursor.moveToFirst();
                GetterSetterAppointments getterSetterAppointments = new GetterSetterAppointments();
                getterSetterAppointments.setPat_id(cursor.getInt(cursor.getColumnIndex(COL_A_PAT_ID)));
                getterSetterAppointments.setPat_name(cursor.getString(cursor.getColumnIndex(COL_A_PAT_NAME)));
                getterSetterAppointments.setPat_app_date(cursor.getString(cursor.getColumnIndex(COL_A_DATE)));
                getterSetterAppointments.setPat_ss(cursor.getString(cursor.getColumnIndex(COL_A_SS)));
                getterSetterAppointments.setPat_fcm(cursor.getString(cursor.getColumnIndex(COL_A_PAT_FCM)));
                getterSetterAppointments.setPat_app_hr(cursor.getInt(cursor.getColumnIndex(COL_A_APPHR)));
                getterSetterAppointments.setPat_app_min(cursor.getInt(cursor.getColumnIndex(COL_A_APPMIN)));
                cleanup(cursor, db);
                return getterSetterAppointments;
            }
        }
        return null;
    }
    private void cleanup(Cursor cursor,SQLiteDatabase sqLiteDatabase){
        cursor.close();
        sqLiteDatabase.close();
    }
    public void insertsomeentriesinPA(){
        GetterSetterPendingAppointments getterSetterPendingAppointments = new GetterSetterPendingAppointments();
        getterSetterPendingAppointments.setAllValues(1,"Mr.Gaurav Chaudhari",
                "my_fcm_key_here","2017-5-5",8,00,"9468532616","MALE");
        this.insertInPA(getterSetterPendingAppointments);
        getterSetterPendingAppointments.setAllValues(2,"Mr.Sagar Chaudhari",
                "sagar_fcm_key","2017-5-6",9,00,"9876543210","MALE");
        this.insertInPA(getterSetterPendingAppointments);
    }
    public void insertsomeentriesinM(){
        GetterSetterMedicines getterSetterMedicines = new GetterSetterMedicines();
            getterSetterMedicines.setAllValues(2001, "Crocin", "tablets", 2,3,25);
        this.insertInM(getterSetterMedicines);
        getterSetterMedicines.setAllValues(2002, "Althrocin", "ml", 3,2,50);
        this.insertInM(getterSetterMedicines);
        getterSetterMedicines.setAllValues(2003, "Honitus", "ml", 3,3,50);
        this.insertInM(getterSetterMedicines);
        getterSetterMedicines.setAllValues(2004, "Vicks", "tablets", 2,4,50);
        this.insertInM(getterSetterMedicines);
        getterSetterMedicines.setAllValues(2005, "Udiliv", "tablets", 2,1,50);
        this.insertInM(getterSetterMedicines);
        getterSetterMedicines.setAllValues(2006, "Mashyne", "capsules", 2,3,50);
        this.insertInM(getterSetterMedicines);
    }
    public void insertsomeentriesinH(){
        GetterSetterHistory getterSetterHistory = new GetterSetterHistory();
        getterSetterHistory.setAllValues(5001,"Mr.Tushar Chaudhari","2017-4-22",5,"1234560789");
        this.insertInH(getterSetterHistory);
        getterSetterHistory.setAllValues(5002,"Ms.Poonam Chaudhari","2017-4-18",6,"4659138027");
        this.insertInH(getterSetterHistory);
    }
    public void insertsomeentriesinCT(){
        GetterSetterCurrentTreatments getterSetterCurrentTreatments = new GetterSetterCurrentTreatments();
        getterSetterCurrentTreatments.setAllValues(7001,"Mr.Amit Patil",
                "amit_fcm_key","1995-5-4","1923764850");
        this.insertInCT(getterSetterCurrentTreatments);
        getterSetterCurrentTreatments.setAllValues(7002,"Ms.Aparna Patil",
                "aparna_fcm_key","1998-5-9","7801462943");
        this.insertInCT(getterSetterCurrentTreatments);
    }
    public Cursor getAllPA(){
        String getDetails="SELECT "+COL_PA_ID+","+COL_PA_NAME+" FROM "+TABLE_PENDING_APPOINTMENT+";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(getDetails,null);
        return cursor;
    }
    public Cursor getAllCT(){
        String getDetails="SELECT "+COL_CT_ID+","+COL_CT_NAME+" FROM "+TABLE_CURRENT_TREATMENT+";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(getDetails,null);
        return cursor;
    }
    public Cursor getAllM(){
        String getDetails="SELECT "+COL_M_ID+","+COL_M_NAME+" FROM "+TABLE_MEDICINES+";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(getDetails,null);
        return cursor;
    }
    public Cursor getAllH(){
        String getDetails="SELECT "+COL_H_ID+","+COL_H_NAME+" FROM "+TABLE_HISTORY+";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(getDetails,null);
        return cursor;
    }
    public Cursor getAllA(){
        String getDetails="SELECT "+COL_A_PAT_ID+","+COL_A_PAT_NAME+" FROM "+TABLE_APPOINTMENTS+";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(getDetails,null);
        return cursor;
    }
    public void moveFromPendingAppointmentToAppointments(int id)
    {
        String deletequery="DELETE FROM "+ TABLE_PENDING_APPOINTMENT+ " WHERE "+COL_PA_ID+" = "+id+";";
        String detailsquery="SELECT * FROM "+TABLE_PENDING_APPOINTMENT+" WHERE "+COL_PA_ID+" = "+id+";";
        GetterSetterAppointments getterSetterAppointments = new GetterSetterAppointments();
        SQLiteDatabase dbr = getReadableDatabase();
        SQLiteDatabase dbw = getWritableDatabase();
        Cursor cursor = dbr.rawQuery(detailsquery,null);
        if(cursor!=null)
        {
            //id name key date ss
            cursor.moveToFirst();
            getterSetterAppointments.setPat_id(cursor.getInt(cursor.getColumnIndex(COL_PA_ID)));
            getterSetterAppointments.setPat_name(cursor.getString(cursor.getColumnIndex(COL_PA_NAME)));
            getterSetterAppointments.setPat_app_date(cursor.getString(cursor.getColumnIndex(COL_PA_DATE)));
            getterSetterAppointments.setPat_ss(cursor.getString(cursor.getColumnIndex(COL_PA_SS)));
            getterSetterAppointments.setPat_fcm(cursor.getString(cursor.getColumnIndex(COL_PA_FCM_KEY)));
            insertInA(getterSetterAppointments);
            dbw.execSQL(deletequery);
        }
        if(cursor!=null) {
            cursor.close();
            dbr.close();
            dbw.close();
        }
    }
    public void moveFromAppointmentToCurrentTreatment(int id)
    {
        String deletequery="DELETE FROM "+ TABLE_APPOINTMENTS+ " WHERE "+COL_A_PAT_ID+" = "+id+";";
        String detailsquery="SELECT * FROM "+TABLE_APPOINTMENTS+" WHERE "+COL_A_PAT_ID+" = "+id+";";
        GetterSetterCurrentTreatments getterSetterCurrentTreatments = new GetterSetterCurrentTreatments();
        SQLiteDatabase dbr = getReadableDatabase();
        SQLiteDatabase dbw = getWritableDatabase();
        Cursor cursor = dbr.rawQuery(detailsquery,null);
        if(cursor!=null)
        {
            //id name key date ss
            cursor.moveToFirst();
            getterSetterCurrentTreatments.setPatient_id(cursor.getInt(cursor.getColumnIndex(COL_A_PAT_ID)));
            getterSetterCurrentTreatments.setPatient_name(cursor.getString(cursor.getColumnIndex(COL_A_PAT_NAME)));
            getterSetterCurrentTreatments.setPatient_fcm_key(cursor.getString(cursor.getColumnIndex(COL_A_PAT_FCM)));
            getterSetterCurrentTreatments.setPatient_start_date(cursor.getString(cursor.getColumnIndex(COL_A_DATE)));
            getterSetterCurrentTreatments.setPatient_symptom_string(cursor.getString(cursor.getColumnIndex(COL_A_SS)));
            insertInCT(getterSetterCurrentTreatments);
            dbw.execSQL(deletequery);
        }
        if(cursor!=null) {
            cursor.close();
        }
    }
    public void moveFromCurrentTreatmentToHistory(int id){
        String deletequery="DELETE FROM "+ TABLE_CURRENT_TREATMENT+ " WHERE "+COL_CT_ID+" = "+id+";";
        String detailsquery="SELECT * FROM "+TABLE_CURRENT_TREATMENT+" WHERE "+COL_CT_ID+" = "+id+";";
        GetterSetterHistory getterSetterHistory = new GetterSetterHistory();
        SQLiteDatabase dbr = getReadableDatabase();
        SQLiteDatabase dbw = getWritableDatabase();
        Cursor cursor = dbr.rawQuery(detailsquery,null);
        if(cursor!=null)
        {
            //id name key date ss
            cursor.moveToFirst();
            getterSetterHistory.setHistory_id(cursor.getInt(cursor.getColumnIndex(COL_CT_ID)));
            getterSetterHistory.setPatient_name(cursor.getString(cursor.getColumnIndex(COL_CT_NAME)));
            getterSetterHistory.setTreatment_start_date(cursor.getString(cursor.getColumnIndex(COL_CT_START_DATE)));
            getterSetterHistory.setTreatment_Duration(9);
            getterSetterHistory.setPatient_ss(cursor.getString(cursor.getColumnIndex(COL_CT_SS)));
            insertInH(getterSetterHistory);
            dbw.execSQL(deletequery);
        }
        if(cursor!=null) {
            cursor.close();
            dbr.close();
            dbw.close();
        }
    }
    public void removefromPA(int id)
    {
        String deletequery="DELETE FROM "+ TABLE_PENDING_APPOINTMENT+ " WHERE "+COL_PA_ID+" = "+id+";";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(deletequery);
        db.close();
    }
    public void removefromA(int id)
    {
        String deletequery="DELETE FROM "+ TABLE_APPOINTMENTS+ " WHERE "+COL_A_PAT_ID+" = "+id+";";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(deletequery);
        db.close();
    }
    public void removefromCT(int id)
    {
        String deletequery="DELETE FROM "+ TABLE_CURRENT_TREATMENT+ " WHERE "+COL_CT_ID+" = "+id+";";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(deletequery);
        db.close();
    }
    public void updatedateinPA(String date,int id)
    {
        String query = "UPDATE "+TABLE_PENDING_APPOINTMENT+" SET "+
                COL_PA_DATE+" = '"+date+
                "' WHERE "+COL_PA_ID+" = "+id+";"
                ;
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
        db.close();
    }
    public void updatehrinPA(int hr,int id)
    {
        String query = "UPDATE "+TABLE_PENDING_APPOINTMENT+" SET "+
                COL_PA_HR+" = "+hr+
                " WHERE "+COL_PA_ID+" = "+id+";"
                ;
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
        db.close();
    }
    public void updatemininPA(int min,int id)
    {
        String query = "UPDATE "+TABLE_PENDING_APPOINTMENT+" SET "+
                COL_PA_MIN+" = "+min+
                " WHERE "+COL_PA_ID+" = "+id+";"
                ;
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
        db.close();
    }
    /*public void updatedateinA(String date,int id)
    {
        String query = "UPDATE "+TABLE_APPOINTMENTS+" SET "+
                COL_A_DATE+" = '"+date+
                "' WHERE "+COL_A_PAT_ID+" = "+id+";"
                ;
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
    }
    public void updatehrinA(int hr,int id)
    {
        String query = "UPDATE "+TABLE_APPOINTMENTS+" SET "+
                COL_A_APPHR+" = "+hr+
                " WHERE "+COL_A_PAT_ID+" = "+id+";"
                ;
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
    }
    public void updatemininA(int min,int id)
    {
        String query = "UPDATE "+TABLE_APPOINTMENTS+" SET "+
                COL_A_APPMIN+" = "+min+
                " WHERE "+COL_A_PAT_ID+" = "+id+";"
                ;
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
    }*/
    public void updateDosageinM(int dosage, int id)
    {
        {
            String query = "UPDATE "+TABLE_MEDICINES+" SET "+
                    COL_M_DOSAGE+" = "+dosage+
                    " WHERE "+COL_M_ID+" = "+id+";"
                    ;
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL(query);
            db.close();
        }
    }
    public void updateTiminginM(int timing,int id)
    {
        String query = "UPDATE "+TABLE_MEDICINES+" SET "+
                COL_M_TIMING+" = "+timing+
                " WHERE "+COL_M_ID+" = "+id+";"
                ;
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
        db.close();
    }
    public Cursor getAllMedsIds(){
        String query = "SELECT "+COL_M_ID+" FROM "+TABLE_MEDICINES;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        return cursor;
    }
    public GetterSetterAppointments getCurrentPatient(){
        SQLiteDatabase db = getReadableDatabase();
        List<temp> tempList = new ArrayList<temp>();
        String getDate="SELECT "+COL_A_APPHR+","+COL_A_APPMIN+","+COL_A_PAT_ID+",MIN("+COL_A_DATE+") FROM "+TABLE_APPOINTMENTS+";";
        Cursor cursor1 = db.rawQuery(getDate,null);
        if(cursor1!=null)
        {
            cursor1.moveToFirst();
            while(!cursor1.isAfterLast()){
                Log.d("DATABASE HELPER"," in main loop");
                temp t = new temp();
                t.setHr(cursor1.getInt(cursor1.getColumnIndex(COL_A_APPHR)));
                t.setMin(cursor1.getInt(cursor1.getColumnIndex(COL_A_APPMIN)));
                t.setId(cursor1.getInt(cursor1.getColumnIndex(COL_A_PAT_ID)));
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

