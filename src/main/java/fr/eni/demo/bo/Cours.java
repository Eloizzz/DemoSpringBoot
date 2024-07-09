package fr.eni.demo.bo;

import java.io.Serializable;

public class Cours implements Serializable {
	/**
	 * Identifiant de l'interface Serializable
	 */
	private static final long serialVersionUID = 1L;

	private long id;
	private String titre;
	private int duree;

	public Cours() {
	}

	public Cours(long id, String titre, int duree) {
		this.id = id;
		this.titre = titre;
		this.duree = duree;
	}

	// + Getter/Setter
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public int getDuree() {
		return duree;
	}

	public void setDuree(int duree) {
		this.duree = duree;
	}

	// toString()
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Cours [id=");
		builder.append(id);
		builder.append(", titre=");
		builder.append(titre);
		builder.append(", duree=");
		builder.append(duree);
		builder.append("]");
		return builder.toString();
	}

}
