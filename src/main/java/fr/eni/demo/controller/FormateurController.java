package fr.eni.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import fr.eni.demo.bll.CoursService;
import fr.eni.demo.bll.FormateurService;
import fr.eni.demo.bo.Cours;
import fr.eni.demo.bo.Formateur;
import jakarta.validation.Valid;

//@Controller --> permet de définir la classe comme un bean Spring de type Controller
@Controller
//Url par défaut pour toutes les méthodes du contrôleur
@RequestMapping("/formateurs")
//Mise en session de la liste des cours
@SessionAttributes({ "coursSession" })
public class FormateurController {
	// Injection du FormateurService
	private FormateurService formateurService;
	private CoursService coursService;

	public FormateurController(FormateurService formateurService, CoursService coursService) {
		this.formateurService = formateurService;
		this.coursService = coursService;
	}
	
	@GetMapping("/creer")
	public String creerFormateur(Model model) {
		Formateur formateur = new Formateur();
		// Ajout de l'instance dans le modèle
		model.addAttribute("formateur", formateur);
		return "view-formateur-creer";
		}
	
	// Récupération de l'objet formateur du formulaire
	// Traçage de la liste des cours associés via Converter
	// sauvegarde
	@PostMapping("/creer")
	public String creerFormateur(@Valid @ModelAttribute("formateur") Formateur formateur, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "view-formateur-creer";
		} else {
			formateurService.add(formateur);
			return "redirect:/formateurs";
		}
	}

	@GetMapping
	public String afficherFormateurs(Model model) {
		List<Formateur> lstFormateurs = formateurService.getFormateurs();
		model.addAttribute("formateurs", lstFormateurs);
		return "view-formateurs";

	}

	@GetMapping("/detail")
	public String detailFormateurParParametre(
			@RequestParam(name = "email", required = false, defaultValue = "coach@campus-eni.fr") String emailFormateur,
			Model model) {
		System.out.println("Le paramètre - " + emailFormateur);
		Formateur formateur = formateurService.findByEmail(emailFormateur);
		// Ajout de l'instance dans le modèle
		model.addAttribute("formateur", formateur);
		return "view-formateur-detail";
	}

	@PostMapping("/detail")
	public String mettreAJourFormateur(@Valid @ModelAttribute("formateur") Formateur f, BindingResult bindingResult) {
		
		if (bindingResult.hasErrors()) {
			return "view-formateur-detail";
		} else {
			System.out.println("Le formateur récupéré depuis le formulaire : ");
			System.out.println(f);
			
			//Sauvegarder les modifications
			formateurService.update(f);
			
			// Redirection l’affichage de tous les formateurs, en appelant la méthode
			// afficherFormateurs
			return "redirect:/formateurs";
		}

	}

	// Méthode pour charger la liste des cours en session
	@ModelAttribute("coursSession")
	public List<Cours> chargerCoursSession() {
		System.out.println("Chargement en session de tous les cours");
		return coursService.getCours();
	}

	// Ajout d'un cours au formateur courant
	@PostMapping("/cours")
	public String ajouterCours(@RequestParam(required = true) String email,
			@RequestParam(name = "idCours", required = true) String id) {
		long idCours = Long.parseLong(id);
		formateurService.updateCoursFormateur(email, idCours);
		return "redirect:/formateurs/detail?email=" + email;
	}

}
