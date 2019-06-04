package eu.cz.lyalinv.controller;

import eu.cz.lyalinv.model.Company;
import eu.cz.lyalinv.model.DataContainer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Map;

/**
 * @author Lyalin Valeriy (lyalival)
 */
public class DBController {
    public static void storeData (DataContainer dataContainer){
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("org.hibernate.businessdbapp.jpa");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        saveDataToSession(dataContainer,entityManager);
        transaction.commit();

        entityManagerFactory.close();
        System.out.println("successfully saved");

    }

    private static void saveDataToSession ( DataContainer dataContainer, EntityManager entityManager ){
        for (Map.Entry<Long,Company> entry : dataContainer.getCompanyMap().entrySet() )
            entityManager.persist(entry.getValue());
    }
}
