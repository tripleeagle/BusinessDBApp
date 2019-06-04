package eu.cz.lyalinv.controller;

import eu.cz.lyalinv.model.Company;
import eu.cz.lyalinv.model.DataContainer;
import eu.cz.lyalinv.model.Employee;
import eu.cz.lyalinv.utils.model.Stat;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Lyalin Valeriy (lyalival)
 */
public class DBController {
    public static Stat storeData (DataContainer dataContainer){
        Stat stat = new Stat();
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("org.hibernate.businessdbapp.jpa");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Session session = entityManager.unwrap(Session.class);
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();
        for (Map.Entry<Long,Company> entry : dataContainer.getCompanyMap().entrySet() ) {
            Company oldCompany = getCompanyFromDBByICO(session,entry.getKey());
            Company newCompany = entry.getValue();
            if ( oldCompany != null && oldCompany.getMtime().compareTo(newCompany.getMtime()) < 0 ){
                Object o = session.load(Company.class,oldCompany.getId());
                oldCompany = (Company) o;
                oldCompany.setMtime(newCompany.getMtime());
                oldCompany.setAddress(newCompany.getAddress());
                oldCompany.setCompanyName(newCompany.getCompanyName());
                session.update(oldCompany);
                stat.modifiedCompany++;
            }
            else {
                entityManager.persist(entry.getValue());
                stat.insertedCompany++;
            }
        }

        for (Map.Entry<String, Employee> entry : dataContainer.getEmployeeMap().entrySet() ) {
            Employee oldEmployee = getEmployeeByEmail(session,entry.getValue().getEmail());
            Employee newEpmloyee = entry.getValue();
            if ( oldEmployee != null && oldEmployee.getMtime().compareTo(newEpmloyee.getMtime()) < 0 ){
                Object o = session.load(Company.class,oldEmployee.getId());
                oldEmployee = (Employee) o;
                oldEmployee.setMtime(newEpmloyee.getMtime());
                oldEmployee.setFirstName(newEpmloyee.getFirstName());
                oldEmployee.setLastName(newEpmloyee.getLastName());
                oldEmployee.setCompany(newEpmloyee.getCompany());
                session.update(oldEmployee);
                stat.modifiedEmployee++;
            }
            else {
                entityManager.persist(entry.getValue());
                stat.insertedEmployee++;
            }
        }
        transaction.commit();

        entityManagerFactory.close();
        System.out.println("successfully saved");
        return stat;
    }

    private static Company getCompanyFromDBByICO ( Session session, Long ICO ){
        Company company = new Company();
        String query = "" + ICO;
        List<Object[]> rows = session.createQuery("select ICO, mtime, id from Company c where c.ICO = :ICOArg")
                                .setParameter("ICOArg",ICO).list();
        for(Object[] row : rows){
            company.setICO(ICO);
            String[] splitted = row[1].toString().split(" ");
            company.setMtime(Date.valueOf(splitted[0]));
            company.setId(Long.parseLong(row[2].toString()));
        }
        return company.getMtime() == null ? null : company;
    }

    private static Employee getEmployeeByEmail ( Session session, String email ){
        Employee employee = new Employee();
        String query = "select mtime, id from Employee e where e.email like " + email;
        List<Object[]> rows = session.createQuery("select mtime, id from Employee e where e.email like :emailArg")
                                    .setParameter("emailArg",email).list();
        for(Object[] row : rows){
            String[] splitted = row[0].toString().split(" ");
            employee.setMtime(Date.valueOf(splitted[0]));
            employee.setId(Long.parseLong(row[1].toString()));
        }
        return employee.getMtime() == null ? null : employee;
    }
}
