package eu.cz.lyalinv.controller;

import eu.cz.lyalinv.model.Company;
import eu.cz.lyalinv.model.DataContainer;
import eu.cz.lyalinv.utils.model.Stat;
import org.hibernate.Criteria;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
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
            if ( oldCompany != null && oldCompany.getMtime().compareTo(newCompany.getMtime()) <= 0 ){
                Object o = session.load(Company.class,oldCompany.getId());
                oldCompany = (Company) o;
                oldCompany.setMtime(newCompany.getMtime());
                oldCompany.setAddress(newCompany.getAddress());
                oldCompany.setCompanyName(newCompany.getCompanyName());
                session.update(oldCompany);
            }
            else {
                entityManager.persist(entry.getValue());
                stat.insertedCompany++;
            }
        }
        transaction.commit();

        entityManagerFactory.close();
        System.out.println("successfully saved");
        return stat;
    }

    private static Company getCompanyFromDBByICO ( Session session, Long ICO ){
        Company company = new Company();
        List<Object[]> rows = session.createQuery("select ICO, mtime, id from Company c where c.ICO=ICO").list();
        for(Object[] row : rows){
            company.setICO(ICO);
            String[] splitted = row[1].toString().split(" ");
            company.setMtime(Date.valueOf(splitted[0]));
            company.setId(Long.parseLong(row[2].toString()));
        }
        return company.getMtime() == null ? null : company;
    }
}
