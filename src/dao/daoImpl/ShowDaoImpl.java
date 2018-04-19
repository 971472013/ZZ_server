package dao.daoImpl;

import crawler.DM_crawler;
import dao.ShowDao;
import model.Show;
import model.User;

import javax.persistence.*;
import java.util.List;

/**
 * Created by zhuanggangqing on 2018/3/5.
 */
public class ShowDaoImpl implements ShowDao{
    private static ShowDaoImpl showDaoImpl = null;
    @PersistenceUnit(name = "ZZ")
    private EntityManagerFactory factory;

    @PersistenceContext
    private EntityManager em;


    private ShowDaoImpl(){}

    public static ShowDaoImpl getInstance(){
        if (null==showDaoImpl){
            DM_crawler dm_crawler = new DM_crawler();
            dm_crawler.crawler();
            return new ShowDaoImpl();
        }
        return showDaoImpl;
    }
    @Override
    public List<Show> findAllShow() {
        factory = Persistence.createEntityManagerFactory("ZZ");
        em = factory.createEntityManager();
        String q ="select * from zz_show";
        Query query = em.createNativeQuery(q, Show.class);
        List<Show> result = query.getResultList();
        if(query.getResultList().size()==0){
            em.close();
            factory.close();
            return null;
        }
        em.close();
        factory.close();
        return result;
    }

    @Override
    public Show findShowByName(String name) {
        factory = Persistence.createEntityManagerFactory("ZZ");
        em = factory.createEntityManager();
        String q ="select * from zz_show where `name` LIKE '"+name+"%'";
        Query query = em.createNativeQuery(q, Show.class);
        List<Show> result = query.getResultList();
        if(query.getResultList().size()==0){
            em.close();
            factory.close();
            return null;
        }
        em.close();
        factory.close();
        return result.get(0);
    }

    @Override
    public List<Show> findAllShowsByPlace(String place) {
        factory = Persistence.createEntityManagerFactory("ZZ");
        em = factory.createEntityManager();
        String q ="select * from zz_show where `place`=\""+place+"\"";
        Query query = em.createNativeQuery(q, Show.class);
        List<Show> result = query.getResultList();
        if (query.getResultList().size() == 0) {
            em.close();
            factory.close();
            return null;
        }
        em.close();
        factory.close();
        return result;
    }
}
