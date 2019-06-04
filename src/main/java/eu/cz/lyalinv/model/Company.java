package eu.cz.lyalinv.model;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Lyalin Valeriy (lyalival)
 */
@Entity
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long ICO;

    private String companyName;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @OneToMany(mappedBy = "company")
    private Set<Employee> employees;

    private Date mtime;

    public Company() {
        employees = new HashSet();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", ICO=" + ICO +
                ", companyName='" + companyName + '\'' +
                ", address=" + address +
                ", mtime=" + mtime +
                '}';
    }
}
