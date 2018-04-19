package dao;

import model.Change_m;
import model.Manager;
import model.Order;
import model.Place;

import java.util.List;

/**
 * Created by zhuanggangqing on 2018/3/26.
 */
public interface ManagerDao {
    public Manager findManagerByEmail(String email);
    public List<Place> getAllPlace_registering();
    public boolean accept_register(String name);
    public List<Place> getAllPlace_changeInfo();
    public List<Place> getChangeInfoByID(String id);
    public boolean sure(String id);
    public boolean unSure(String id);
    public boolean end(String email,String place_name,String show_name);
    public List<Order> getEndList();
    public List<Change_m> getMChangeList();
}
