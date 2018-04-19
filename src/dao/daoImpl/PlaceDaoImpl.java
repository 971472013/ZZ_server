package dao.daoImpl;

import dao.PlaceDao;
import model.Change_m;
import model.Place;
import model.Show;
import other.Map;
import service.PlaceService;
import service.serviceImpl.PlaceServiceImpl;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by zhuanggangqing on 2018/3/16.
 */
public class PlaceDaoImpl implements PlaceDao {
    private static PlaceDao placeDao;
    private PlaceDaoImpl(){}
    public static PlaceDao getInstance(){
        if (placeDao == null){
            placeDao = new PlaceDaoImpl();
        }
        return placeDao;
    }

    @PersistenceUnit(name = "ZZ")
    private EntityManagerFactory factory;

    @PersistenceContext
    private EntityManager em;

    @Override
    public Place getPlaceByName(String name) {
        factory = Persistence.createEntityManagerFactory("ZZ");
        em = factory.createEntityManager();
        String q ="select * from zz_place WHERE `name`=\""+name+"\"";
        Query query = em.createNativeQuery(q, Place.class);
        List<Place> result = query.getResultList();
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
    public Place getPlaceById(String id) {
        factory = Persistence.createEntityManagerFactory("ZZ");
        em = factory.createEntityManager();
        String q ="select * from zz_place WHERE `id`=\""+id+"\"";
        Query query = em.createNativeQuery(q, Place.class);
        List<Place> result = query.getResultList();
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
    public boolean registerPlace(Place place) {
        factory = Persistence.createEntityManagerFactory("ZZ");
        em = factory.createEntityManager();
        Query query = em.createNativeQuery("INSERT INTO zz_place(name,city,introduction,img,id,passwd,state,balance) VALUES (?,?,?,?,?,?,?,?)")
                .setParameter(1,place.getName()).setParameter(3,"").setParameter(2,place.getCity())
                .setParameter(4,place.getImg()).setParameter(5,get_random_id()).setParameter(6,place.getPasswd())
                .setParameter(7,0).setParameter(8,0);
        query.executeUpdate();
        em.close();
        factory.close();
        return true;
    }

    @Override
    public boolean apply_change(Place place,Place oldPlace) {
        factory = Persistence.createEntityManagerFactory("ZZ");
        em = factory.createEntityManager();
        Query query = em.createNativeQuery("INSERT into zz_placeInfo_list(`name`,city,introduction,img,id,passwd,state,balance,n) VALUES (?,?,?,?,?,?,?,?,?)")
                .setParameter(1,place.getName()).setParameter(2,place.getCity()).setParameter(3,place.getIntroduction())
                .setParameter(4,place.getImg()).setParameter(6,place.getPasswd()).setParameter(5,place.getId()).setParameter(7,1).setParameter(8,place.getBalance()).setParameter(9,null);
        query.executeUpdate();
        em.clear();
        query = em.createNativeQuery("INSERT into zz_placeInfo_list(`name`,city,introduction,img,id,passwd,state,balance,n) VALUES (?,?,?,?,?,?,?,?,?)")
                .setParameter(1,oldPlace.getName()).setParameter(2,oldPlace.getCity()).setParameter(3, oldPlace.getIntroduction())
                .setParameter(4,oldPlace.getImg()).setParameter(6,oldPlace.getPasswd()).setParameter(5,oldPlace.getId()).setParameter(7,0).setParameter(8,oldPlace.getBalance()).setParameter(9,null);
        query.executeUpdate();
        em.close();
        factory.close();
        return true;
    }

    @Override
    public boolean checkin(String showName, String checkMap) {
        factory = Persistence.createEntityManagerFactory("ZZ");
        em = factory.createEntityManager();
        Query query = em.createNativeQuery("UPDATE zz_show SET checkmap=? WHERE `name`=?")
                .setParameter(1,checkMap).setParameter(2,showName);
        query.executeUpdate();
        em.close();
        factory.close();
        return true;
    }

    @Override
    public List<Change_m> getPChangeList(String name) {
        factory = Persistence.createEntityManagerFactory("ZZ");
        em = factory.createEntityManager();
        Query q = em.createNativeQuery("SELECT * FROM zz_change_p WHERE place_name=? ORDER BY `index` DESC ",Change_m.class).setParameter(1,name);
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

    @Override
    public boolean addShow(String name, String time, String type, String intro, String img, Place p) {
        factory = Persistence.createEntityManagerFactory("ZZ");
        em = factory.createEntityManager();
        Query q = em.createNativeQuery("INSERT into zz_show(`name`,`time`,place,price,type,introduction,img,city,map,checkmap) VALUES (?,?,?,?,?,?,?,?,?,?)")
                .setParameter(1,name).setParameter(2,time).setParameter(3,p.getName()).setParameter(5,type)
                .setParameter(6,intro).setParameter(7,img).setParameter(8,p.getCity()).setParameter(4,"80,180,360,540")
                .setParameter(9, Map.getMap()).setParameter(10,Map.getMap());
        q.executeUpdate();
        em.close();
        factory.close();
        return true;
    }

    public static String get_random_id(){
        String a = "";
        while (a.length()<7){
            a = a+new Random().nextInt(10);
        }
        return a;
    }
}