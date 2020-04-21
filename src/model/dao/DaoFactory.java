package model.dao;

import db.DB;
import model.dao.impl.ApostadorDaoJDBC;
import model.dao.impl.ApostasDaoJDBC;
import model.dao.impl.DepartmentDaoJDBC;

public class DaoFactory {

	public static ApostadorDao createApostadorDao() {
		return new ApostadorDaoJDBC(DB.getConnection());
	}
	
	public static ApostasDao createApostasDao() {
		return new ApostasDaoJDBC(DB.getConnection());
	}
	
	public static DepartmentDao createDepartmentDao() {
		return new DepartmentDaoJDBC(DB.getConnection());
	}
}