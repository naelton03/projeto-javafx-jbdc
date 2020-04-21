package model.dao;

import java.util.List;

import model.entities.Apostador;
import model.entities.Apostas;

public interface ApostasDao {

	void insert(Apostas obj);
	void update(Apostas obj);
	void deleteById(Integer id);
	Apostas findById(Integer id);
	List<Apostas> findAll();
	List<Apostas> findByApostador(Apostador apostador);
	
}
