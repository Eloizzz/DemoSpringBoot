package fr.eni.demo.dal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import fr.eni.demo.bo.Cours;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestInSQL {
	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	// Imaginons une méthode qui prend en paramètre une valeur saisie qui représente
	// l'email du formateur à rechercher
	boolean idCoursInDB(List<Cours> cours) {
		// Liste des identifiants des cours
		List<Long> lstId = cours.stream().map(Cours::getId).collect(Collectors.toList());

		// La méhtode crée une requête en concaténant la donnée
		String sql = "select count(id) from COURS_ENI where id in (:lst_id_cours)";
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("lst_id_cours", lstId);

		int nbIds = namedParameterJdbcTemplate.queryForObject(
				sql, namedParameters, Integer.class);

		return lstId.size() == nbIds;
	}

	@Test
	void test01_In_OK() {
		//Vérifier que si tous les identifiants existent -> il y a validation de la requête
		List<Cours> lstCours = new ArrayList<>();
		lstCours.add(new Cours(10, "", 0));
		lstCours.add(new Cours(100, "", 0));
		lstCours.add(new Cours(150, "", 0));
		boolean idExist = idCoursInDB(lstCours);
		assertTrue(idExist);
	}
	
	@Test
	void test02_In_KO() {
		//Vérifier que si l'un des identifiants n'existe pas -> il n'y a pas validation de la requête
		List<Cours> lstCours = new ArrayList<>();
		lstCours.add(new Cours(10, "", 0));
		lstCours.add(new Cours(1, "", 0));
		lstCours.add(new Cours(150, "", 0));
		boolean idExist = idCoursInDB(lstCours);
		assertFalse(idExist);
	}

}
