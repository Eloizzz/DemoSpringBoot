package fr.eni.demo.controller.contexte;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import fr.eni.demo.bo.contexte.Utilisateur;

@Controller
@RequestMapping("/contexte/utilisateur")
@SessionAttributes({ "utilisateurSession" })
public class UtilisateurController {

	@GetMapping
	public String getDetail(@ModelAttribute("utilisateurSession") String utilisateurSession) {
	System.out.println("UtilisateurController - Contexte de la session comporte : " 
			+ utilisateurSession);

		return "contexte/view-utilisateur";
	}
	@ModelAttribute("utilisateurSession")
	public Utilisateur addAttributSession() {
		System.out.println("Add Attribut Session");
		return new Utilisateur("Anne-Lise");
	}
}
