package model.entities;

import java.io.Serializable;

public class Apostas implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private Double saldoADever;
	private Double saldoAReceber;
	private Double saldo;
	
	public Apostas() {
	}

	

	public Integer getId() {
		return id;
	}



	public void setId(Integer id) {
		this.id = id;
	}



	public Double getSaldoADever() {
		return saldoADever;
	}



	public void setSaldoADever(Double saldoADever) {
		this.saldoADever = saldoADever;
	}



	public Double getSaldoAReceber() {
		return saldoAReceber;
	}



	public void setSaldoAReceber(Double saldoAReceber) {
		this.saldoAReceber = saldoAReceber;
	}



	public Double getSaldo() {
		return saldo;
	}



	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Apostas other = (Apostas) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Department [id=" + id + ", saldoADever=" + saldoADever + ", saldoAReceber=" + saldoAReceber +", saldo=" + saldo +" ]";
	}
}
