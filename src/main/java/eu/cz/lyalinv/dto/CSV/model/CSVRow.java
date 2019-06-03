package eu.cz.lyalinv.dto.CSV.model;

import com.opencsv.bean.CsvBindByName;

/**
 * @author Lyalin Valeriy (lyalival)
 */
public class CSVRow {

    @CsvBindByName(column = "ICO")
    private Long ICO;

    @CsvBindByName(column = "company_name")
    private String companyName;

    @CsvBindByName(column = "street_address")
    private String streetAddress;

    @CsvBindByName(column = "city")
    private String city;

    @CsvBindByName(column = "country")
    private String country;

    @CsvBindByName(column = "first_name")
    private String employeeName;

    @CsvBindByName(column = "last_name")
    private String employeeLastName;

    @CsvBindByName(column = "email")
    private String employeeEmail;

    @CsvBindByName(column = "date")
    private String mtime;

    public Long getICO() {
        return ICO;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getEmployeeLastName() {
        return employeeLastName;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public String getMtime() {
        return mtime;
    }
}
