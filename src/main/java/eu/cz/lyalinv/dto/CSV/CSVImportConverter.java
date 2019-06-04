package eu.cz.lyalinv.dto.CSV;

import eu.cz.lyalinv.dto.CSV.model.CSVRow;
import eu.cz.lyalinv.model.Address;
import eu.cz.lyalinv.model.Company;
import eu.cz.lyalinv.model.DataContainer;
import eu.cz.lyalinv.model.Employee;

import java.sql.Date;
import java.util.List;

/**
 * @author Lyalin Valeriy (lyalival)
 */
public class CSVImportConverter {

    public static DataContainer importData(List<CSVRow> inputDataList) {
        DataContainer dataContainer = new DataContainer();

        for ( CSVRow csvRow : inputDataList )
        {
            Company company = dataContainer.getCompanyMap().get(csvRow.getICO());
            if ( company == null ) {
                company = new Company();
                updateCompanyFromCSVRow(csvRow,company);
                dataContainer.addCompany(company);
            }
            else if (Date.valueOf(csvRow.getMtime()).compareTo(company.getMtime()) > 0 ){
                updateCompanyFromCSVRow(csvRow,company);
                dataContainer.addCompany(company);
            }

            Employee employee = dataContainer.getEmployeeMap().get(csvRow.getEmployeeName()+csvRow.getEmployeeLastName());
            if ( employee == null ){
                employee = new Employee();
                updateEmployeeFromCSVRow(csvRow,employee,company);
                dataContainer.addEmployee(employee);
            }
            else if (Date.valueOf(csvRow.getMtime()).compareTo(employee.getMtime()) > 0 ) {
                updateEmployeeFromCSVRow(csvRow,employee,company);
                dataContainer.addEmployee(employee);
            }
        }
        return dataContainer;
    }

    private static void updateCompanyFromCSVRow ( CSVRow csvRow, Company company ){
        company.setICO(csvRow.getICO());
        company.setCompanyName(csvRow.getCompanyName());
        company.setAddress(new Address(csvRow.getStreetAddress(),csvRow.getCity(),csvRow.getCountry()));
        company.setMtime(Date.valueOf(csvRow.getMtime()));
    }

    private static void updateEmployeeFromCSVRow ( CSVRow csvRow, Employee employee, Company company){
        employee.setCompany(company);
        employee.setEmail(csvRow.getEmployeeEmail());
        employee.setFirstName(csvRow.getEmployeeName());
        employee.setLastName(csvRow.getEmployeeLastName());
        employee.setMtime(Date.valueOf(csvRow.getMtime()));
        company.getEmployees().add(employee);
    }
}
