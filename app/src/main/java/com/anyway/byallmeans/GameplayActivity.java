package com.anyway.byallmeans;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.ViewFlipper;

import java.util.Random;

public class GameplayActivity extends Activity {
    private static final String TAG = "GameplayActivity";
    private GameThread gameThread;
    private ListView employeeList;

    @Override
    protected void onPause() {
        gameThread.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameThread.unpause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay);

        employeeList = (ListView) findViewById(R.id.employeesListView);

        ViewFlipper viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        viewFlipper.setDisplayedChild(1);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radioInfo:
                    viewFlipper.setDisplayedChild(0);
                    break;
                case R.id.radioStaff:
                    viewFlipper.setDisplayedChild(1);
                    break;
                case R.id.radioBank:
                    viewFlipper.setDisplayedChild(2);
                    break;
            }
        });

        Button button4 = (Button) findViewById(R.id.button4);
        Button button5 = (Button) findViewById(R.id.button5);

        button4.setOnClickListener(e -> gameThread.pause());
        button5.setOnClickListener(e -> gameThread.unpause());

        if (savedInstanceState == null) {
            gameThread = new GameThread();
            gameThread.configureUI(this, new Handler());
            gameThread.start();
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public void hireOnClick(View view) {
        String[] names = {"Vasya", "Petya", "Kostya", "Pavel","Sanya","Jenia"};
        Random rand = new Random();
        int skill = rand.nextInt(10)+1;
        int salary = skill*100+skill*(rand.nextInt(50));
        gameThread.hireEmployee(names[rand.nextInt(6)], skill, salary);
    }

    public void fireOnClick(View view) {
        int position = employeeList.getPositionForView(view);
        gameThread.fireEmployee(position);
    }

    public void sendToTraining(View view) {
        int position = employeeList.getPositionForView(view);
        gameThread.sendToTraining(position);
    }

    public void increaseSalary(View view) {
        int position = employeeList.getPositionForView(view);
        gameThread.increaseSalary(position);
    }

    public void decreaseSalary(View view) {
        int position = employeeList.getPositionForView(view);
        gameThread.decreaseSalary(position);
    }
}
