package model.entities;

import java.io.Serializable;

public class Apostador implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String name;
	
	private Apostador apostador;

	public double setSaldo;
	
	public Apostador() {
	}

	

	public Apostador(Integer id, String name, Double saldoADever, Double saldoAReceber, Double saldo,
			Department department) {
		super();
		this.id = id;
		this.name = name;
	
	}
	


	public Integer getId() {
		return id;
	}



	public void setId(Integer id) {
		this.id = id;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}


	public Apostador getApostador() {
		return apostador;
	}



	public void setApostador(Apostador apostador) {
		this.apostador = apostador;
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
		Apostador other = (Apostador) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Apostador [id=" + id + ", name=" + name +"]";
	}
}