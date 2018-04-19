package model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by zhuanggangqing on 2018/4/4.
 */
@Data
@Entity
@Table(name = "zz_un_order")
public class Un_Order implements Serializable{
    private String belong_user;
    private String show_name;
    private String place_name;
    private double total;
    private int count;
    @Id
    private int index;
}
