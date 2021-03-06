package eu.cz.lyalinv;

import eu.cz.lyalinv.controller.DBController;
import eu.cz.lyalinv.controller.ImportController;
import eu.cz.lyalinv.utils.model.DataContainer;
import eu.cz.lyalinv.model.Employee;
import eu.cz.lyalinv.utils.model.Stat;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Lyalin Valeriy (lyalival)
 */
public class DBTest {
    private static String projectsFolder = System.getProperty("user.home") + "/Projects/";
    private static String resourcesFolder = "BusinessDBApp/src/test/resources/";
    private static String input = "inputs/csv_inputs/";
    private static String duplicatesInput = "inputs/csv_inputs_duplicates_test/";
    private static String modificationsInput = "inputs/csv_inputs_modification_test/";
    private static String out = "out";

    /**
     * Only check if Hibernate doesn't throw an error
     */
    @Test
    public void testImportAndStore() {
        DataContainer dataContainer = ImportController.importCSVFromFolderAndMove(projectsFolder + resourcesFolder + input, projectsFolder + resourcesFolder + out);
        ImportCSVTest.moveFilesBack(input);
        DBController.storeData(dataContainer);
    }

    /**
     * @note The result of the test depends on Hibernate property : create / create-drop / update
     * To pass this test the Hibernate property should be "update"
     */
    @Test
    public void testImportAndStoreAndEmployeeList() {
        DataContainer dataContainer = ImportController.importCSVFromFolderAndMove(projectsFolder + resourcesFolder + input, projectsFolder + resourcesFolder + out);
        ImportCSVTest.moveFilesBack(input);
        DBController.storeData(dataContainer);
        List<Employee> employeeList = DBController.getEmployees();
        assertEquals(17, employeeList.size());
        List<String> employeeNameList = getEmployeeNameList(employeeList);
        List<String> refEmployeeNameList = getRefEmployeeNameList();
        employeeNameList.sort(String::compareToIgnoreCase);
        refEmployeeNameList.sort(String::compareToIgnoreCase);
        assertEquals(refEmployeeNameList, employeeNameList);
    }

    /**
     * @note Number of duplicates depends on order of the file, so for testing duplicate and modification there is another test
     */
    @Test
    public void testStat() {
        DataContainer dataContainer = ImportController.importCSVFromFolderAndMove(projectsFolder + resourcesFolder + input, projectsFolder + resourcesFolder + out);
        ImportCSVTest.moveFilesBack(input);
        Stat stat = DBController.storeData(dataContainer);
        assertEquals(9, stat.insertedCompany);
        assertEquals(17, stat.insertedEmployee);
    }

    @Test
    public void testDuplicates() {
        DataContainer dataContainer = ImportController.importCSVFromFolderAndMove(projectsFolder + resourcesFolder + duplicatesInput, projectsFolder + resourcesFolder + out);
        ImportCSVTest.moveFilesBack(duplicatesInput);
        Stat stat = DBController.storeData(dataContainer);
        assertEquals(1, stat.insertedCompany);
        assertEquals(3, stat.insertedEmployee);
        assertEquals(3, stat.duplication);
    }

    /**
     * @note Company and Employee modifies every time after adding the record with a new newer date
     */
    @Test
    public void testModifications() {
        DataContainer dataContainer = ImportController.importCSVFromFolderAndMove(projectsFolder + resourcesFolder + modificationsInput, projectsFolder + resourcesFolder + out);
        ImportCSVTest.moveFilesBack(modificationsInput);
        Stat stat = DBController.storeData(dataContainer);
        assertEquals(2, stat.insertedCompany);
        assertEquals(2, stat.insertedEmployee);
        assertEquals(2, stat.modifiedCompany);
        assertEquals(2, stat.modifiedEmployee);
        assertEquals(0, stat.duplication);
    }

    private List<String> getEmployeeNameList(List<Employee> employeeList) {
        List<String> employeeNameList = new ArrayList();
        for (Employee employee : employeeList)
            employeeNameList.add(employee.getFirstName() + employee.getLastName());
        return employeeNameList;
    }

    private List<String> getRefEmployeeNameList() {
        List<String> employeeNameList = new ArrayList();
        employeeNameList.add("Willem" + "Friedenbach");
        employeeNameList.add("Linn" + "Sexti");
        employeeNameList.add("Edsel" + "Messer");
        employeeNameList.add("Giulio" + "Comini");
        employeeNameList.add("Lonee" + "Scudders");
        employeeNameList.add("Caresse" + "Prin");
        employeeNameList.add("Konstanze" + "Panyer");
        employeeNameList.add("Rowena" + "Mortlock");
        employeeNameList.add("Cliff" + "Petken");
        employeeNameList.add("Carina" + "Martygin");
        employeeNameList.add("Erl" + "Lebrun");
        employeeNameList.add("Sayre" + "Batchelder");
        employeeNameList.add("Reggie" + "Emburey");
        employeeNameList.add("Jordan" + "Brims");
        employeeNameList.add("Marilee" + "Tofano");
        employeeNameList.add("Cary" + "Skoyles");
        employeeNameList.add("Nikolaos" + "Willatts");
        return employeeNameList;
    }

}
