package eu.cz.lyalinv.controller;

import eu.cz.lyalinv.model.Company;
import eu.cz.lyalinv.model.DataContainer;
import eu.cz.lyalinv.model.Employee;
import eu.cz.lyalinv.utils.model.Stat;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.sql.Date;
import java.util.List;

/**
 * @author Lyalin Valeriy (lyalival)
 */
public class DBController {

    public static Stat storeData(DataContainer dataContainer) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("org.hibernate.businessdbapp.jpa");
        Stat stat = new Stat();
        boolean isPotentialDuplicate = false;

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Session session = entityManager.unwrap(Session.class);
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();
        for (Company newCompany : dataContainer.getCompanyList()) {
            Company oldCompany = getCompanyFromDBByICO(session, newCompany.getICO());
            if (oldCompany != null && oldCompany.getMtime().compareTo(newCompany.getMtime()) < 0) {
                Object oldCompanyObj = session.load(Company.class, oldCompany.getId());
                oldCompany = (Company) oldCompanyObj;
                updateCompany(oldCompany, newCompany);
                session.update(oldCompany);
                stat.modifiedCompany++;
            } else if (oldCompany == null) {
                entityManager.persist(newCompany);
                stat.insertedCompany++;
            } else if (oldCompany.getMtime().compareTo(newCompany.getMtime()) == 0) {
                isPotentialDuplicate = true;
            }
        }

        for (Employee newEmployee : dataContainer.getEmployeeList()) {
            Employee oldEmployee = getEmployeeByEmail(session, newEmployee.getEmail());
            if (oldEmployee != null && oldEmployee.getMtime().compareTo(newEmployee.getMtime()) < 0) {
                Object oldEmployeeObj = session.load(Employee.class, oldEmployee.getId());
                oldEmployee = (Employee) oldEmployeeObj;
                updateEmployee(oldEmployee, newEmployee, session);
                session.update(oldEmployee);
                stat.modifiedEmployee++;
            } else if (oldEmployee == null) {
                //Change company to the newest company, that has been stored in DB
                newEmployee.setCompany(getCompanyFromDBByICO(session, newEmployee.getCompany().getICO()));
                entityManager.persist(newEmployee);
                stat.insertedEmployee++;
            } else if (oldEmployee.getMtime().compareTo(newEmployee.getMtime()) == 0 && isPotentialDuplicate) {
                stat.duplication++;
            }
        }
        transaction.commit();

        entityManagerFactory.close();
        System.out.println("successfully saved");
        return stat;
    }

    public static List<Employee> getEmployees() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("org.hibernate.businessdbapp.jpa");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Session session = entityManager.unwrap(Session.class);
        Transaction tx = null;
        List<Employee> employees = null;
        try {
            tx = session.beginTransaction();
            employees = session.createQuery("FROM Employee").list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
            entityManagerFactory.close();
        }
        return employees;
    }

    public static List<Company> getCompaines() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("org.hibernate.businessdbapp.jpa");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Session session = entityManager.unwrap(Session.class);
        Transaction tx = null;
        List<Company> companies = null;
        try {
            tx = session.beginTransaction();
            companies = session.createQuery("FROM Company").list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
            entityManagerFactory.close();
        }
        return companies;
    }

    private static void updateCompany(Company oldCompany, Company newCompany) {
        oldCompany.setMtime(newCompany.getMtime());
        oldCompany.setAddress(newCompany.getAddress());
        oldCompany.setCompanyName(newCompany.getCompanyName());
    }

    private static void updateEmployee(Employee oldEmployee, Employee newEmployee, Session session) {
        oldEmployee.setMtime(newEmployee.getMtime());
        oldEmployee.setFirstName(newEmployee.getFirstName());
        oldEmployee.setLastName(newEmployee.getLastName());
        oldEmployee.setCompany(getCompanyFromDBByICO(session, newEmployee.getCompany().getICO()));
    }

    private static Company getCompanyFromDBByICO(Session session, Long ICO) {
        Company company = new Company();
        List<Object[]> rows = session.createQuery("select ICO, mtime, id from Company c where c.ICO = :ICOArg")
                .setParameter("ICOArg", ICO).list();
        for (Object[] row : rows) {
            company.setICO(ICO);
            String[] splitted = row[1].toString().split(" ");
            company.setMtime(Date.valueOf(splitted[0]));
            company.setId(Long.parseLong(row[2].toString()));
        }
        return company.getMtime() == null ? null : company;
    }

    private static Employee getEmployeeByEmail(Session session, String email) {
        Employee employee = new Employee();
        List<Object[]> rows = session.createQuery("select mtime, id from Employee e where e.email like :emailArg")
                .setParameter("emailArg", email).list();
        for (Object[] row : rows) {
            String[] splitted = row[0].toString().split(" ");
            employee.setMtime(Date.valueOf(splitted[0]));
            employee.setId(Long.parseLong(row[1].toString()));
        }
        return employee.getMtime() == null ? null : employee;
    }
}
