package com.anyway.byallmeans;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EmployeeManagerTest {

    private Employee testEmployee;
    private EmployeeManager testEmployeeManager;

    @Before
    public void setTestEmployee() {
        testEmployee = new Employee("testName", 1, 2);
        testEmployee.setDaysLeft(0);
    }

    @Test
    public void addEmployee_NewEmployee_AllEmployeesListHasOneElement() {
        testEmployeeManager = new EmployeeManager();
        testEmployeeManager.add(testEmployee);
        assertEquals(1, testEmployeeManager.getAllEmployees().size());
  }

    @Test
    public void addEmployee_NewEmployee_AvailableEmployeesListHasOneElement() {
        testEmployeeManager = new EmployeeManager();
        testEmployeeManager.add(testEmployee);

        assertEquals(1, testEmployeeManager.getAvailableEmployees().size());
    }

    @Test
    public void addEmployee_NewEmployee_AvailableEmployeesDataListHasOneElement() {
        testEmployeeManager = new EmployeeManager();
        testEmployeeManager.add(testEmployee);

        assertEquals(1, testEmployeeManager.getAvailableEmployeesData().size());
    }

    @Test
    public void addEmployee_NewEmployee_UnavailableEmployeesListHasNoElements() {
        testEmployeeManager = new EmployeeManager();
        testEmployeeManager.add(testEmployee);

        assertEquals(0, testEmployeeManager.getUnavailableEmployees().size());
    }

    @Test
    public void addEmployee_NewEmployee_UnavailableEmployeesDataListHasNoElements() {
        testEmployeeManager = new EmployeeManager();
        testEmployeeManager.add(testEmployee);

        assertEquals(0, testEmployeeManager.getUnavailableEmployeesData().size());
    }

    //remove hardcode
    @Test
    public void sendToTraining_IndexInBounds_AvailableEmployeesListHasNoElements() {
        testEmployeeManager = new EmployeeManager();
        testEmployeeManager.add(testEmployee);
        testEmployeeManager.sendToTraining(0);
        testEmployee = testEmployeeManager.getUnavailableEmployees().get(0);

        assertEquals(0, testEmployeeManager.getAvailableEmployees().size());
    }

    //remove hardcode
    @Test
    public void sendToTraining_IndexInBounds_AvailableEmployeesDataListHasNoElements() {
        testEmployeeManager = new EmployeeManager();
        testEmployeeManager.add(testEmployee);
        testEmployeeManager.sendToTraining(0);
        testEmployee = testEmployeeManager.getUnavailableEmployees().get(0);

        assertEquals(0, testEmployeeManager.getAvailableEmployeesData().size());
    }

    //remove hardcode
    @Test
    public void sendToTraining_IndexInBounds_UnavailableEmployeesListHasOneElements() {
        testEmployeeManager = new EmployeeManager();
        testEmployeeManager.add(testEmployee);
        testEmployeeManager.sendToTraining(0);
        testEmployee = testEmployeeManager.getUnavailableEmployees().get(0);

        assertEquals(1, testEmployeeManager.getUnavailableEmployees().size());
    }

    //remove hardcode
    @Test
    public void sendToTraining_IndexInBounds_UnavailableEmployeesDataListHasOneElements() {
        testEmployeeManager = new EmployeeManager();
        testEmployeeManager.add(testEmployee);
        testEmployeeManager.sendToTraining(0);
        testEmployee = testEmployeeManager.getUnavailableEmployees().get(0);

        assertEquals(1, testEmployeeManager.getUnavailableEmployeesData().size());
    }

    //remove hardcode
    @Test
    public void sendToTraining_IndexInBounds_DaysLeftChanges() {
        testEmployeeManager = new EmployeeManager();
        testEmployeeManager.add(testEmployee);
        testEmployeeManager.sendToTraining(0);
        testEmployee = testEmployeeManager.getUnavailableEmployees().get(0);

        assertEquals(3, testEmployeeManager.getUnavailableEmployees().get(0).getDaysLeft());
    }

    @Test
    public void updateTraining_IndexInBounds_DaysLeftChanges() {
        testEmployeeManager = new EmployeeManager();
        testEmployeeManager.add(testEmployee);
        testEmployeeManager.sendToTraining(0);
        testEmployeeManager.updateTraining();

        assertEquals(2, testEmployeeManager.getUnavailableEmployees().get(0).getDaysLeft());
    }

    @Test
    public void removeTest_IndexInBounds_AllEmployeesListHasNoElements() {
        testEmployeeManager = new EmployeeManager();
        testEmployeeManager.add(testEmployee);
        testEmployeeManager.remove(0);
        assertEquals(0,testEmployeeManager.getAllEmployees().size());
    }

    @Test
    public void removeTest_IndexInBounds_AvailableEmployeesListHasNoElements() {
        testEmployeeManager = new EmployeeManager();
        testEmployeeManager.add(testEmployee);
        testEmployeeManager.remove(0);
        assertEquals(0,testEmployeeManager.getAvailableEmployees().size());
    }

    @Test
    public void removeTest_IndexInBounds_AvailableEmployeesDataListHasNoElements() {
        testEmployeeManager = new EmployeeManager();
        testEmployeeManager.add(testEmployee);
        testEmployeeManager.remove(0);
        assertEquals(0,testEmployeeManager.getAvailableEmployeesData().size());
    }

    @Test
    public void getAllEmployeesTest() {
        testEmployeeManager = new EmployeeManager();
        testEmployeeManager.add(testEmployee);
        assertEquals(1, testEmployeeManager.getAllEmployees().size());
    }

    @Test
    public void getAvailableEmployeesTest() {
        testEmployeeManager = new EmployeeManager();
        testEmployeeManager.add(testEmployee);

        assertEquals(1, testEmployeeManager.getAvailableEmployees().size());
    }

    @Test
    public void getUnavailableEmployeesTest() {
        testEmployeeManager = new EmployeeManager();
        testEmployeeManager.add(testEmployee);
        testEmployeeManager.sendToTraining(0);

        assertEquals(1, testEmployeeManager.getUnavailableEmployees().size());
    }

    @Test
    public void getAvailableEmployeesDataTest() {
        testEmployeeManager = new EmployeeManager();
        testEmployeeManager.add(testEmployee);

        assertEquals(1, testEmployeeManager.getAvailableEmployeesData().size());
    }

    @Test
    public void getUnavailableEmployeesDataTest() {
        testEmployeeManager = new EmployeeManager();
        testEmployeeManager.add(testEmployee);
        testEmployeeManager.sendToTraining(0);

        assertEquals(1, testEmployeeManager.getUnavailableEmployeesData().size());
    }

    @Test
    public void updateTraining_NoDaysLeft_UnavailableEmployeesListHasNoElements() {
        testEmployeeManager = new EmployeeManager();
        testEmployeeManager.add(testEmployee);
        testEmployeeManager.sendToTraining(0);
        testEmployeeManager.updateTraining();

        while(testEmployeeManager.getAllEmployees().get(0).getDaysLeft()>0){
            testEmployeeManager.updateTraining();
        }

        assertEquals(0, testEmployeeManager.getUnavailableEmployees().size());
    }

    @Test
    public void updateTraining_NoDaysLeft_UnavailableEmployeesDataListHasNoElements() {
        testEmployeeManager = new EmployeeManager();
        testEmployeeManager.add(testEmployee);
        testEmployeeManager.sendToTraining(0);
        testEmployeeManager.updateTraining();

        while(testEmployeeManager.getAllEmployees().get(0).getDaysLeft()>0){
            testEmployeeManager.updateTraining();
        }

        assertEquals(0, testEmployeeManager.getUnavailableEmployeesData().size());
    }

    @Test
    public void updateTraining_NoDaysLeft_AvailableEmployeesListHasNoElements() {
        testEmployeeManager = new EmployeeManager();
        testEmployeeManager.add(testEmployee);
        testEmployeeManager.sendToTraining(0);
        testEmployeeManager.updateTraining();

        while(testEmployeeManager.getAllEmployees().get(0).getDaysLeft()>0){
            testEmployeeManager.updateTraining();
        }

        assertEquals(1, testEmployeeManager.getAvailableEmployees().size());
    }

    @Test
    public void updateTraining_NoDaysLeft_AvailableEmployeesDataListHasNoElements() {
        testEmployeeManager = new EmployeeManager();
        testEmployeeManager.add(testEmployee);
        testEmployeeManager.sendToTraining(0);
        testEmployeeManager.updateTraining();

        while(testEmployeeManager.getAllEmployees().get(0).getDaysLeft()>0){
            testEmployeeManager.updateTraining();
        }

        assertEquals(1, testEmployeeManager.getAvailableEmployeesData().size());
    }

    @Test
    public void addSkill_EmployeeExist_SkillChanges() {
        testEmployeeManager = new EmployeeManager();
        testEmployeeManager.add(testEmployee);
        testEmployeeManager.addSkill();

        assertEquals(2, testEmployeeManager.getAvailableEmployees().get(0).getSkill());
    }

    @Test
    public void addSkill_EmployeeExist_EmployeeDataChanges() {
        testEmployeeManager = new EmployeeManager();
        testEmployeeManager.add(testEmployee);
        testEmployeeManager.addSkill();

        assertEquals("Skill: 2", testEmployeeManager.getAvailableEmployeesData().get(0).get("Skill"));
    }

    @Test
    public void changeSalary_CorrectPositionAndValue_SalaryChanges() {
        testEmployeeManager = new EmployeeManager();
        testEmployeeManager.add(testEmployee);
        testEmployeeManager.changeSalary(0, 100);

        //remove hardcode
        assertEquals(102, testEmployeeManager.getAvailableEmployees().get(0).getSalary());
    }

    @Test
    public void changeSalary_IncorrectPositionAndValue_SalaryDoesNotChange() {
        testEmployee.setSalary(10);
        testEmployeeManager = new EmployeeManager();
        testEmployeeManager.add(testEmployee);
        testEmployeeManager.changeSalary(0, -1);

        //remove hardcode
        assertEquals(10, testEmployeeManager.getAvailableEmployees().get(0).getSalary());

    }

}
