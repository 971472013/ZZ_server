package service;

import model.Change_m;
import model.Place;

import javax.ejb.Remote;
import java.util.List;

/**
 * Created by zhuanggangqing on 2018/3/15.
 */
@Remote
public interface PlaceService {
    public Place getPlaceByName(String name);
    public Place getPlaceById(String id);
    public boolean registerPlace(Place place);
    public boolean apply_change(Place place);
    public boolean checkin(String showName,String checkMap);
    public List<Change_m> getPChangeList(String name);
    public boolean addShow(String name,String time,String type,String intro,String img,Place p);
}
