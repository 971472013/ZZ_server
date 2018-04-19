package model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by zhuanggangqing on 2018/2/5.
 */
@Data
@Entity
@Table(name = "zz_user")
public class User implements Serializable{
    private static final long serialVersionUID = 1L;
//private static final long serialVersionUID = -558553967080513790L;
    @Id
//    @Column(name = "email")
    private String email;
//    @Column(name = "passwd")
    private String passwd;
//    @Column(name = "mark")
    private int mark;
//    @Column(name = "level")
    private int level;
//    @Column(name = "balance")
    private double balance;

    private double discount;

    private int state;

    private String acti_code;

    private String token_exptime;

    private String nickname;

    private String real_name;

    private double consume;

    private String gender;

    private String birthday;

    private String home_place;

    private int credit;
}
