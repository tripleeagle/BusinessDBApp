package eu.cz.lyalinv.view;

import eu.cz.lyalinv.controller.DBController;
import eu.cz.lyalinv.controller.ImportController;
import eu.cz.lyalinv.model.Company;
import eu.cz.lyalinv.model.DataContainer;
import eu.cz.lyalinv.model.Employee;
import eu.cz.lyalinv.utils.model.Stat;

import java.util.Scanner;

/**
 * @author Lyalin Valeriy (lyalival)
 */
public class CommandLine {

    public static void main(String[] args) {
        System.out.println("Enter the path to CSV files:");
        String inputPath = null ;
        String outPath = null;
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            inputPath = sc.next();
            break;
        }

        System.out.println("Now, please, enter the output directory:");
        while (sc.hasNext()) {
            outPath = sc.next();
            break;
        }

        DataContainer dataContainer = ImportController.importCSVFromFolderAndMove(inputPath, outPath);
        Stat stat = DBController.storeData(dataContainer);

        System.out.println("Statistics:");
        System.out.println("Count of inserted companies = " + stat.insertedCompany);
        System.out.println("Count of inserted employees = " + stat.insertedEmployee);
        System.out.println("Count of modified companies = " + stat.modifiedCompany);
        System.out.println("Count of modified employees = " + stat.modifiedEmployee);
        System.out.println("Count of duplicate records = " + stat.duplication);

        System.out.println("Companies:");
        for (Company company : DBController.getCompaines())
            System.out.println(company.toString());

        System.out.println("Employees:");
        for (Employee employee : DBController.getEmployees())
            System.out.println(employee.toString());
    }
}
