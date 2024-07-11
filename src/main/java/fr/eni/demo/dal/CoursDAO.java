package fr.eni.demo.dal;

import java.util.List;
import fr.eni.demo.bo.Cours;

public interface CoursDAO {
	Cours read(long id);

	List<Cours> findAll();

	List<Cours> findByFormateur(String emailFormateur);

	void insertCoursFormateur(long idCours, String emailFormateur);
}