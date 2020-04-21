package model.services;

import java.util.List;

import model.dao.ApostasDao;
import model.dao.DaoFactory;
import model.entities.Apostas;

public class ApostasService {

	private ApostasDao dao = DaoFactory.createApostasDao();

	public List<Apostas> findAll() {

		return dao.findAll();

	}
	
	public void saveOrUpdate(Apostas obj) {
		if(obj.getId() == null) {
			dao.insert(obj);
		}else {
			dao.update(obj);
		}
	}
	
	public void remove(Apostas obj) {
		dao.deleteById(obj.getId());
	}

}
