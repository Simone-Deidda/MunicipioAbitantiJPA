package it.prova.municipioAbitanteJPA.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "abitante")
public class Abitante {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(name = "nome")
	private String nome;
	@Column(name = "cognome")
	private String cognome;
	@Column(name = "eta")
	private Integer eta;
	@Column(name = "residenza")
	private String residenza;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "municipio_id")
	private Municipio municipio;

	public Abitante() {

	}

	public Abitante(String nome, String cognome, Integer eta, String residenza) {
		super();
		this.nome = nome;
		this.cognome = cognome;
		this.eta = eta;
		this.residenza = residenza;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public Integer getEta() {
		return eta;
	}

	public void setEta(Integer eta) {
		this.eta = eta;
	}

	public String getResidenza() {
		return residenza;
	}

	public void setResidenza(String residenza) {
		this.residenza = residenza;
	}

	public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}

	@Override
	public String toString() {
		return "Abitante [id=" + id + ", nome=" + nome + ", cognome=" + cognome + ", eta=" + eta + ", residenza="
				+ residenza + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(cognome, eta, id, municipio, nome, residenza);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Abitante other = (Abitante) obj;
		return Objects.equals(cognome, other.cognome) && Objects.equals(eta, other.eta) && Objects.equals(id, other.id)
				&& Objects.equals(municipio, other.municipio) && Objects.equals(nome, other.nome)
				&& Objects.equals(residenza, other.residenza);
	}

}
