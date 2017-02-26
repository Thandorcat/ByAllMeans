package com.anyway.byallmeans;

import android.app.Activity;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
    ArrayList<Map<String, Object>> employeesData;
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
    private ViewSwitcher projectViewSwitcher;
    private TextView projectName;
    private TextView projectIncome;
    private ProgressBar projectProgress;
    private TextView startNewProjectTextView;
    private GameSpeed speed;
    private SimpleAdapter adapter;

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
        employeesData = new ArrayList<>();
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
        ListView listView = (ListView) activity.findViewById(R.id.employeesListView);
        projectViewSwitcher = (ViewSwitcher) activity.findViewById(R.id.project_view_switcher);
        projectName = (TextView) activity.findViewById(R.id.project_name);
        projectIncome = (TextView) activity.findViewById(R.id.project_income);
        projectProgress = (ProgressBar) activity.findViewById(R.id.project_progress);
        startNewProjectTextView = (TextView) activity.findViewById(R.id.new_project_text_view);
        startNewProjectTextView.setOnClickListener(v -> {
            String[] titles = {"Call of Duty", "SERZH", "TRiTPO", "MATAN", "Gradle", "Android Studio"};
            Random rand = new Random();
            startNewProject(titles[rand.nextInt(6)], rand.nextInt(300) + 100, rand.nextInt(3000) + 1000);
        });
        adapter = new SimpleAdapter(activity, employeesData, R.layout.employee_view, new String[]{"Name", "Skill", "Salary"}, new int[]{R.id.employeeView_NameTextView, R.id.employeeView_skillTextView, R.id.employeeView_salaryTextView});
        listView.setAdapter(adapter);
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


                    if (project != null) {
                        workToDo = 0;
                        for (Employee employee :
                                employees) {
                            workToDo += employee.work();
                        }
                        project.doWork(workToDo);
                        projectProgress.setProgress(project.getProgressPercent());

                        if (project.isFinished()) {
                            acceptProject();
                        }

                    }

                    handler.post(() -> {
                        text1.setText(String.format("%d/%02d/%4d", calendar.getMonth(), calendar.getDay(), calendar.getYear()));
                        text2.setText(String.format("$%d", balance));
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
            Map<String, Object> m = new HashMap<>();
            m.put("Name", name);
            m.put("Skill", skill);
            m.put("Salary", salary);
            employeesData.add(m);
            adapter.notifyDataSetChanged();
        }
    }

    public void fireEmployee() {
        Log.d(TAG, "fireEmployee()");
        synchronized (monitor) {
            if (employees.size() > 0) {
                employees.remove(0);
                employeesData.remove(0);
                adapter.notifyDataSetChanged();
            }
        }
    }

    public void acceptProject() {
        Log.d(TAG, "acceptProject()");
        if (project != null && project.isFinished()) {
            balance += project.getIncome();
            projectsHistory.add(project);
            project = null;
            handler.post(() -> projectViewSwitcher.setDisplayedChild(0));
        }


    }

    public void startNewProject(String title, int workAmount, int income) {
        Log.d(TAG, "startNewProject(String title, int workAmount)");
        if (project == null) {
            project = new GameProject(title, workAmount, income);
            projectName.setText(project.getTitle());
            projectIncome.setText(String.format("$%d", project.getIncome()));
            projectProgress.setProgress(0);
            projectViewSwitcher.setDisplayedChild(1);

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