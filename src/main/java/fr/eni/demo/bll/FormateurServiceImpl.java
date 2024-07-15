package fr.eni.demo.bll;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.eni.demo.bo.Cours;
import fr.eni.demo.bo.Formateur;
import fr.eni.demo.dal.CoursDAO;
import fr.eni.demo.dal.FormateurDAO;
import fr.eni.demo.exceptions.BusinessCode;
import fr.eni.demo.exceptions.BusinessException;

@Service
public class FormateurServiceImpl implements FormateurService {
	private FormateurDAO formateurDAO;
	private CoursDAO coursDAO;

	public FormateurServiceImpl(FormateurDAO formateurDAO, CoursDAO coursDAO) {
		this.formateurDAO = formateurDAO;
		this.coursDAO = coursDAO;
	}

	@Override
	//Gestion de transaction
	@Transactional
	public void add(Formateur formateur) {
		// Validation des données de la couche présentation
		BusinessException be = new BusinessException();
		boolean isValid = true;
		isValid &= validerFormateur(formateur, be);
		isValid &= validerNom(formateur.getNom(), be);
		isValid &= validerPrenom(formateur.getPrenom(), be);
		isValid &= validerEmail(formateur.getEmail(), be);
		isValid &= validerListeCours(formateur.getListeCours(), be);
		isValid &= validerUniqueEmail(formateur.getEmail(), be);
		if (isValid) {
			formateurDAO.create(formateur);
			// Attention, il faut aussi compléter l'appel de la méthode pour gérer
			// l'insertion en base des cours
			
			//TEST TRANSACTION - Ajout d'un cours qui n'existe pas
//			Cours cours = new Cours();
//			cours.setId(1);
//			formateur.getListeCours().add(cours);
			
			formateur.getListeCours().forEach(c -> {
				coursDAO.insertCoursFormateur(c.getId(), formateur.getEmail());
			});

		} else {
			throw be;
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

		// Récupérons les cours associés à ce formateur
		List<Cours> coursDispenses = coursDAO.findByFormateur(emailFormateur);

		// attribuons les cours s'il en existe
		if (coursDispenses != null) {
			f.setListeCours(coursDispenses);
		}

		return f;

	}

	@Override
	public void update(Formateur formateur) {
		// Validation des données de la couche présentation
		BusinessException be = new BusinessException();
		boolean isValid = true;
		isValid &= validerFormateur(formateur, be);
		isValid &= validerEmail(formateur.getEmail(), be);
		isValid &= validerEmailExiste(formateur.getEmail(), be);
		isValid &= validerNom(formateur.getNom(), be);
		isValid &= validerPrenom(formateur.getPrenom(), be);

		if (isValid) {
			try {
				formateurDAO.update(formateur);
			} catch (DataAccessException e) {// Exception de la couche DAL
				// Rollback automatique
				be.add(BusinessCode.BLL_FORMATEUR_UPDATE_ERREUR);
				throw be;
			}
		} else {
			throw be;
		}
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

	/**
	 * Méthodes de validation des BO
	 */
	private boolean validerFormateur(Formateur f, BusinessException be) {
		if (f == null) {
			be.add(BusinessCode.VALIDATION_FORMATEUR_NULL);
			return false;
		}
		return true;
	}

	private boolean validerNom(String nom, BusinessException be) {
		if (nom == null || nom.isBlank()) {
			be.add(BusinessCode.VALIDATION_FORMATEUR_NOM_BLANK);
			return false;
		}
		if (nom.length() < 4 || nom.length() > 250) {
			be.add(BusinessCode.VALIDATION_FORMATEUR_NOM_LENGTH);
			return false;
		}
		return true;
	}

	private boolean validerPrenom(String prenom, BusinessException be) {
		if (prenom == null || prenom.isBlank()) {
			be.add(BusinessCode.VALIDATION_FORMATEUR_PRENOM_BLANK);
			return false;
		}
		if (prenom.length() < 4 || prenom.length() > 250) {
			be.add(BusinessCode.VALIDATION_FORMATEUR_PRENOM_LENGTH);
			return false;
		}
		return true;
	}

	private boolean validerEmail(String email, BusinessException be) {
		if (email == null || email.isBlank()) {
			be.add(BusinessCode.VALIDATION_FORMATEUR_EMAIL_BLANK);
			return false;
		}
		// Regex to check valid email
		String regex = "^[\\w-\\.]+@campus-eni.fr$";

		if (!email.matches(regex)) {
			be.add(BusinessCode.VALIDATION_FORMATEUR_EMAIL_PATTERN);
			return false;
		}
		return true;
	}

	private boolean validerListeCours(List<Cours> lstCours, BusinessException be) {
		if (lstCours == null || lstCours.isEmpty()) {
			be.add(BusinessCode.VALIDATION_FORMATEUR_COURS_EMPTY);
			return false;
		}

		// Vérifier que chaque identifiant de cours existe en base :
		if (!coursDAO.validateListOfCourseIds(lstCours)) {
			be.add(BusinessCode.VALIDATION_FORMATEUR_COURS_ID_INCONNU);
			return false;
		}

		return true;
	}

	private boolean validerUniqueEmail(String email, BusinessException be) {
		int count = formateurDAO.uniqueEmail(email);
		if (count == 1) {
			be.add(BusinessCode.VALIDATION_FORMATEUR_UNIQUE_EMAIL);
			return false;
		}
		return true;
	}

	private boolean validerEmailExiste(String emailFormateur, BusinessException be) {
		// L'email doit exister - s'il n'existe pas il y aura levée de l'exception
		// DataAccessException
		// Il faut gérer les 2 cas
		try {
			Formateur f = formateurDAO.read(emailFormateur);
			if (f == null) {
				// Il n'y a pas de formateur correspondant en base
				be.add(BusinessCode.VALIDATION_FORMATEUR_DB_NULL);
				return false;
			}
		} catch (DataAccessException e) {
			// Impossible de trouver un formateur
			// Il n'y a pas de formateur correspondant en base
			be.add(BusinessCode.VALIDATION_FORMATEUR_DB_NULL);
			return false;
		}
		return true;
	}
}
