package com.anyway.byallmeans;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Employee implements Parcelable {
    public static final Parcelable.Creator<Employee> CREATOR = new Parcelable.Creator<Employee>() {
        @Override
        public Employee createFromParcel(Parcel in) {
            return new Employee(in);
        }

        @Override
        public Employee[] newArray(int size) {
            return new Employee[size];
        }
    };
    private static final String TAG = "Employee";
    private String name;
    private int skill;
    private int salary;

    /**
     *
     * @param name
     * @param skill
     * @param salary
     */
    public Employee(String name, int skill, int salary) {
        Log.d(TAG, "Employee: created new object");
        this.name = name;
        this.skill = skill;
        this.salary = salary;
    }

    protected Employee(Parcel in) {
        Log.d(TAG, "Employee: loaded from Parcel");
        name = in.readString();
        skill = in.readInt();
        salary = in.readInt();
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int newValue) {
        Log.d(TAG, "setSalary");
        this.salary = newValue;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", skill=" + skill +
                ", salary=" + salary +
                '}';
    }

    public int getSkill() {
        return skill;
    }

    public int work() {
        //TODO value calculation based on attributes
        return skill;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Log.d(TAG, "writeToParcel: saving to Parcel");
        dest.writeString(name);
        dest.writeInt(skill);
        dest.writeInt(salary);
    }
}