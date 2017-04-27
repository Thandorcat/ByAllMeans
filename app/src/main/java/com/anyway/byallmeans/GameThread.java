package com.anyway.byallmeans;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private SimpleAdapter adapterHistory;
    private GameProject project;
    private List<GameProject> projectsHistory;
    private List<Map<String, Object>> projectHistoryData;
    private long balance;
    private int salaries;
    private int credit = 0;
    private int creditPayments;
    private GregorianCalendar calendar;
    private boolean running;
    private GameState state;
    private GameSpeed speed;

    private Button betabutton;
    private Button artbutton;
    private Button revbutton;
    private Button creditButton;

    private UIHolder uiHolder;

    public GameThread() {
        Log.d(TAG, "GameThread()");
        state = GameState.READY;
        speed = GameSpeed.FASTEST;

        running = true;
        monitor = new Object();
        calendar = new GregorianCalendar(2017, 0, 1);
        uiHolder = new UIHolder();
        projectsHistory = new ArrayList<>();
        employeeManager = new EmployeeManager();
        state = GameState.RUNNING;
        balance = 50000;
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
                        salaries = 0;
                        for (Employee employee :
                                employeeManager.getAllEmployees()) {
                            balance -= employee.getSalary();
                            salaries += employee.getSalary();
                        }

                        if (credit != 0) {
                            if (creditPayments < 5) {
                                balance -= credit;
                                creditPayments++;
                            } else {
                                balance -= credit;
                                credit = 0;
                            }
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
        employeeManager.sendToTraining(index);
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
                uiHolder.updateProjectHistory();
                uiHolder.switchProjectView(0);
                project = null;
            }
        }
    }

    public void startNewProject(String title, int workAmount, int income) {
        synchronized (monitor) {
            if (project == null) {
                project = new GameProject(title, workAmount, income);
                uiHolder.updateProjectInfo();
                uiHolder.switchProjectView(1);
                betabutton.setEnabled(true);
            }
        }
    }

    public void increaseSalary(int position) {
        int value = 50;
        employeeManager.changeSalary(position, value);
    }

    public void decreaseSalary(int position) {
        int value = -50;
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
                    returnValue = 1;
                    break;
                case FAST:
                    returnValue = 2;
                    break;
                case FASTEST:
                    returnValue = 4;
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            return returnValue;
        }
    }

    private class UIHolder {
        private TextView dateTextView;
        private TextView balanceBankView;
        private TextView bankSalariesView;
        private TextView balanceTextView;
        private TextView creditSummView;
        private TextView creditMonthView;
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

            projectHistoryData = new ArrayList<>();
            dateTextView = (TextView) activity.findViewById(R.id.textDate);
            balanceTextView = (TextView) activity.findViewById(R.id.textBalance);
            balanceBankView = (TextView) activity.findViewById(R.id.bankBalanceView);
            bankSalariesView = (TextView) activity.findViewById(R.id.bankSalariesView);
            creditSummView = (TextView) activity.findViewById(R.id.creditSumm);
            creditMonthView = (TextView) activity.findViewById(R.id.creditMonth);
            ListView listView = (ListView) activity.findViewById(R.id.employeesListView);
            ListView listView2 = (ListView) activity.findViewById(R.id.employeesUnavailableListView);
            ListView listViewProjectHistory = (ListView) activity.findViewById(R.id.projectHistoryListView);
            projectViewSwitcher = (ViewSwitcher) activity.findViewById(R.id.project_view_switcher);
            projectNameTextView = (TextView) activity.findViewById(R.id.project_name);
            projectIncomeTextView = (TextView) activity.findViewById(R.id.project_income);
            projectProgressProgressBar = (ProgressBar) activity.findViewById(R.id.project_progress);
            startNewProjectTextView = (TextView) activity.findViewById(R.id.new_project_text_view);
            startNewProjectTextView.setOnClickListener(v -> {
                String[] titles = {"Call of Duty", "SERZH Inc.", "TRiTPO", "MATAN", "Gradle", "Android Studio", "Full-Life", "World of cats", "New PHP", "Doing nothing", "Hot Chickens"};
                Random rand = new Random();
                int work = rand.nextInt(1500) + 100;
                int income = work * 5 + (rand.nextInt(3)) * work;
                startNewProject(titles[rand.nextInt(10)], work, income);
            });

            betabutton = (Button) activity.findViewById(R.id.button4);
            artbutton = (Button) activity.findViewById(R.id.button5);
            revbutton = (Button) activity.findViewById(R.id.button6);
            creditButton = (Button) activity.findViewById(R.id.creditButton);

            betabutton.setOnClickListener(e -> openBetatest());
            artbutton.setOnClickListener(e -> postArticle());
            revbutton.setOnClickListener(e -> postReview());
            creditButton.setOnClickListener(e -> takeCredit());

            adapter = new SimpleAdapter(activity,
                    employeeManager.getAvailableEmployeesData(),
                    R.layout.employee_view,
                    new String[]{"Name", "Skill", "Salary"},
                    new int[]{R.id.employeeViewNameTextView,
                            R.id.employeeViewSkillTextView,
                            R.id.employeeViewSalaryTextView});
            adapter2 = new SimpleAdapter(activity,
                    employeeManager.getUnavailableEmployeesData(),
                    R.layout.employee_view_unavailable,
                    new String[]{"Name", "Skill", "Salary", "Days Left"},
                    new int[]{R.id.employeeViewNameTextView,
                            R.id.employeeViewSkillTextView,
                            R.id.employeeViewSalaryTextView,
                            R.id.daysLeftTextView});
            adapterHistory = new SimpleAdapter(activity,
                    projectHistoryData,
                    R.layout.project_view,
                    new String[]{"Name", "Income", "Date Finished"},
                    new int[]{R.id.project_view_project_name,
                            R.id.project_view_project_income,
                            R.id.project_view_date_finished});
            listView.setAdapter(adapter);
            listView2.setAdapter(adapter2);
            listViewProjectHistory.setAdapter(adapterHistory);
        }

        private void showMessage(String s) {
            handler.post(() -> Toast.makeText(context, s, Toast.LENGTH_SHORT).show());
        }

        private void openBetatest() {
            Random rand = new Random();
            int profit = project.getIncome();
            profit = (profit * (rand.nextInt(50) + 80)) / 100;
            project.setIncome(profit);
            project.setWork((int) (project.getWork() * 1.15));
            uiHolder.updateProjectInfo();
            betabutton.setEnabled(false);
        }

        private void postArticle() {
            balance -= 1000;
            int profit = project.getIncome();
            profit = (profit * 115) / 100;
            project.setIncome(profit);
            uiHolder.updateProjectInfo();
        }

        private void postReview() {
            balance -= 5000;
            int profit = project.getIncome();
            profit = (profit * 140) / 100;
            project.setIncome(profit);
            uiHolder.updateProjectInfo();
        }

        private void takeCredit() {
            credit = ((int) (balance * 2) + salaries * 2);
            balance += credit;
            credit = (credit * 115) / 100 / 6;
            creditPayments = 0;
            creditButton.setEnabled(false);
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
                balanceBankView.setText(balanceText);
                bankSalariesView.setText("Salaries: " + salaries + "$/month");
                if (credit == 0) {
                    int creditsize = (int) (balance * 2) + salaries * 2;
                    creditSummView.setText(creditsize + "$ for 6 months");
                    int monthPayment = (creditsize * 115) / 100 / 6;
                    creditMonthView.setText(monthPayment + "$/month");
                    creditButton.setEnabled(true);
                } else {
                    creditSummView.setText((6 - creditPayments) + " payments left");
                    creditMonthView.setText(credit + "$/month");
                }
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

        public void updateProjectHistory() {
            Map<String, Object> m = new HashMap<>();
            m.put("Name", project.getTitle());
            m.put("Income", "$" + project.getIncome());
            m.put("Date Finished", String.format("%d/%02d/%4d",
                    calendar.get(MONTH) + 1,
                    calendar.get(DAY_OF_MONTH),
                    calendar.get(YEAR)));
            projectHistoryData.add(m);
            handler.post(() -> adapterHistory.notifyDataSetChanged());
        }
    }
}