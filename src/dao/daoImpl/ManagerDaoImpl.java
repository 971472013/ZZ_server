package dao.daoImpl;

import com.sun.tools.corba.se.idl.constExpr.Or;
import dao.ManagerDao;
import model.Change_m;
import model.Manager;
import model.Order;
import model.Place;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhuanggangqing on 2018/3/26.
 */
public class ManagerDaoImpl implements ManagerDao{
    private static ManagerDao managerDao;
    private ManagerDaoImpl(){}
    public static ManagerDao getInstance(){
        if(managerDao == null){
            managerDao = new ManagerDaoImpl();
        }
        return managerDao;
    }

    @PersistenceUnit(name = "ZZ")
    private EntityManagerFactory factory;

    @PersistenceContext
    private EntityManager em;

    @Override
    public Manager findManagerByEmail(String email) {
        factory = Persistence.createEntityManagerFactory("ZZ");
        em = factory.createEntityManager();
        String q ="select * from zz_manager WHERE `email`=\""+email+"\"";
        Query query = em.createNativeQuery(q, Manager.class);
        List<Manager> result = query.getResultList();
        if(result.size() != 0){
            em.close();
            factory.close();
            return result.get(0);
        }
        em.close();
        factory.close();
        return null;
    }

    @Override
    public List<Place> getAllPlace_registering() {
        factory = Persistence.createEntityManagerFactory("ZZ");
        em = factory.createEntityManager();
        String q = "select * from zz_place where `state`=0";
        Query query = em.createNativeQuery(q,Place.class);
        List<Place> result = query.getResultList();
        if(result == null){
            em.close();
            factory.close();
            return new ArrayList<Place>();
        }
        em.close();
        factory.close();
        return result;
    }

    @Override
    public boolean accept_register(String name) {
        factory = Persistence.createEntityManagerFactory("ZZ");
        em = factory.createEntityManager();
        Query query = em.createNativeQuery("UPDATE zz_place SET state = 1 WHERE `name` =?")
                .setParameter(1,name);
        query.executeUpdate();
        em.close();
        factory.close();
        return true;
    }

    @Override
    public List<Place> getAllPlace_changeInfo() {
        factory = Persistence.createEntityManagerFactory("ZZ");
        em = factory.createEntityManager();
        String q = "select * from zz_placeInfo_list WHERE state=0";
        Query query = em.createNativeQuery(q,Place.class);
        List<Place> result = query.getResultList();
        if(result == null){
            em.close();
            factory.close();
            return new ArrayList<Place>();
        }
        em.close();
        factory.close();
        return result;
    }

    @Override
    public List<Place> getChangeInfoByID(String id) {
        factory = Persistence.createEntityManagerFactory("ZZ");
        em = factory.createEntityManager();
        String q = "select * from zz_placeInfo_list WHERE id=\""+id+"\" and state=0";
        Query query = em.createNativeQuery(q,Place.class);
        List<Place> result = query.getResultList();
        em.clear();
        String q2 = "select * from zz_placeInfo_list WHERE id=\""+id+"\" and state=1";
        Query query2 = em.createNativeQuery(q2,Place.class);
        result.add((Place) query2.getResultList().get(0));
        em.close();
        factory.close();
        return result;
    }

    @Override
    public boolean sure(String id) {
        factory = Persistence.createEntityManagerFactory("ZZ");
        em = factory.createEntityManager();
        String q1 = "select * from zz_placeInfo_list WHERE id=\""+id+"\" and state=1";
        Query query1 = em.createNativeQuery(q1,Place.class);
        Place place_new = (Place) query1.getResultList().get(0);
        em.clear();
        q1 = "select * from zz_placeInfo_list WHERE id=\""+id+"\" and state=0";
        query1 = em.createNativeQuery(q1,Place.class);
        Place place_old = (Place) query1.getResultList().get(0);
        em.clear();
        Query query2 = em.createNativeQuery("DELETE FROM zz_placeInfo_list WHERE id =?")
                .setParameter(1,id);
        query2.executeUpdate();
        em.clear();
        Query query3 = em.createNativeQuery("UPDATE zz_place SET `name`=?,city=?,introduction=?,img=? WHERE id =?")
                .setParameter(1,place_new.getName()).setParameter(2,place_new.getCity()).setParameter(3,place_new.getIntroduction())
                .setParameter(4,place_new.getImg()).setParameter(5,place_new.getId());
        query3.executeUpdate();
        em.clear();
        query3 = em.createNativeQuery("UPDATE zz_show SET place=? WHERE place=?")
        .setParameter(1,place_new.getName()).setParameter(2,place_old.getName());
        query3.executeUpdate();
        em.close();
        factory.close();
        return true;
    }

    @Override
    public boolean unSure(String id) {
        factory = Persistence.createEntityManagerFactory("ZZ");
        em = factory.createEntityManager();
        Query query2 = em.createNativeQuery("DELETE FROM zz_placeInfo_list WHERE id =?")
                .setParameter(1,id);
        query2.executeUpdate();
        em.close();
        factory.close();
        return true;
    }

    @Override
    public boolean end(String email,String place_name, String show_name) {
        factory = Persistence.createEntityManagerFactory("ZZ");
        em = factory.createEntityManager();
        Query q = em.createNativeQuery("SELECT * FROM zz_order WHERE place_name=? AND show_name=? AND state=1 AND pay_state=0", Order.class)
                .setParameter(1,place_name).setParameter(2,show_name);
        List<Order> list = q.getResultList();
        em.clear();

        double total=0;
        for(Order i:list){
            total+=i.getTotal();
        }

        q = em.createNativeQuery("UPDATE zz_order SET pay_state=1 WHERE place_name=? AND show_name=?")
                .setParameter(1,place_name).setParameter(2,show_name);
        q.executeUpdate();
        em.clear();

        q = em.createNativeQuery("SELECT * FROM zz_place WHERE `name`=?", Place.class).setParameter(1,place_name);
        Place place = (Place) q.getResultList().get(0);
        em.clear();

        q = em.createNativeQuery("UPDATE zz_place SET balance=? WHERE `name`=?")
                .setParameter(1,place.getBalance()+total*0.9).setParameter(2,place_name);
        q.executeUpdate();
        em.clear();

        Date date = new Date();
        SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");
        String time2 = sdff.format(date);

        q = em.createNativeQuery("INSERT INTO zz_change_p (belong_user,show_name,place_name,`time`,op,`change`,total,`index`) VALUES (?,?,?,?,?,?,?,?)")
                .setParameter(1,email).setParameter(2,show_name).setParameter(3,place_name)
                .setParameter(4,time2).setParameter(5,"结算").setParameter(6,"+"+total*0.9)
                .setParameter(7,place.getBalance()+total*0.9).setParameter(8,null);
        q.executeUpdate();
        em.clear();

        q = em.createNativeQuery("SELECT * FROM zz_manager", Manager.class);
        Manager manager = (Manager) q.getResultList().get(0);
        em.clear();

        q = em.createNativeQuery("INSERT INTO zz_change_m (belong_user,show_name,place_name,`time`,op,`change`,total,`index`) VALUES (?,?,?,?,?,?,?,?)")
                .setParameter(1,email).setParameter(2,show_name).setParameter(3,place_name)
                .setParameter(4,time2).setParameter(5,"结算").setParameter(6,"+"+total*0.1)
                .setParameter(7,manager.getBalance()+total*0.1).setParameter(8,null);
        q.executeUpdate();
        em.clear();

        q = em.createNativeQuery("UPDATE zz_manager SET balance = ? ").setParameter(1,manager.getBalance()+total*0.1);
        q.executeUpdate();

        em.close();
        factory.close();
        return true;
    }

    @Override
    public List<Order> getEndList() {
        factory = Persistence.createEntityManagerFactory("ZZ");
        em = factory.createEntityManager();
        Query q = em.createNativeQuery("SELECT * FROM zz_order WHERE pay_state=0", Order.class);
        List<Order> list = q.getResultList();
        List<Order> result = new ArrayList<Order>();

        HashMap<String,Order> map = new HashMap<String,Order>();
        for(Order v:list){
            if(map.containsKey(v.getShow_name())){

            }
            else {
                map.put(v.getShow_name(),v);
                result.add(v);
            }
        }

        em.close();
        factory.close();
        return result;
    }

    @Override
    public List<Change_m> getMChangeList() {
        factory = Persistence.createEntityManagerFactory("ZZ");
        em = factory.createEntityManager();
        Query q = em.createNativeQuery("SELECT * FROM zz_change_m ORDER BY `index` DESC",Change_m.class);
        List<Change_m> list = q.getResultList();
        if(list.size()==0){
            em.close();
            factory.close();
            return new ArrayList<>();
        }
        em.close();
        factory.close();
        return  list;
    }

}
