package com.anyway.byallmeans;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class EmployeeManagerTest {

    private Employee testEmployee;
    private EmployeeManager testEmployeeManager;

    @Before
    public void setTestEmployee() {
        testEmployee = new Employee("testName", 1, 2);
        testEmployee.setDaysLeft(0);
    }

    @Test
    public void addEmployeeTest() {
        testEmployeeManager = new EmployeeManager();
        testEmployeeManager.add(testEmployee);
        assertNotNull(testEmployeeManager.getAllEmployees());

        assertNotNull(testEmployeeManager.getAvailableEmployees());
        assertNotNull(testEmployeeManager.getAvailableEmployeesData());

        assertNotNull(testEmployeeManager.getUnavailableEmployees());
        assertNotNull(testEmployeeManager.getUnavailableEmployeesData());
    }


    @Test
    public void sendToTraining() {
        testEmployeeManager = new EmployeeManager();
        testEmployeeManager.add(testEmployee);
        testEmployeeManager.sendToTraining(0);
        testEmployee = testEmployeeManager.getUnavailableEmployees().get(0);

        assertEquals(0, testEmployeeManager.getAvailableEmployees().size());
        assertEquals(0, testEmployeeManager.getAvailableEmployeesData().size());

        assertEquals(1, testEmployeeManager.getUnavailableEmployees().size());
        assertEquals(1, testEmployeeManager.getUnavailableEmployeesData().size());
        //remove hardcode
        assertEquals(3, testEmployeeManager.getUnavailableEmployees().get(0).getDaysLeft());

    }

    //not completed
    @Test
    public void sendToTrainingTest() {
        testEmployeeManager = new EmployeeManager();
        testEmployeeManager.add(testEmployee);
        testEmployeeManager.sendToTraining(0);

        assertNotNull(testEmployeeManager.getAllEmployees());

        assertEquals(0, testEmployeeManager.getAvailableEmployees().size());
        assertEquals(0, testEmployeeManager.getAvailableEmployeesData().size());

        assertEquals(1, testEmployeeManager.getUnavailableEmployees().size());
        assertEquals(1, testEmployeeManager.getUnavailableEmployeesData().size());
    }

    @Test
    public void removeTest() {
        testEmployeeManager = new EmployeeManager();
        testEmployeeManager.add(testEmployee);
        testEmployeeManager.remove(0);
        assertNotNull(testEmployeeManager.getAllEmployees());
        assertNotNull(testEmployeeManager.getAvailableEmployees());
        assertNotNull(testEmployeeManager.getAvailableEmployeesData());
    }

    @Test
    public void getAllEmployeesTest() {
        testEmployeeManager = new EmployeeManager();
        testEmployeeManager.add(testEmployee);
        assertNotNull(testEmployeeManager.getAllEmployees());
        assertEquals(1, testEmployeeManager.getAllEmployees().size());

        assertNotNull(testEmployeeManager.getAllEmployees());
        assertEquals(1, testEmployeeManager.getAllEmployees().size());
    }

    @Test
    public void getAvailableEmployeesTest() {
        testEmployeeManager = new EmployeeManager();
        testEmployeeManager.add(testEmployee);

        assertNotNull(testEmployeeManager.getAvailableEmployees());
        assertEquals(1, testEmployeeManager.getAvailableEmployees().size());
    }

    @Test
    public void getUnavailableEmployeesTest() {
        testEmployeeManager = new EmployeeManager();
        testEmployeeManager.add(testEmployee);
        testEmployeeManager.sendToTraining(0);

        assertNotNull(testEmployeeManager.getUnavailableEmployees());
        assertEquals(1, testEmployeeManager.getUnavailableEmployees().size());
    }

    @Test
    public void getAvailableEmployeesDataTest() {
        testEmployeeManager = new EmployeeManager();
        testEmployeeManager.add(testEmployee);

        assertNotNull(testEmployeeManager.getAvailableEmployeesData());
        assertEquals(1, testEmployeeManager.getAvailableEmployeesData().size());
    }

    @Test
    public void getUnavailableEmployeesDataTest() {
        testEmployeeManager = new EmployeeManager();
        testEmployeeManager.add(testEmployee);
        testEmployeeManager.sendToTraining(0);

        assertNotNull(testEmployeeManager.getUnavailableEmployeesData());
        assertEquals(1, testEmployeeManager.getUnavailableEmployeesData().size());
    }

    @Test
    public void updateTrainingTest() {
        testEmployeeManager = new EmployeeManager();
        testEmployeeManager.add(testEmployee);
        testEmployeeManager.sendToTraining(0);
        testEmployeeManager.updateTraining();

        assertEquals(2, testEmployeeManager.getUnavailableEmployees().get(0).getDaysLeft());

    }

    @Test
    public void updateTraining_NoDaysLeft() {
        testEmployeeManager = new EmployeeManager();
        testEmployeeManager.add(testEmployee);
        testEmployeeManager.sendToTraining(0);
        testEmployeeManager.updateTraining();
        testEmployeeManager.updateTraining();
        testEmployeeManager.updateTraining();
        testEmployeeManager.updateTraining();

        assertEquals(0, testEmployeeManager.getUnavailableEmployees().size());
        assertEquals(0, testEmployeeManager.getUnavailableEmployeesData().size());

        assertEquals(1, testEmployeeManager.getAvailableEmployees().size());
        assertEquals(1, testEmployeeManager.getAvailableEmployeesData().size());

    }

    @Test
    public void addSkillTest() {
        testEmployeeManager = new EmployeeManager();
        testEmployeeManager.add(testEmployee);
        testEmployeeManager.addSkill();

        assertEquals(2, testEmployeeManager.getAvailableEmployees().get(0).getSkill());
        assertEquals("Skill: 2", testEmployeeManager.getAvailableEmployeesData().get(0).get("Skill"));
    }

    @Test
    public void changeSalaryTest() {
        testEmployeeManager = new EmployeeManager();
        testEmployeeManager.add(testEmployee);
        testEmployeeManager.changeSalary(0,100);

        //remove hardcode
        assertEquals(102, testEmployeeManager.getAvailableEmployees().get(0).getSalary());

    }

    @Test
    public void changeSalaryTest2() {
        testEmployee.setSalary(10);
        testEmployeeManager = new EmployeeManager();
        testEmployeeManager.add(testEmployee);
        testEmployeeManager.changeSalary(0, -1);

        //remove hardcode
        assertEquals(10, testEmployeeManager.getAvailableEmployees().get(0).getSalary());

    }


}
