package com.anyway.byallmeans;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.ViewFlipper;

import java.util.Random;

public class GameplayActivity extends Activity {
    private static final String KEY_GAME_THREAD = "GAME_THREAD";
    private static final String TAG = "GameplayActivity";
    GameThread gameThread;
    ViewFlipper viewFlipper;
    RadioGroup radioGroup;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState:               outState=" + outState);
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState: after super   outState=" + outState);
        outState.putParcelable(KEY_GAME_THREAD, gameThread);
        Log.d(TAG, "onSaveInstanceState: after my call outState=" + outState);
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        //TODO: check order
        gameThread.pause();
        super.onPause();
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "onRestart");
        super.onRestart();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        //TODO: check order
        super.onResume();
        gameThread.unpause();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState);
        gameThread = savedInstanceState.getParcelable(KEY_GAME_THREAD);
        gameThread.configureUI(this, new Handler());
        gameThread.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay);

        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        viewFlipper.setDisplayedChild(1);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
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
        Button hireEmployee = (Button) findViewById(R.id.hireEmployee);
        Button fireEmployee = (Button) findViewById(R.id.fireEmployee);

        button4.setText("STOP");
        button5.setText("START");
        hireEmployee.setText("Hire employee");
        fireEmployee.setText("Fire employee");

        button4.setOnClickListener(e -> gameThread.pause());
        button5.setOnClickListener(e -> gameThread.unpause());

        hireEmployee.setOnClickListener(e -> {
            String[] names = {"Vasya", "Petya", "Kostya", "Pavel"};
            Random rand = new Random();
            gameThread.hireEmployee(names[rand.nextInt(4)], rand.nextInt(8), rand.nextInt(1500));
        });
        fireEmployee.setOnClickListener(e -> gameThread.fireEmployee());

        if (savedInstanceState == null) {
            Log.d(TAG, "onCreate: creating new GameThread object");
            gameThread = new GameThread();
            gameThread.configureUI(this, new Handler());
            gameThread.start();
        }
    }

    public void hireOnClick(View view) {
        String[] names = {"Vasya", "Petya", "Kostya", "Pavel"};
        Random rand = new Random();
        gameThread.hireEmployee(names[rand.nextInt(4)], rand.nextInt(8), rand.nextInt(1500));
    }
}
