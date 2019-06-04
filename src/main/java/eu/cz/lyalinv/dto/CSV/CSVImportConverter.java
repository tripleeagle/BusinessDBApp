package eu.cz.lyalinv.dto.CSV;

import eu.cz.lyalinv.dto.CSV.model.CSVRow;
import eu.cz.lyalinv.model.Address;
import eu.cz.lyalinv.model.Company;
import eu.cz.lyalinv.utils.model.DataContainer;
import eu.cz.lyalinv.model.Employee;

import java.sql.Date;
import java.util.List;

/**
 * @author Lyalin Valeriy (lyalival)
 */
public class CSVImportConverter {

    public static DataContainer importData(List<CSVRow> inputDataList) {
        DataContainer dataContainer = new DataContainer();

        for (CSVRow csvRow : inputDataList) {
            Company company = getCompanyFromCSVRow(csvRow);
            dataContainer.addCompany(company);

            Employee employee = getEmployeeFromCSVRow(csvRow, company);
            dataContainer.addEmployee(employee);
        }
        return dataContainer;
    }

    private static Company getCompanyFromCSVRow(CSVRow csvRow) {
        Company company = new Company();
        company.setICO(csvRow.getICO());
        company.setCompanyName(csvRow.getCompanyName());
        company.setAddress(new Address(csvRow.getStreetAddress(), csvRow.getCity(), csvRow.getCountry()));
        company.setMtime(Date.valueOf(csvRow.getMtime()));
        return company;
    }

    private static Employee getEmployeeFromCSVRow(CSVRow csvRow, Company company) {
        Employee employee = new Employee();
        employee.setCompany(company);
        employee.setEmail(csvRow.getEmployeeEmail());
        employee.setFirstName(csvRow.getEmployeeName());
        employee.setLastName(csvRow.getEmployeeLastName());
        employee.setMtime(Date.valueOf(csvRow.getMtime()));
        return employee;
    }
}
