package eu.cz.lyalinv.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Lyalin Valeriy (lyalival)
 */
public class Company {

    private Long ICO;

    private String companyName;

    private Address address;

    private Set<Employee> employees;

    private Date mtime;

    public Company () {
        employees = new HashSet();
    }

    public Long getICO() {
        return ICO;
    }

    public void setICO(Long ICO) {
        this.ICO = ICO;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }

    public Date getMtime() {
        return mtime;
    }

    public void setMtime(Date mtime) {
        this.mtime = mtime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return ICO.equals(company.ICO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ICO);
    }
}
