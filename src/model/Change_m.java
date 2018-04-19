package model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by zhuanggangqing on 2018/4/3.
 */
@Data
@Entity
@Table(name = "zz_change_m")
public class Change_m implements Serializable{
    private String belong_user;
    private String show_name;
    private String place_name;
    private String time;
    private String op;
    private String change;
    private double total;
    @Id
    private int index;
}
