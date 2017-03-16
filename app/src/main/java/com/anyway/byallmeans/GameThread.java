package com.anyway.byallmeans;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

public class GameThread extends Thread {

    private static final String TAG = "GameThread";
    private final Object monitor;
    private EmployeeManager employeeManager;
    private SimpleAdapter adapter;
    private SimpleAdapter adapter2;
    private GameProject project;
    private List<GameProject> projectsHistory;
    private long balance;
    private GregorianCalendar calendar;
    private boolean running;
    private GameState state;
    private GameSpeed speed;

    private UIHolder uiHolder;

    public GameThread() {
        Log.d(TAG, "GameThread()");
        state = GameState.READY;
        speed = GameSpeed.FASTEST;

        running = true;
        monitor = new Object();
        calendar = new GregorianCalendar(1990, 0, 1);
        uiHolder = new UIHolder();
        projectsHistory = new ArrayList<>();
        employeeManager = new EmployeeManager();
        state = GameState.RUNNING;
        balance = 999999;
    }

    public void configureUI(Activity activity, Handler handler) {
        uiHolder.configure(activity, handler);
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

                    calendar.add(DAY_OF_MONTH, 1);

                    int day = calendar.get(DAY_OF_MONTH);
                    if (day == 1) {
                        for (Employee employee :
                                employeeManager.getAllEmployees()) {
                            balance -= employee.getSalary();
                        }
                    }

                    if (project != null) {
                        workToDo = 0;
                        for (Employee employee :
                                employeeManager.getAvailableEmployees()) {
                            workToDo += employee.work();
                        }
                        project.doWork(workToDo);

                        if (project.isFinished()) {
                            acceptProject();
                            employeeManager.addSkill();
                        }

                    }

                    employeeManager.updateTraining();

                    uiHolder.update();
                }
            }

            timeEnd = System.currentTimeMillis();
            timeElapsed = timeEnd - timeStart;
            timeToSleep = 1_000 / speed.value() - timeElapsed;

            if (timeToSleep > 0) {
                try {
                    sleep(timeToSleep);
                } catch (InterruptedException e) {
                    Log.e(TAG, "run: sleep: interrupted", e);
                }
            }
        }
    }

    public void pause() {
        synchronized (monitor) {
            state = GameState.PAUSED;
        }

    }

    public void unpause() {
        synchronized (monitor) {
            state = GameState.RUNNING;
        }

    }

    public void hireEmployee(String name, int skill, int salary) {
        synchronized (monitor) {
            employeeManager.add(new Employee(name, skill, salary));
            adapter.notifyDataSetChanged();
        }
    }

    public void sendToTraining(int index) {
        int duration = new Random().nextInt(30) + 30;
        employeeManager.sendToTraining(index, duration);
        adapter.notifyDataSetChanged();
        adapter2.notifyDataSetChanged();
    }

    public void fireEmployee(int index) {
        synchronized (monitor) {
            if (employeeManager.getAvailableEmployees().size() > index) {
                employeeManager.remove(index);
                adapter.notifyDataSetChanged();
            }
        }
    }

    public void acceptProject() {
        synchronized (monitor) {
            if (project != null && project.isFinished()) {
                balance += project.getIncome();
                projectsHistory.add(project);
                project = null;
                uiHolder.switchProjectView(0);
            }
        }
    }

    public void startNewProject(String title, int workAmount, int income) {
        synchronized (monitor) {
            if (project == null) {
                project = new GameProject(title, workAmount, income);
                uiHolder.updateProjectInfo();
                uiHolder.switchProjectView(1);
            }
        }
    }

    public void increaseSalary(int position) {
        int value = 10;
        employeeManager.changeSalary(position, value);
    }

    public void decreaseSalary(int position) {
        int value = -10;
        employeeManager.changeSalary(position, value);
    }

    public enum GameState {
        READY, RUNNING, PAUSED,
    }

    public enum GameSpeed {
        NORMAL, FAST, FASTEST;

        public int value() {
            int returnValue;
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

    private class UIHolder {
        private TextView dateTextView;
        private TextView balanceTextView;
        private ViewSwitcher projectViewSwitcher;
        private TextView projectNameTextView;
        private TextView projectIncomeTextView;
        private ProgressBar projectProgressProgressBar;
        private TextView startNewProjectTextView;

        private Handler handler;
        private Context context;

        public void configure(Activity activity, Handler handler) {
            this.handler = handler;
            context = activity;

            dateTextView = (TextView) activity.findViewById(R.id.textDate);
            balanceTextView = (TextView) activity.findViewById(R.id.textBalance);
            ListView listView = (ListView) activity.findViewById(R.id.employeesListView);
            ListView listView2 = (ListView) activity.findViewById(R.id.employeesUnavailableListView);
            projectViewSwitcher = (ViewSwitcher) activity.findViewById(R.id.project_view_switcher);
            projectNameTextView = (TextView) activity.findViewById(R.id.project_name);
            projectIncomeTextView = (TextView) activity.findViewById(R.id.project_income);
            projectProgressProgressBar = (ProgressBar) activity.findViewById(R.id.project_progress);
            startNewProjectTextView = (TextView) activity.findViewById(R.id.new_project_text_view);
            startNewProjectTextView.setOnClickListener(v -> {
                String[] titles = {"Call of Duty", "SERZH", "TRiTPO", "MATAN", "Gradle", "Android Studio"};
                Random rand = new Random();
                int work = rand.nextInt(1000) + 100;
                int income = work * 10 + (rand.nextInt(5) + 1) * work;
                startNewProject(titles[rand.nextInt(6)], work, income);
            });
            adapter = new SimpleAdapter(activity, employeeManager.getAvailableEmployeesData(), R.layout.employee_view, new String[]{"Name", "Skill", "Salary"}, new int[]{R.id.employeeViewNameTextView, R.id.employeeViewSkillTextView, R.id.employeeViewSalaryTextView});
            adapter2 = new SimpleAdapter(activity, employeeManager.getUnavailableEmployeesData(), R.layout.employee_view_unavailable, new String[]{"Name", "Skill", "Salary", "Days Left"}, new int[]{R.id.employeeViewNameTextView, R.id.employeeViewSkillTextView, R.id.employeeViewSalaryTextView, R.id.daysLeftTextView});
            listView.setAdapter(adapter);
            listView2.setAdapter(adapter2);
        }

        private void showMessage(String s) {
            handler.post(() -> Toast.makeText(context, s, Toast.LENGTH_SHORT).show());
        }

        public void update() {
            if (project != null) {
                int progressValue = project.getProgressPercent();
                handler.post(() -> projectProgressProgressBar.setProgress(progressValue));
            }

            String dateText = String.format("%d/%02d/%4d", calendar.get(MONTH) + 1, calendar.get(DAY_OF_MONTH), calendar.get(YEAR));
            String balanceText = String.format("$%d", balance);

            handler.post(() -> {
                dateTextView.setText(dateText);
                balanceTextView.setText(balanceText);
                adapter.notifyDataSetChanged();
                adapter2.notifyDataSetChanged();
            });
        }

        public void switchProjectView(int i) {
            handler.post(() -> projectViewSwitcher.setDisplayedChild(i));
        }

        public void updateProjectInfo() {
            projectNameTextView.setText(project.getTitle());
            projectIncomeTextView.setText(String.format("$%d", project.getIncome()));
            projectProgressProgressBar.setProgress(0);
        }
    }
}