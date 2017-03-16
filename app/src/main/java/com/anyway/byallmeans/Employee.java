package com.anyway.byallmeans;

public class Employee {
    private String name;
    private int skill;
    private int salary;
    private int daysLeft;

    public Employee(String name, int skill, int salary) {
        this.name = name;
        this.skill = skill;
        this.salary = salary;
    }

    public int getDaysLeft() {
        return daysLeft;
    }

    public void setDaysLeft(int daysLeft) {
        this.daysLeft = daysLeft;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int newValue) {
        this.salary = newValue;
    }

    public String getName() {
        return name;
    }
    public void addSkill() { skill++;}

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

    public void setSkill(int skill) {
        this.skill = skill;
    }

    public int work() {
        return skill;
    }

    public void increaseSalary() {
    }
}