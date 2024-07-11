package fr.eni.demo.dal;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import fr.eni.demo.bo.Formateur;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestNamedParameterJdbcTemplate {
	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// Insertion d"un formateur avec un PreparedStatement
	int insertWithPreparedStatement(String email, String nom, String prenom) {
		// utilisation d"un paramètre ?
		String sql = "INSERT INTO [FORMATEURS] ([email],[nom],[prenom]) VALUES (?, ?, ?)";

		// Attention, il faut que l"ordre des paramètres correspondent exactement à
		// l"ordre des ?
		return jdbcTemplate.update(sql, email, nom, prenom);
	}

	@Test
	void test01_PreparedStatementAvecPlusieursParametres() {
		int nbEnregistrement = insertWithPreparedStatement("pmontembault@campus-eni.fr", "MONTEMBAULT", "Philippe");
		assertEquals(1, nbEnregistrement);
	}

	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	// Insertion d"un formateur avec un NamedParameterJdbcTemplate
	int insertWithNamedParameterJdbcTemplate(String email, String nom, String prenom) {
		// utilisation d"un paramètre ?
		String sql = "INSERT INTO [FORMATEURS] ([email],[nom],[prenom]) VALUES (:email, :nom, :prenom)";

		// Manipulation d"une Map <nom_paramètre, valeur>
		// l"ordre n"est pas imposé
		// le nom_parmètre qui est important
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("email", email);
		namedParameters.addValue("prenom", prenom);
		namedParameters.addValue("nom", nom);

		return namedParameterJdbcTemplate.update(sql, namedParameters);
	}

	@Test
	void test02_NamedParameterJdbcTemplate() {
		int nbEnregistrement = insertWithNamedParameterJdbcTemplate("hbernard@campus-eni.fr", "BERNARD", "Hervé");
		assertEquals(1, nbEnregistrement);
	}

	// Insertion d"un formateur avec un NamedParameterJdbcTemplate
	int insertWithBO(Formateur f) {
		// utilisation d"un paramètre ?
		String sql = "INSERT INTO [FORMATEURS] ([email],[nom],[prenom]) VALUES (:email, :nom, :prenom)";

		SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(f);

		return namedParameterJdbcTemplate.update(sql, namedParameters);
	}

	@Test
	void test03_NamedParameterJdbcTemplate_BO() {
		Formateur f = new Formateur("GROUSSARD", "Thierry", "tgroussard@campus-eni.fr");
		int nbEnregistrement = insertWithBO(f);
		assertEquals(1, nbEnregistrement);
	}
}
