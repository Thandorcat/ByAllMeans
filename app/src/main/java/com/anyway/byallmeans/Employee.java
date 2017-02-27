package com.anyway.byallmeans;

public class Employee {
    private String name;
    private int skill;
    private int salary;

    public Employee(String name, int skill, int salary) {
        this.name = name;
        this.skill = skill;
        this.salary = salary;
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

    public int work() {
        return skill;
    }
}