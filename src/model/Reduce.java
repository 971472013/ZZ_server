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
@Table(name = "zz_reduce")
public class Reduce implements Serializable{
    @Id
    private int id;
    private String belong_user;
    private String de;
    private double tot;
    private double sub;
}
