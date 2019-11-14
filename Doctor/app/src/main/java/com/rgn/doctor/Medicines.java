package com.rgn.doctor;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dpizarro
 */
public class Medicines implements Parcelable {

    String medname;
    int meddosage;
    int medtimings;
    int medid;
    String type;
    private boolean selected;

    public Medicines(String medname, int meddosage, int medtimings, int medid, String type, boolean selected) {
        this.medname = medname;
        this.meddosage = meddosage;
        this.medtimings = medtimings;
        this.selected = selected;
        this.medid = medid;
        this.type=type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMedid() {
        return medid;
    }

    public void setMedid(int medid) {
        this.medid = medid;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getMedname() {
        return medname;
    }

    public void setMedname(String medname) {
        this.medname = medname;
    }

    public int getMeddosage() {
        return meddosage;
    }

    public void setMeddosage(int meddosage) {
        this.meddosage = meddosage;
    }

    public int getMedtimings() {
        return medtimings;
    }

    public void setMedtimings(int medtimings) {
        this.medtimings = medtimings;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.medname);
        dest.writeInt(this.meddosage);
        dest.writeInt(this.medtimings);
        dest.writeByte(selected ? (byte) 1 : (byte) 0);
        dest.writeInt(this.medid);
        dest.writeString(this.type);
    }

    private Medicines(Parcel in) {
        this.medname = in.readString();
        this.meddosage = in.readInt();
        this.medtimings = in.readInt();
        this.selected = in.readByte() != 0;
        this.medid = in.readInt();
        this.type= in.readString();
    }

    public static final Parcelable.Creator<Medicines> CREATOR = new Parcelable.Creator<Medicines>() {
        public Medicines createFromParcel(Parcel source) {
            return new Medicines(source);
        }

        public Medicines[] newArray(int size) {
            return new Medicines[size];
        }
    };
}
