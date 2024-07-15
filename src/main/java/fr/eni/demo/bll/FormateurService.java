package fr.eni.demo.bll;

import java.util.List;

import fr.eni.demo.bo.Formateur;

public interface FormateurService {
	
	void add(Formateur formateur);

	List<Formateur> getFormateurs();
	
	Formateur findByEmail(String emailFormateur);
	
	void update(Formateur formateur);
	
	void updateCoursFormateur(String emailFormateur, long idCours);
}
