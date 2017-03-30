package com.anyway.byallmeans;

public class GameProject {
    private String title;
    private int workTotal;
    private int workLeft;
    private boolean finished;
    private int income;

    public GameProject(String title, int workTotal, int income) {
        this.title = title;
        this.workLeft = workTotal;
        this.workTotal = workTotal;
        this.income = income;
    }

    public int getIncome() {
        return income;
    }

    public void setIncome(int newIncome) {
        income = newIncome;
    }

    public int getWork() {
        return workLeft;
    }

    public void setWork(int newWork) {
        workLeft = newWork;
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

    public int getProgressPercent() {
        return (int) (getProgress() * 100);
    }

    public float getProgress() {
        return 1.0f - (float) workLeft / (float) workTotal;
    }
}