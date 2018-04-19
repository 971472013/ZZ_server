package service;

import model.*;

import javax.ejb.Remote;
import java.util.List;

/**
 * Created by zhuanggangqing on 2018/2/6.
 */
@Remote
public interface UserService {
    public User findUserByEmail(String email);
    public User registerUser(User user);
    public User activate(String email);
    public boolean changeInfo(User user);
    public boolean userPay_choose(User user, Show show, double total, String map, String seats);
    public boolean userPay_unChoose(User user,Show show,double total,int count);
    public List<Order> getNormalOrderByEmail(String email);
    public boolean cancelOrderByShowName(String email,String showName);
    public List<Order> getCancelOrderByEmail(String email);
    public List<Change> getChangeList(String email);
    public boolean XU(String email);
    public List<Reduce> getReduceList(String email);
    public boolean forReduce(User user);
    public List<Un_Order> getUOrder(String email);
}
