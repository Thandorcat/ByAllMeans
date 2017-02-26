package com.anyway.byallmeans;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class GameProject implements Parcelable {

    public static final Parcelable.Creator<GameProject> CREATOR = new Parcelable.Creator<GameProject>() {
        @Override
        public GameProject createFromParcel(Parcel in) {
            return new GameProject(in);
        }

        @Override
        public GameProject[] newArray(int size) {
            return new GameProject[size];
        }
    };
    private static final String TAG = "GameProject";
    private String title;
    private int workTotal;
    private int workLeft;
    private boolean finished;

    public GameProject(String title, int workTotal) {
        Log.d(TAG, "GameProject: created new object");
        this.title = title;
        this.workLeft = workTotal;
        this.workTotal = workTotal;
    }

    protected GameProject(Parcel in) {
        Log.d(TAG, "GameProject: loaded from Parcel");
        title = in.readString();
        workTotal = in.readInt();
        workLeft = in.readInt();
        finished = in.readByte() != 0x00;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "GameProject{" +
                "title='" + title + '\'' +
                ", workTotal=" + workTotal +
                ", workLeft=" + workLeft +
                ", finished=" + finished +
                '}';
    }

    public boolean isFinished() {
        return finished;
    }

    public void doWork(int workAmount) {
        if (isFinished()) {
            return;
        }

        workLeft -= workAmount;
        if (workLeft <= 0) {
            workLeft = 0;
            finished = true;
        }
    }

    public float getProgress() {
        return 1.0f - (float) workLeft / (float) workTotal;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Log.d(TAG, "writeToParcel: saving to Parcel");
        dest.writeString(title);
        dest.writeInt(workTotal);
        dest.writeInt(workLeft);
        dest.writeByte((byte) (finished ? 0x01 : 0x00));
    }
}