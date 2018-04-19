package service.serviceImpl;

import dao.daoImpl.PlaceDaoImpl;
import model.Change_m;
import model.Place;
import service.PlaceService;

import javax.ejb.Stateless;
import java.util.List;

/**
 * Created by zhuanggangqing on 2018/3/16.
 */
@Stateless
public class PlaceServiceImpl implements PlaceService {
    @Override
    public Place getPlaceByName(String name) {
        return PlaceDaoImpl.getInstance().getPlaceByName(name);
    }

    @Override
    public Place getPlaceById(String id) {
        return PlaceDaoImpl.getInstance().getPlaceById(id);
    }

    @Override
    public boolean registerPlace(Place place) {
        return PlaceDaoImpl.getInstance().registerPlace(place);
    }

    @Override
    public boolean apply_change(Place place) {
        Place oldPlace = getPlaceById(place.getId());
        return PlaceDaoImpl.getInstance().apply_change(place,oldPlace);
    }

    @Override
    public boolean checkin(String showName, String checkMap) {
        return PlaceDaoImpl.getInstance().checkin(showName,checkMap);
    }

    @Override
    public List<Change_m> getPChangeList(String name) {
        return PlaceDaoImpl.getInstance().getPChangeList(name);
    }

    @Override
    public boolean addShow(String name, String time, String type, String intro, String img, Place p) {
        return PlaceDaoImpl.getInstance().addShow(name,time,type,intro,img,p);
    }
}
