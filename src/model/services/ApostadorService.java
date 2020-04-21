package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.ApostadorDao;
import model.entities.Apostador;

public class ApostadorService {

	private ApostadorDao dao = DaoFactory.createApostadorDao();

	public List<Apostador> findAll() {

		return dao.findAll();

	}

	public void saveOrUpdate(Apostador obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

	public void remove(Apostador obj) {
		dao.deleteById(obj.getId());
	}

}
