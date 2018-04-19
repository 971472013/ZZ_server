package model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by zhuanggangqing on 2018/2/22.
 */
@Data
@Entity
@Table(name = "zz_manager")
public class Manager implements Serializable{
    @Id
    private String email;
    private String password;
    private double balance;
}
