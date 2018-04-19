package dao;

import model.Show;

import java.util.List;

/**
 * Created by zhuanggangqing on 2018/3/5.
 */
public interface ShowDao {
    public List<Show> findAllShow();
    public Show findShowByName(String name);
    public List<Show> findAllShowsByPlace(String place);
}
