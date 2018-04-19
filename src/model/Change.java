package model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by zhuanggangqing on 2018/4/1.
 */
@Entity
@Table(name = "zz_change")
@Data
public class Change implements Serializable {
    private String email;
    private String time;
    private String op;
    private String change;
    private double total;
    @Id
    private int index;
}
