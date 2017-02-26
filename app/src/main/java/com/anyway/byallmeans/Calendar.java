package com.anyway.byallmeans;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Calendar implements Parcelable {

    public static final Parcelable.Creator<Calendar> CREATOR = new Parcelable.Creator<Calendar>() {
        @Override
        public Calendar createFromParcel(Parcel in) {
            return new Calendar(in);
        }

        @Override
        public Calendar[] newArray(int size) {
            return new Calendar[size];
        }
    };
    private static final String TAG = "Calendar";
    private final int WEEK_DAYS_VALUE = 7;
    private final int MONTH_DAYS_VALUE = 30;
    private final int YEAR_MONTH_VALUE = 12;
    private final int YEAR_DAYS_VALUE = 360;
    private int daysValue;

    public Calendar() {
        Log.d(TAG, "Calendar: created new object");
    }

    protected Calendar(Parcel in) {
        Log.d(TAG, "Calendar: loaded from Parcel");
        daysValue = in.readInt();
    }

    public void setDate(int year, int month, int day) {
        this.daysValue = (((year * YEAR_MONTH_VALUE) + month) * MONTH_DAYS_VALUE) + day;
    }

    public void setDate(int days) {
        daysValue = days;
    }

    public void tick() {
        daysValue++;
    }

    @Override
    public String toString() {
        return "Calendar{" +
                "WEEK_DAYS_VALUE=" + WEEK_DAYS_VALUE +
                ", MONTH_DAYS_VALUE=" + MONTH_DAYS_VALUE +
                ", YEAR_MONTH_VALUE=" + YEAR_MONTH_VALUE +
                ", YEAR_DAYS_VALUE=" + YEAR_DAYS_VALUE +
                ", daysValue=" + daysValue +
                ", year=" + getYear() +
                ", month=" + getMonth() +
                ", day=" + getDay() +
                '}';
    }

    public int getYear() {
        return daysValue / YEAR_DAYS_VALUE + 1970;
    }

    public int getMonth() {
        return daysValue % YEAR_DAYS_VALUE / MONTH_DAYS_VALUE + 1;
    }

    public int getDay() {
        return daysValue % YEAR_DAYS_VALUE % MONTH_DAYS_VALUE + 1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Log.d(TAG, "writeToParcel: saving to Parcel");
        dest.writeInt(daysValue);
    }
}