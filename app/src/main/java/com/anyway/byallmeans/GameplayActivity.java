package com.anyway.byallmeans;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;

import java.util.Random;

public class GameplayActivity extends Activity {
    private static final String KEY_GAME_THREAD = "GAME_THREAD";
    private static final String TAG = "GameplayActivity";
    GameThread gameThread;

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

        Button button1 = (Button) findViewById(R.id.button1);
        Button button2 = (Button) findViewById(R.id.button2);
        Button button3 = (Button) findViewById(R.id.button3);
        Button button4 = (Button) findViewById(R.id.button4);
        Button button5 = (Button) findViewById(R.id.button5);
        Button newProject = (Button) findViewById(R.id.buttonNewProject);
        Button acceptProject = (Button) findViewById(R.id.buttonAcceptProject);
        Button hireEmployee = (Button) findViewById(R.id.hireEmployee);
        Button fireEmployee = (Button) findViewById(R.id.fireEmployee);

        button1.setText("stop");
        button2.setText("start");
        button3.setText("x1");
        button4.setText("x2");
        button5.setText("x4");
        newProject.setText("New project");
        acceptProject.setText("Delete project");
        hireEmployee.setText("Hire employee");
        fireEmployee.setText("Fire employee");

        button1.setOnClickListener(e -> gameThread.pause());
        button2.setOnClickListener(e -> gameThread.unpause());
        button3.setOnClickListener(e -> gameThread.setSpeed(GameThread.GameSpeed.NORMAL));
        button4.setOnClickListener(e -> gameThread.setSpeed(GameThread.GameSpeed.FAST));
        button5.setOnClickListener(e -> gameThread.setSpeed(GameThread.GameSpeed.FASTEST));
        newProject.setOnClickListener(e -> {
            String[] titles = {"Call of Duty", "SERZH", "TRiTPO", "MATAN", "Gradle", "Android Studio"};
            Random rand = new Random();
            gameThread.startNewProject(titles[rand.nextInt(6)], rand.nextInt(300) + 100);
        });
        acceptProject.setOnClickListener(e -> gameThread.acceptProject());

        hireEmployee.setOnClickListener(e -> {
            String[] names = {"Vasya", "Petya", "Kostya", "Pavel"};
            Random rand = new Random();
            gameThread.hireEmployee(names[rand.nextInt(4)], rand.nextInt(10), rand.nextInt(2000));
        });
        fireEmployee.setOnClickListener(e -> gameThread.fireEmployee());

        if (savedInstanceState == null) {
            Log.d(TAG, "onCreate: creating new GameThread object");
            gameThread = new GameThread();
            gameThread.configureUI(this, new Handler());
            gameThread.start();
        }
    }
}
