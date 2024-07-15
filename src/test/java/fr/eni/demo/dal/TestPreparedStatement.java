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
public class TestPreparedStatement {
	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// Imaginons une méthode qui prend en paramètre une valeur saisie qui représente
	// l'email du formateur à rechercher et
	//utilise un PreparedStament pour le manipuler
	Formateur findByEmail(String email) {
		// utilisation d'un paramètre ?
		String sql = "SELECT * FROM [FORMATEURS] where email = ?";
				
		return jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<>(Formateur.class), email);
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
		
	}
}
