package model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by zhuanggangqing on 2018/3/30.
 */
@Data
@Entity
@Table(name = "zz_order")
public class Order implements Serializable{
    private String belong_user;
    private String show_name;
    private String place_name;
    private double total;
    private String seats;
    private String time;
    private String create_time;
    private String cancel_time;
    private int state;     //  1:已支付;2:已撤销
    private int pay_state;   //  0:未结算;1:已结算
    @Id
    private int index;
}
