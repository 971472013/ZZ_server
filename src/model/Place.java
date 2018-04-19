package model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by zhuanggangqing on 2018/3/15.
 */
@Data
@Entity
@Table(name = "zz_place")
public class Place implements Serializable{
    @Id
    private String name;
    private String introduction;
    private String city;
    private String img;
    private String id;
    private String passwd;
    private int state;
    private double balance;
}
