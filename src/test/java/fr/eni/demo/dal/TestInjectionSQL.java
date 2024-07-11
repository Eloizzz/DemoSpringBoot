package fr.eni.demo.dal;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import fr.eni.demo.bo.Formateur;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestInjectionSQL {
	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// Imaginons une méthode qui prend en paramètre une valeur saisie qui représente
	// l'email du formateur à rechercher
	Formateur findByEmail(String email) {
		// La méhtode crée une requête en concaténant la donnée
		String sql = "SELECT * FROM [FORMATEURS] where email = " + email;
		return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Formateur.class));
	}

	@Test
	void test01_InjectionSQL() {
		// Imaginons un écran avec un champ pour saisir l'email du formateur recherché
		// Voici la chaîne de caractères que l'utilisateur a saisie : 'a';delete from FORMATEURS
		String saisieUtilisateur = "'abaille@campus-eni.fr';delete from FORMATEURS";

		Formateur f = findByEmail(saisieUtilisateur);
		assertNotNull(f);
		logger.info("QueryForList");
		logger.info(f);
		
		//Allez voir en base de données notre table : FORMATEURS
		//Toutes les données de la table ont été supprimées
		//Vous pouvez voir la requête exécutée dans les traces
		//Aucune sécurité sur l'injection de script SQL dans une concaténation
		//A BANIR
	}

}
