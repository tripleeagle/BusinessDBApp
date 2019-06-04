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
        boolean isPotentialDuplicate = false;

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("org.hibernate.businessdbapp.jpa");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Session session = entityManager.unwrap(Session.class);
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();
        for (Map.Entry<Long,Company> entry : dataContainer.getCompanyMap().entrySet() ) {
            Company oldCompany = getCompanyFromDBByICO(session,entry.getKey());
            Company newCompany = entry.getValue();
            if ( oldCompany != null && oldCompany.getMtime().compareTo(newCompany.getMtime()) < 0 ){
                Object oldCompanyObj = session.load(Company.class,oldCompany.getId());
                oldCompany = (Company) oldCompanyObj;
                updateCompany(oldCompany, newCompany);
                session.update(oldCompany);
                stat.modifiedCompany++;
            }
            else if ( oldCompany == null ){
                entityManager.persist(entry.getValue());
                stat.insertedCompany++;
            }
            else if ( oldCompany.getMtime().compareTo(newCompany.getMtime()) == 0 ){
                isPotentialDuplicate = true;
            }

        }

        for (Map.Entry<String, Employee> entry : dataContainer.getEmployeeMap().entrySet() ) {
            Employee oldEmployee = getEmployeeByEmail(session,entry.getValue().getEmail());
            Employee newEmployee = entry.getValue();
            if ( oldEmployee != null && oldEmployee.getMtime().compareTo(newEmployee.getMtime()) < 0 ){
                Object oldEmployeeObj = session.load(Company.class,oldEmployee.getId());
                oldEmployee = (Employee) oldEmployeeObj;
                updateEmployee(oldEmployee,newEmployee,session);
                session.update(oldEmployee);
                stat.modifiedEmployee++;
            }
            else if ( oldEmployee == null ){
                newEmployee.setCompany(getCompanyFromDBByICO(session,newEmployee.getCompany().getICO()));
                entityManager.persist(newEmployee);
                stat.insertedEmployee++;
            }
            else if ( oldEmployee.getMtime().compareTo(newEmployee.getMtime()) == 0 && isPotentialDuplicate){
                stat.duplication++;
            }

        }
        transaction.commit();

        entityManagerFactory.close();
        System.out.println("successfully saved");
        return stat;
    }

    private static void updateCompany ( Company oldCompany, Company newCompany){
        oldCompany.setMtime(newCompany.getMtime());
        oldCompany.setAddress(newCompany.getAddress());
        oldCompany.setCompanyName(newCompany.getCompanyName());
    }

    private static void updateEmployee ( Employee oldEmployee, Employee newEmployee, Session session ){
        oldEmployee.setMtime(newEmployee.getMtime());
        oldEmployee.setFirstName(newEmployee.getFirstName());
        oldEmployee.setLastName(newEmployee.getLastName());
        oldEmployee.setCompany(getCompanyFromDBByICO(session,newEmployee.getCompany().getICO()));
    }

    private static Company getCompanyFromDBByICO ( Session session, Long ICO ){
        Company company = new Company();
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
