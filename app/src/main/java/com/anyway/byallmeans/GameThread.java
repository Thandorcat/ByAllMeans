package com.anyway.byallmeans;

import android.app.Activity;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GameThread extends Thread implements Parcelable {

    public static final Parcelable.Creator<GameThread> CREATOR = new Parcelable.Creator<GameThread>() {
        @Override
        public GameThread createFromParcel(Parcel in) {
            return new GameThread(in);
        }

        @Override
        public GameThread[] newArray(int size) {
            return new GameThread[size];
        }
    };
    private static final String TAG = "GameThread";
    private final Object monitor;
    private int tickerMonth;
    private int tickerWeek;
    private GameProject project;
    private List<GameProject> projectsHistory;
    private List<Employee> employees;
    private long balance;
    private Calendar calendar;
    private TextView text1;
    private TextView text2;
    private boolean running;
    private GameState state;
    private GameSpeed speed;

    private Handler handler;

    public GameThread() {
        Log.d(TAG, "GameThread()");
        state = GameState.READY;
        speed = GameSpeed.NORMAL;

        running = true;
        monitor = new Object();
        calendar = new Calendar();
        projectsHistory = new ArrayList<>();
        employees = new ArrayList<>();

        //TODO implement doStart() method instead
        state = GameState.RUNNING;
        balance = 999999;
        project = new GameProject("Game Dev Tycoon", 350);
    }

    protected GameThread(Parcel in) {
        Log.d(TAG, "GameThread: loaded from Parcel");
        monitor = new Object();
        tickerMonth = in.readInt();
        tickerWeek = in.readInt();
        project = (GameProject) in.readValue(GameProject.class.getClassLoader());
        if (in.readByte() == 0x01) {
            projectsHistory = new ArrayList<>();
            in.readList(projectsHistory, GameProject.class.getClassLoader());
        } else {
            projectsHistory = null;
        }
        if (in.readByte() == 0x01) {
            employees = new ArrayList<>();
            in.readList(employees, Employee.class.getClassLoader());
        } else {
            employees = null;
        }
        balance = in.readLong();
        calendar = (Calendar) in.readValue(Calendar.class.getClassLoader());
        running = in.readByte() != 0x00;
        state = (GameState) in.readValue(GameState.class.getClassLoader());
        speed = (GameSpeed) in.readValue(GameSpeed.class.getClassLoader());
    }

    public void configureUI(Activity activity, Handler handler) {
        this.handler = handler;
        text1 = (TextView) activity.findViewById(R.id.textDate);
        text2 = (TextView) activity.findViewById(R.id.textBalance);
    }

    public void setSpeed(GameSpeed speed) {
        Log.d(TAG, "setSpeed(GameSpeed speed)");
        synchronized (monitor) {
            this.speed = speed;
        }
    }

    public void setRunning(boolean running) {
        Log.d(TAG, "setRunning(boolean running)");
        this.running = running;
    }

    @Override
    public void run() {
        long timeStart;
        long timeEnd;
        long timeElapsed;
        long timeToSleep;

        int workToDo;


        while (running) {
            synchronized (monitor) {

                timeStart = System.currentTimeMillis();

                if (state == GameState.RUNNING) {

                    calendar.tick();

                    tickerMonth++;
                    if (tickerMonth == 7) {
                        tickerMonth = 0;
                        for (Employee employee :
                                employees) {
                            balance -= employee.getSalary();
                        }
                    }

                    StringBuilder projectInfo = new StringBuilder();

                    if (project != null) {
                        workToDo = 0;

                        projectInfo.append(String.format("project: %s\nprogress: %.2f%%\nfinished: %b\n",
                                project.getTitle(),
                                project.getProgress() * 100,
                                project.isFinished()));

                        for (Employee employee :
                                employees) {
                            workToDo += employee.work();
                        }
                        project.doWork(workToDo);
                    } else {
                        projectInfo.append("No active project\n");
                    }

                    for (Employee employee :
                            employees) {
                        projectInfo.append(employee.toString());
                        projectInfo.append('\n');
                    }

                    handler.post(() -> {
                        text1.setText(projectInfo.toString());
                        text2.setText(
                                String.format("balance:%d\nyear:%4d\nmonth:%2d\nday:%2d",
                                        balance,
                                        calendar.getYear(),
                                        calendar.getMonth(),
                                        calendar.getDay()));
                    });
                }
            }

            timeEnd = System.currentTimeMillis();
            timeElapsed = timeEnd - timeStart;
            timeToSleep = 1_000 / speed.value() - timeElapsed;

            if (timeToSleep > 0) {
                try {
                    sleep(timeToSleep);
                } catch (InterruptedException e) {
                    Log.e(this.TAG, "run: sleep: interrupted", e);
                }
            }
        }
    }

    public void pause() {
        Log.d(TAG, "pause()");
        synchronized (monitor) {
            state = GameState.PAUSED;
        }

    }

    public void unpause() {
        Log.d(TAG, "unpause()");
        synchronized (monitor) {
            state = GameState.RUNNING;
        }

    }

    public void hireEmployee(String name, int skill, int salary) {
        Log.d(TAG, "hireEmployee(String name, int skill, int salary)");
        synchronized (monitor) {
            employees.add(new Employee(name, skill, salary));
        }
    }

    public void fireEmployee() {
        Log.d(TAG, "fireEmployee()");
        synchronized (monitor) {
            if (employees.size() > 0) {
                employees.remove(0);
            }
        }
    }

    public void acceptProject() {
        Log.d(TAG, "acceptProject()");
        synchronized (monitor) {
            if (project != null && project.isFinished()) {
                projectsHistory.add(project);
                project = null;
            }
        }

    }

    public void startNewProject(String title, int workAmount) {
        Log.d(TAG, "startNewProject(String title, int workAmount)");
        synchronized (monitor) {
            if (project == null) {
                project = new GameProject(title, workAmount);
            }
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Log.d(TAG, "writeToParcel: saving to Parcel");
        dest.writeInt(tickerMonth);
        dest.writeInt(tickerWeek);
        dest.writeValue(project);
        if (projectsHistory == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(projectsHistory);
        }
        if (employees == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(employees);
        }
        dest.writeLong(balance);
        dest.writeValue(calendar);
        dest.writeByte((byte) (running ? 0x01 : 0x00));
        dest.writeValue(state);
        dest.writeValue(speed);
    }

    public enum GameState {
        READY, RUNNING, PAUSED, FINISHED
    }

    public enum GameSpeed {
        NORMAL, FAST, FASTEST;

        public int value() {
            int returnValue = 0;
            switch (GameSpeed.this) {
                case NORMAL:
                    returnValue = 2;
                    break;
                case FAST:
                    returnValue = 4;
                    break;
                case FASTEST:
                    returnValue = 8;
                    break;
                default:
                    throw new IllegalArgumentException();

            }
            return returnValue;
        }
    }
}