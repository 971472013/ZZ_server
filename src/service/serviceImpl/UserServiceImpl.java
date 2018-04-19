package service.serviceImpl;

import dao.UserDao;
import dao.daoImpl.UserDaoImpl;
import model.*;
import other.Email;
import service.UserService;

import javax.ejb.Stateless;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by zhuanggangqing on 2018/2/6.
 */
@Stateless
public class UserServiceImpl implements UserService {
    @Override
    public User findUserByEmail(String email) {
        return UserDaoImpl.getInstance().findUserByEmail(email);
    }

    @Override
    public User registerUser(User user) {
        User U = UserDaoImpl.getInstance().registerUser(user);

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

        StringBuffer sb=new StringBuffer("<div style=\"width:660px;overflow:hidden;border-bottom:1px solid #bdbdbe;\"><div style=\"height:52px;overflow:hidden;border:1px solid #464c51;background:#353b3f url(http://www.lofter.com/rsc/img/email/hdbg.png);\"><a href=\"http://www.lofter.com?mail=qbclickbynoticemail_20120626_01\" target=\"_blank\" style=\"display:block;width:144px;height:34px;margin:10px 0 0 20px;overflow:hidden;text-indent:-2000px;background:url(http://www.lofter.com/rsc/img/email/logo.png) no-repeat;\">珍珠网</a></div>"+"<div style=\"padding:24px 20px;\">您好，"+user.getEmail()+"<br/><br/>珍珠网是一款\"演出订票、场馆管理\"的在线信息交换平台，旨在为\"喜爱观看演出的你/拥有演出场地而无法宣传的你\"打造一个全新的演出信息展示平台！<br/><br/>请点击下面链接激活账号，24小时生效，否则重新注册账号，链接只能使用一次，请尽快激活！</br>");
        sb.append("<a href=\"http://localhost:8000/EmailActivate?email=");
        sb.append(user.getEmail());
        sb.append("&acti_code=");
        sb.append(user.getActi_code());
        sb.append("\">http://localhost:8000/EmailActivate?email=");
        sb.append(user.getEmail());
        sb.append("&acti_code=");
        sb.append(user.getActi_code());
        sb.append("</a>"+"<br/>如果以上链接无法点击，请把上面网页地址复制到浏览器地址栏中打开<br/><br/><br/>珍珠网，演出订票，一张搞定<br/>"+sdf.format(new Date())+ "</div></div>" );

        //发送邮件
        Email.sendEmail(user.getEmail(), sb.toString());
        return U;
    }

    @Override
    public User activate(String email) {
        return UserDaoImpl.getInstance().activate(email);
    }

    @Override
    public boolean changeInfo(User user) {
        return UserDaoImpl.getInstance().changeInfo(user);
    }

    @Override
    public boolean userPay_choose(User user, Show show, double total, String map, String seats) {
        return UserDaoImpl.getInstance().userPay_choose(user,show,total,map,seats);
    }

    @Override
    public boolean userPay_unChoose(User user, Show show, double total,int count) {
        return UserDaoImpl.getInstance().userPay_unChoose(user,show,total,count);
    }

    @Override
    public List<Order> getNormalOrderByEmail(String email) {
        return UserDaoImpl.getInstance().getNormalOrderByEmail(email);
    }

    @Override
    public boolean cancelOrderByShowName(String email,String showName) {
        return UserDaoImpl.getInstance().cancelOrderByShowName(UserDaoImpl.getInstance().findUserByEmail(email),email,showName);
    }

    @Override
    public List<Order> getCancelOrderByEmail(String email) {
        return UserDaoImpl.getInstance().getCancelOrderByEmail(email);
    }

    @Override
    public List<Change> getChangeList(String email) {
        return UserDaoImpl.getInstance().getChangeList(email);
    }

    @Override
    public boolean XU(String email) {
        return UserDaoImpl.getInstance().XU(email);
    }

    @Override
    public List<Reduce> getReduceList(String email) {
        return UserDaoImpl.getInstance().getReduceList(email);
    }

    @Override
    public boolean forReduce(User user) {
        return UserDaoImpl.getInstance().forReduce(user);
    }

    @Override
    public List<Un_Order> getUOrder(String email) {
        return UserDaoImpl.getInstance().getUOrder(email);
    }


}
