package eu.cz.lyalinv.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lyalin Valeriy (lyalival)
 */
public class DataContainer {
    List<Company> companyList;
    List<Employee> employeeList;

    public DataContainer() {
        companyList = new ArrayList();
        employeeList = new ArrayList();
    }


    public void addCompany(Company company) {
        companyList.add(company);
    }


    public void addEmployee(Employee employee) {
        employeeList.add(employee);
    }

    public List<Company> getCompanyList() {
        return companyList;
    }

    public void setCompanyList(List<Company> companyList) {
        this.companyList = companyList;
    }

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }
}
