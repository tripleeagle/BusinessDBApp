package eu.cz.lyalinv.model;

import javax.persistence.Entity;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Lyalin Valeriy (lyalival)
 */
public class DataContainer {
    Map<Long,Company> companyMap;
    Map<String,Employee> employeeMap;

    public DataContainer () {
        companyMap = new HashMap();
        employeeMap = new HashMap();
    }

    public Map<Long, Company> getCompanyMap() {
        return companyMap;
    }

    public void addCompany (Company company) {
        companyMap.put(company.getICO(),company);
    }

    public void setCompanyMap(Map<Long, Company> companyMap) {
        this.companyMap = companyMap;
    }

    public Map<String, Employee> getEmployeeMap() {
        return employeeMap;
    }

    public void addEmployee (Employee employee){
        employeeMap.put(employee.getFirstName() + employee.getLastName(),employee);
    }

    public void setEmployeeMap(Map<String, Employee> employeeMap) {
        this.employeeMap = employeeMap;
    }
}
