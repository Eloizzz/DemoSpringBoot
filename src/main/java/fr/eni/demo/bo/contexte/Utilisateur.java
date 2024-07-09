package fr.eni.demo.bo.contexte;

import java.io.Serializable;

public class Utilisateur implements Serializable {
	private static final long serialVersionUID = 1L;
	private String pseudo;

	public Utilisateur() {
	}

	public Utilisateur(String pseudo) {
		this.pseudo = pseudo;
	}

	// + Getter/Setter
	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	// + toString()
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Utilisateur [pseudo=");
		builder.append(pseudo);
		builder.append("]");
		return builder.toString();
	}

}
