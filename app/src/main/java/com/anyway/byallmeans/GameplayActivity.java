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
        String[] names = {"Vasya", "Petya", "Kostya", "Pavel"};
        Random rand = new Random();
        gameThread.hireEmployee(names[rand.nextInt(4)], rand.nextInt(8), rand.nextInt(1500));
    }

    public void fireOnClick(View view) {
        int position = employeeList.getPositionForView(view);
        gameThread.fireEmployee(position);
    }
}
