package model.dao;

import java.util.List;

import model.entities.Apostador;

public interface ApostadorDao {

	void insert(Apostador obj);
	void update(Apostador obj);
	void deleteById(Integer id);
	Apostador findById(Integer id);
	List<Apostador> findAll();

}