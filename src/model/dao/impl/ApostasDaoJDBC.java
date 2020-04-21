package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.Statement;

import db.DB;
import db.DbException;
import model.dao.ApostasDao;
import model.entities.Apostador;
import model.entities.Apostas;

public class ApostasDaoJDBC implements ApostasDao {

	private Connection conn;

	public ApostasDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Apostas obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO Apostas \" + \"(SaldoADever, SaldoAReceber, Saldo) \" + \"VALUES \" + \"(?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);

			st.setDouble(1, obj.getSaldoADever());
			st.setDouble(2, obj.getSaldoAReceber());
			st.setDouble(3, obj.getSaldo());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			} else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Apostas obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE Apostas "
					+ "INSERT INTO Apostas \" + \"(SaldoADever, SaldoAReceber, Saldo) \" + \"VALUES \" + \"(?, ?, ?)");

			st.setDouble(1, obj.getSaldoADever());
			st.setDouble(2, obj.getSaldoAReceber());
			st.setDouble(3, obj.getSaldo());

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM Apostas WHERE Id = ?");

			st.setInt(1, id);

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Apostas findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM apostador\\r\\n\" + \"INNER JOIN apostas\\r\\n\"\r\n"
					+ "					+ \"ON apostador.Id = apostas.fk_apostador;\" + \"WHERE Apostas.Id = ?");

			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Apostador dep = instantiateApostador(rs);
				Apostas obj = instantiateApostas(rs, dep);
				return obj;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	private Apostas instantiateApostas(ResultSet rs, Apostador dep) throws SQLException {
		Apostas obj = new Apostas();

		obj.setId(rs.getInt("Id"));
		obj.setSaldoADever(rs.getDouble("SaldoADever"));
		obj.setSaldoAReceber(rs.getDouble("SaldoAReceber"));
		obj.setSaldo(rs.getDouble("Saldo"));

		return obj;
	}

	private Apostador instantiateApostador(ResultSet rs) throws SQLException {
		Apostador dep = new Apostador();
		dep.setId(rs.getInt("ApostadorId"));
		dep.setName(rs.getString("Name"));
		return dep;
	}

	@Override
	public List<Apostas> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT Apostas.*,Apostador.Name as DepName " + "FROM Apostas INNER JOIN Apostador "
							+ "ON Apostas.ApostadorId = Apostador.Id " + "ORDER BY Name");

			rs = st.executeQuery();

			List<Apostas> list = new ArrayList<>();
			Map<Integer, Apostador> map = new HashMap<>();

			while (rs.next()) {

				Apostador dep = map.get(rs.getInt("ApostadorId"));

				if (dep == null) {
					dep = instantiateApostador(rs);
					map.put(rs.getInt("ApostadorId"), dep);
				}

				Apostas obj = instantiateApostas(rs, dep);
				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Apostas> findByApostador(Apostador apostador) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * FROM apostador INNER JOIN apostas ON apostador.Id = apostas.fk_apostador; "
							+ "ORDER BY Name");

			st.setInt(1, apostador.getId());

			rs = st.executeQuery();

			List<Apostas> list = new ArrayList<>();
			Map<Integer, Apostador> map = new HashMap<>();

			while (rs.next()) {

				Apostador dep = map.get(rs.getInt("ApostadorId"));

				if (dep == null) {
					dep = instantiateApostador(rs);
					map.put(rs.getInt("ApostadorId"), dep);
				}

				Apostas obj = instantiateApostas(rs, dep);
				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
}