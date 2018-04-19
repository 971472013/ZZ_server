package dao.daoImpl;

import crawler.DM_crawler;
import dao.UserDao;
import model.*;
import service.serviceImpl.UserServiceImpl;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.persistence.*;
import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhuanggangqing on 2018/2/5.
 */
public class UserDaoImpl implements UserDao{
    private static UserDaoImpl userDaoImpl = null;
    @PersistenceUnit(name = "ZZ")
    private EntityManagerFactory factory;

    @PersistenceContext
    private EntityManager em;


    private UserDaoImpl(){}

    public static UserDaoImpl getInstance(){
        if (null==userDaoImpl){
            DM_crawler dm_crawler = new DM_crawler();
            dm_crawler.crawler();
            return new UserDaoImpl();
        }
        return userDaoImpl;
    }

    @Override
    public User findUserByEmail(String email) {
        factory = Persistence.createEntityManagerFactory("ZZ");
        em = factory.createEntityManager();
        String q ="select * from zz_user where email= \""+email+"\"";
        Query query = em.createNativeQuery(q,User.class);
        List<User> result = query.getResultList();
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
    public User registerUser(User user) {
        factory = Persistence.createEntityManagerFactory("ZZ");
        em = factory.createEntityManager();
        Query query = em.createNativeQuery("insert into zz_user(email,passwd,mark,`level`,balance,discount,state,acti_code,token_exptime,consume,nickname,real_name,gender,birthday,home_place,credit) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)")
                .setParameter(1,user.getEmail()).setParameter(2,user.getPasswd())
                .setParameter(3,1).setParameter(4,1).setParameter(5,1.0).setParameter(6,1)
                .setParameter(7,0).setParameter(8,user.getActi_code()).setParameter(9,user.getToken_exptime())
                .setParameter(10,0).setParameter(11,"暂无").setParameter(12,"暂无").setParameter(13,"male")
                .setParameter(14,"2018-01-01").setParameter(15,"暂无").setParameter(16,0);
        query.executeUpdate();
        em.close();
        factory.close();
        return user;
    }

    @Override
    public User activate(String email) {
        factory = Persistence.createEntityManagerFactory("ZZ");
        em = factory.createEntityManager();
        Query query = em.createNativeQuery("UPDATE zz_user SET state = 1 WHERE email=\""+email+"\"");
        query.executeUpdate();
        em.close();
        factory.close();
        return findUserByEmail(email);
    }

    @Override
    public boolean changeInfo(User user) {
        factory = Persistence.createEntityManagerFactory("ZZ");
        em = factory.createEntityManager();
        Query query = em.createNativeQuery("UPDATE zz_user SET nickname=\""+user.getNickname()+"\",real_name=\""+user.getReal_name()+
                "\",gender=\""+user.getGender()+"\",birthday=\""+user.getBirthday()+"\",home_place=\""+user.getHome_place()+"\"" +
                " WHERE email=\""+user.getEmail()+"\"");
        query.executeUpdate();
        em.close();
        factory.close();
        return true;
    }

    @Override
    public boolean userPay_choose(User user, Show show, double total, String map, String seats) {
        factory = Persistence.createEntityManagerFactory("ZZ");
        em = factory.createEntityManager();

        double sub=0;
        boolean mark=false;
        int id = -1;
        Query query = em.createNativeQuery("SELECT * FROM zz_reduce WHERE belong_user=?",Reduce.class).setParameter(1,user.getEmail());
        List<Reduce> list = query.getResultList();
        if(list.size()!=0){
            mark=true;
            sub=10;
            id=list.get(0).getId();
        }
        em.clear();

        if(mark){
            query=em.createNativeQuery("DELETE FROM zz_reduce WHERE id=?").setParameter(1,id);
            query.executeUpdate();
            em.clear();
        }

        query = em.createNativeQuery("UPDATE zz_user SET balance=?,consume=?,`level`=?,discount=?,credit=? WHERE email=?")
                .setParameter(1,user.getBalance()-total*user.getDiscount()+sub)
                .setParameter(2,user.getConsume()+total*user.getDiscount()-sub)
                .setParameter(3,getUserLevel(user.getConsume()+total*user.getDiscount()))
                .setParameter(4,getDiscountByLevel(getUserLevel(user.getConsume()+total*user.getDiscount())))
                .setParameter(6,user.getEmail())
                .setParameter(5,user.getCredit()+(int)total*user.getDiscount()-sub);
        query.executeUpdate();
        em.clear();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");
        Date date = new Date();
        String time = sdf.format(date);
        query = em.createNativeQuery("INSERT INTO zz_order(belong_user,show_name,place_name,total,seats,`time`,create_time,cancel_time,state,pay_state,`index`) VALUES (?,?,?,?,?,?,?,?,?,?,?)")
                .setParameter(1,user.getEmail()).setParameter(2,show.getName()).setParameter(3,show.getPlace())
                .setParameter(4,total*user.getDiscount()).setParameter(5,seats).setParameter(6,show.getTime())
                .setParameter(7,time).setParameter(8,"").setParameter(9,1).setParameter(10,0).setParameter(11,null);
        query.executeUpdate();
        em.clear();

        query = em.createNativeQuery("UPDATE zz_show set map=?,checkmap=? WHERE `name`=?")
                .setParameter(1,map).setParameter(2,map).setParameter(3,show.getName());
        query.executeUpdate();
        em.clear();

        SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");
        String time2 = sdff.format(date);
        query = em.createNativeQuery("INSERT INTO zz_change(email, `time`,op,`change`,total,`index`) VALUES(?,?,?,?,?,?)")
                .setParameter(1,user.getEmail()).setParameter(2,time2).setParameter(3,"预订")
                .setParameter(4,"-"+(total*user.getDiscount()-sub)).setParameter(5,user.getBalance()-total*user.getDiscount()+sub)
        .setParameter(6,null);
        query.executeUpdate();
        em.clear();

        query = em.createNativeQuery("INSERT INTO zz_change_p (belong_user,show_name,place_name,`time`,op,`change`,total,`index`) VALUES (?,?,?,?,?,?,?,?)")
                .setParameter(1,user.getEmail()).setParameter(2,show.getName()).setParameter(3,show.getPlace())
                .setParameter(4,time2).setParameter(5,"预订").setParameter(6,"+"+total*user.getDiscount())
                .setParameter(7,-1).setParameter(8,null);
        query.executeUpdate();
        em.clear();


        em.close();
        factory.close();
        return true;
    }

    @Override
    public boolean userPay_unChoose(User user, Show show, double total,int count) {
        factory = Persistence.createEntityManagerFactory("ZZ");
        em = factory.createEntityManager();

        double sub=0;
        boolean mark=false;
        int id = -1;
        Query query = em.createNativeQuery("SELECT * FROM zz_reduce WHERE belong_user=?",Reduce.class).setParameter(1,user.getEmail());
        List<Reduce> list = query.getResultList();
        if(list.size()!=0){
            mark=true;
            sub=10;
            id=list.get(0).getId();
        }
        em.clear();

        if(mark){
            query=em.createNativeQuery("DELETE FROM zz_reduce WHERE id=?").setParameter(1,id);
            query.executeUpdate();
            em.clear();
        }

        query = em.createNativeQuery("INSERT INTO zz_un_order (belong_user,show_name,place_name,total,`count`,`index`) VALUES (?,?,?,?,?,?)")
                .setParameter(1,user.getEmail()).setParameter(2,show.getName()).setParameter(3,show.getPlace())
                .setParameter(4,total).setParameter(5,count).setParameter(6,null);
        query.executeUpdate();
        em.clear();

        query = em.createNativeQuery("UPDATE zz_user SET balance=?,consume=?,`level`=?,discount=?,credit=? WHERE email=?")
                .setParameter(1,user.getBalance()-total+sub)
                .setParameter(2,user.getConsume()+total-sub)
                .setParameter(3,getUserLevel(user.getConsume()+total))
                .setParameter(4,getDiscountByLevel(getUserLevel(user.getConsume()+total)))
                .setParameter(6,user.getEmail())
        .setParameter(5,user.getCredit()+(int)total-sub);
        query.executeUpdate();
        em.clear();

        Date date = new Date();
        SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");
        String time2 = sdff.format(date);
        query = em.createNativeQuery("INSERT INTO zz_change(email, `time`,op,`change`,total,`index`) VALUES(?,?,?,?,?,?)")
                .setParameter(1,user.getEmail()).setParameter(2,time2).setParameter(3,"预订")
                .setParameter(4,"-"+(total-sub)).setParameter(5,user.getBalance()-total+sub).setParameter(6,null);
        query.executeUpdate();
        em.clear();

        query = em.createNativeQuery("INSERT INTO zz_change_p (belong_user,show_name,place_name,`time`,op,`change`,total,`index`) VALUES (?,?,?,?,?,?,?,?)")
                .setParameter(1,user.getEmail()).setParameter(2,show.getName()).setParameter(3,show.getPlace())
                .setParameter(4,time2).setParameter(5,"预订").setParameter(6,"+"+total)
                .setParameter(7,-1).setParameter(8,null);
        query.executeUpdate();
        em.clear();


        em.close();
        factory.close();
        return true;
    }

    @Override
    public List<Order> getNormalOrderByEmail(String email) {
        factory = Persistence.createEntityManagerFactory("ZZ");
        em = factory.createEntityManager();
        String q ="select * from zz_order where belong_user= \""+email+"\" and state=1";
        Query query = em.createNativeQuery(q,Order.class);
        List<Order> result = query.getResultList();
        if(query.getResultList().size()==0){
            em.close();
            factory.close();
            return new ArrayList<Order>();
        }
        em.close();
        factory.close();
        return result;
    }

    @Override
    public boolean cancelOrderByShowName(User user,String email,String showName) {
        factory = Persistence.createEntityManagerFactory("ZZ");
        em = factory.createEntityManager();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");
        Date date = new Date();
        String time = sdf.format(date);
        Query query = em.createNativeQuery("UPDATE zz_order SET state=2,cancel_time=? WHERE belong_user=?")
                .setParameter(1,time).setParameter(2,email);
        query.executeUpdate();
        em.clear();

        query = em.createNativeQuery("SELECT * FROM zz_order where belong_user=\""+email+"\" and show_name=\""+showName+"\"",Order.class);
        Order order = (Order) query.getResultList().get(0);
        em.clear();

        query = em.createNativeQuery("UPDATE zz_user SET balance=? WHERE email=?")
                .setParameter(1,user.getBalance()+order.getTotal()*0.8).setParameter(2,email);
        query.executeUpdate();
        em.clear();

        SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");
        String time2 = sdff.format(date);
        query = em.createNativeQuery("INSERT INTO zz_change(email, `time`,op,`change`,total,`index`) VALUES(?,?,?,?,?,?)")
                .setParameter(1,user.getEmail()).setParameter(2,time2).setParameter(3,"撤销")
                .setParameter(4,"+"+order.getTotal()*0.8).setParameter(5,user.getBalance()+order.getTotal()*0.8)
        .setParameter(6,null);
        query.executeUpdate();
        em.clear();

        query = em.createNativeQuery("SELECT * FROM zz_place WHERE `name`=?", Place.class).setParameter(1,order.getPlace_name());
        Place place = (Place) query.getResultList().get(0);
        em.clear();

        query = em.createNativeQuery("INSERT INTO zz_change_p (belong_user,show_name,place_name,`time`,op,`change`,total,`index`) VALUES (?,?,?,?,?,?,?,?)")
                .setParameter(1,user.getEmail()).setParameter(2,showName).setParameter(3,place.getName())
                .setParameter(4,time2).setParameter(5,"撤销").setParameter(6,"-"+order.getTotal()*user.getDiscount()*0.8)
                .setParameter(7,-1).setParameter(8,null);
        query.executeUpdate();
        em.clear();

        em.close();
        factory.close();
        return true;
    }

    @Override
    public List<Order> getCancelOrderByEmail(String email) {
        factory = Persistence.createEntityManagerFactory("ZZ");
        em = factory.createEntityManager();
        String q ="select * from zz_order where belong_user= \""+email+"\" and state=2";
        Query query = em.createNativeQuery(q,Order.class);
        List<Order> result = query.getResultList();
        if(query.getResultList().size()==0){
            em.close();
            factory.close();
            return new ArrayList<Order>();
        }
        em.close();
        factory.close();
        return result;
    }

    @Override
    public List<Change> getChangeList(String email) {
        factory = Persistence.createEntityManagerFactory("ZZ");
        em = factory.createEntityManager();
        String q ="select * from zz_change where email= \""+email+"\" ORDER BY `index` DESC ";
        Query query = em.createNativeQuery(q,Change.class);
        List<Change> result = query.getResultList();
        if(query.getResultList().size()==0){
            em.close();
            factory.close();
            return new ArrayList<Change>();
        }
        em.close();
        factory.close();
        return result;
    }

    @Override
    public boolean XU(String email) {
        factory = Persistence.createEntityManagerFactory("ZZ");
        em = factory.createEntityManager();
        Query q = em.createNativeQuery("UPDATE zz_user SET mark=0 WHERE email=?").setParameter(1,email);
        q.executeUpdate();
        em.close();
        factory.close();
        return true;
    }

    @Override
    public List<Reduce> getReduceList(String email) {
        factory = Persistence.createEntityManagerFactory("ZZ");
        em = factory.createEntityManager();
        Query q = em.createNativeQuery("SELECT * FROM zz_reduce WHERE belong_user=?",Reduce.class).setParameter(1,email);
        List<Reduce> list = q.getResultList();
        if(list.size()==0){
            em.close();
            factory.close();
            return new ArrayList<>();
        }
        em.close();
        factory.close();
        return list;
    }

    @Override
    public boolean forReduce(User user) {
        factory = Persistence.createEntityManagerFactory("ZZ");
        em = factory.createEntityManager();
        Query q = em.createNativeQuery("UPDATE zz_user SET credit=? WHERE email=?")
                .setParameter(1,user.getCredit()-300).setParameter(2,user.getEmail());
        q.executeUpdate();
        em.clear();

        q = em.createNativeQuery("INSERT into zz_reduce(id,belong_user,de,tot,sub) VALUES (?,?,?,?,?)")
                .setParameter(1,null).setParameter(2,user.getEmail()).setParameter(3,"满100减10")
                .setParameter(4,100).setParameter(5,10);
        q.executeUpdate();

        em.close();
        factory.close();
        return true;
    }

    @Override
    public List<Un_Order> getUOrder(String email) {
        factory = Persistence.createEntityManagerFactory("ZZ");
        em = factory.createEntityManager();
        Query q = em.createNativeQuery("SELECT * FROM zz_un_order WHERE belong_user=?",Un_Order.class).setParameter(1,email);
        List<Un_Order> list = q.getResultList();
        return list;
    }

    private int getUserLevel(double consume){
        if(consume<500){
            return 1;
        }
        else if(consume<2500){
            return 2;
        }
        else if(consume<4500){
            return 3;
        }
        else{
            return 4;
        }
    }

    private double getDiscountByLevel(int level){
        double[] t = {0.98,0.95,0.92,0.89,0.86,0.83};
        return t[level-1];
    }
}
