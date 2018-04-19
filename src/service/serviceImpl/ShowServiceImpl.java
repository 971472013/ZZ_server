package service.serviceImpl;

import dao.ShowDao;
import dao.daoImpl.ShowDaoImpl;
import model.Show;
import service.ShowService;

import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuanggangqing on 2018/3/5.
 */
@Stateless
public class ShowServiceImpl implements ShowService{
    @Override
    public List<Show> findAllShowByPage(int page) {
        List<Show> list = ShowDaoImpl.getInstance().findAllShow();
        List<Show> result = new ArrayList<Show>();
        int index = (page-1)*50;
        int count = 0;
        for(Show i:list){
            if(count >= 50){
                break;
            }
            if(index == 0){
                result.add(i);
                count++;
                continue;
            }
            index--;
        }
        return result;
    }

    @Override
    public int getShowNumbers() {
        return ShowDaoImpl.getInstance().findAllShow().size();
    }

    @Override
    public Show findShowByName(String name) {
        return ShowDaoImpl.getInstance().findShowByName(name);
    }

    @Override
    public List<Show> findAllShowsByPlace(String place) {
        return ShowDaoImpl.getInstance().findAllShowsByPlace(place);
    }
}
