package com.anyway.byallmeans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class EmployeeManager {
    private List<Employee> employees;
    private List<Map<String, Object>> employeesData;
    private List<Employee> employeesUnavailable;
    private List<Map<String, Object>> employeesUnavailableData;

    public EmployeeManager() {
        employees = new ArrayList<>();
        employeesData = new ArrayList<>();
        employeesUnavailable = new ArrayList<>();
        employeesUnavailableData = new ArrayList<>();
    }

    public void add(Employee e) {
        employees.add(e);
        Map<String, Object> m = new HashMap<>();
        m.put("Name", e.getName());
        m.put("Skill", "Skill: " + e.getSkill());
        m.put("Salary", "$" + e.getSalary() + "/month");
        employeesData.add(m);
    }

    public void sendToTraining(int index, int duration) {
        Employee e = employees.remove(index);
        employeesData.remove(index);

        e.setDaysLeft(duration);
        employeesUnavailable.add(e);
        Map<String, Object> m = new HashMap<>();
        m.put("Name", e.getName());
        m.put("Skill", "Skill: " + e.getSkill());
        m.put("Salary", "$" + e.getSalary() + "/month");
        m.put("Days Left", duration);
        employeesUnavailableData.add(m);
    }

    public void remove(int index) {
        employees.remove(index);
        employeesData.remove(index);
    }

    public List<Employee> getAllEmployees() {
        List<Employee> temp = new ArrayList<>();
        temp.addAll(employees);
        temp.addAll(employeesUnavailable);
        return temp;
    }

    public List<Employee> getAvailableEmployees() {
        return employees;
    }

    public List<Employee> getUnavailableEmployees() {
        return employeesUnavailable;
    }

    public List<Map<String, Object>> getAvailableEmployeesData() {
        return employeesData;
    }

    public List<Map<String, Object>> getUnavailableEmployeesData() {
        return employeesUnavailableData;
    }

    public void updateTraining() {
        for (int i = 0; i < employeesUnavailable.size(); i++) {
            Employee e = employeesUnavailable.get(i);
            e.setDaysLeft(e.getDaysLeft() - 1);
            if (e.getDaysLeft() == 0) {
                employeesUnavailable.remove(i);
                employeesUnavailableData.remove(i);

                e.setSkill(e.getSkill() + new Random().nextInt(5));
                employees.add(e);
                Map<String, Object> m = new HashMap<>();
                m.put("Name", e.getName());
                m.put("Skill", "Skill: " + e.getSkill());
                m.put("Salary", "$" + e.getSalary() + "/month");
                employeesData.add(m);
                i--;
            } else {
                employeesUnavailableData.get(i).put("Days Left", "Days left: " + e.getDaysLeft());
            }
        }
    }

    public void addSkill() {
        for (int i = 0; i < employees.size(); i++) {
            employees.get(i).addSkill();
            employeesData.get(i).put("Skill", employees.get(i).getSkill());
        }
    }

    public void changeSalary(int position, int value) {
        if (employees.get(position).getSalary() < 11 && value < 0) return;
        employees.get(position).setSalary(employees.get(position).getSalary() + value);
        employeesData.get(position).put("Salary", "$" + employees.get(position).getSalary() + "/month");
    }
}
