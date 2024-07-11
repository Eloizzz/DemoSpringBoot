package fr.eni.demo.bll;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.eni.demo.bo.Cours;
import fr.eni.demo.bo.Formateur;
import fr.eni.demo.dal.CoursDAO;
import fr.eni.demo.dal.FormateurDAO;

@Service
public class FormateurServiceImpl implements FormateurService {
	private FormateurDAO formateurDAO;
	private CoursDAO coursDAO;

	public FormateurServiceImpl(FormateurDAO formateurDAO, CoursDAO coursDAO) {
		this.formateurDAO = formateurDAO;
		this.coursDAO = coursDAO;
	}

	@Override
	public void add(String nom, String prenom, String email) {
		Formateur formateur = new Formateur(nom, prenom, email);
		formateurDAO.create(formateur);
	}

	@Override
	public void add(Formateur formateur) {
		// unicité de l'email
		Formateur formateurAvecEmailIdentique = formateurDAO.read(formateur.getEmail());
		if (formateurAvecEmailIdentique == null) {
			formateurDAO.create(formateur);
		}
	}

	@Override
	public List<Formateur> getFormateurs() {
		return formateurDAO.findAll();
	}

	@Override
	public Formateur findByEmail(String emailFormateur) {
		// Il nous faut le formateur et les cours associés
		Formateur f = formateurDAO.read(emailFormateur);
		//Récupérons les cours associés à ce formateur
		List<Cours> coursDispenses = coursDAO.findByFormateur(emailFormateur);
		//attribuons les cours s'il en existe
		if(coursDispenses != null) {
			f.setListeCours(coursDispenses); 
		}
		return f;
	}

	public void update(Formateur formateur) {
		formateurDAO.update(formateur);
	}

	@Override
	public void updateCoursFormateur(String emailFormateur, long idCours) {
		// Mise à jour au niveau BO
		Formateur f = formateurDAO.read(emailFormateur);
		Cours c = coursDAO.read(idCours);
		f.getListeCours().add(c);

		// Mise à jour en base
		coursDAO.insertCoursFormateur(idCours, emailFormateur);
	}

}
