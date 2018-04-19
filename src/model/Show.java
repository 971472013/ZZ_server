package model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Date;

/**
 * Created by zhuanggangqing on 2018/2/23.
 */
@Data
@Entity
@Table(name = "zz_show")
public class Show implements Serializable{
    @Id
    private String name;
    private String time;
    private String place;
    private String price;
    private String type;
    private String introduction;
    private String img;
    private String city;
    private String map;
    private String checkmap;
//    private String mark;
}
