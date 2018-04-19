package service;

import model.Show;

import javax.ejb.Remote;
import java.util.List;

/**
 * Created by zhuanggangqing on 2018/3/5.
 */
@Remote
public interface ShowService {
    public List<Show> findAllShowByPage(int page);
    public int getShowNumbers();
    public Show findShowByName(String name);
    public List<Show> findAllShowsByPlace(String place);
}
