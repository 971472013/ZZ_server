package service.serviceImpl;

import dao.daoImpl.ManagerDaoImpl;
import model.Change_m;
import model.Manager;
import model.Order;
import model.Place;
import service.ManagerService;

import javax.ejb.Stateless;
import java.util.List;

/**
 * Created by zhuanggangqing on 2018/3/26.
 */
@Stateless
public class ManagerServiceImpl implements ManagerService{

    @Override
    public Manager findManagerByEmail(String email) {
        return ManagerDaoImpl.getInstance().findManagerByEmail(email);
    }

    @Override
    public List<Place> getAllPlace_registering() {
        return ManagerDaoImpl.getInstance().getAllPlace_registering();
    }

    @Override
    public boolean accept_register(String name) {
        return ManagerDaoImpl.getInstance().accept_register(name);
    }

    @Override
    public List<Place> getAllPlace_changeInfo() {
        return ManagerDaoImpl.getInstance().getAllPlace_changeInfo();
    }

    @Override
    public List<Place> getChangeInfoByID(String id) {
        return ManagerDaoImpl.getInstance().getChangeInfoByID(id);
    }

    @Override
    public boolean sure(String id) {
        return ManagerDaoImpl.getInstance().sure(id);
    }

    @Override
    public boolean unSure(String id) {
        return ManagerDaoImpl.getInstance().unSure(id);
    }

    @Override
    public boolean end(String email,String place_name, String show_name) {
        return ManagerDaoImpl.getInstance().end(email,place_name,show_name);
    }

    @Override
    public List<Order> getEndList() {
        return ManagerDaoImpl.getInstance().getEndList();
    }

    @Override
    public List<Change_m> getMChangeList() {
        return ManagerDaoImpl.getInstance().getMChangeList();
    }
}
