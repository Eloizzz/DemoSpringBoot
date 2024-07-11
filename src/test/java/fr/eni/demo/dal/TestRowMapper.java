package fr.eni.demo.dal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import fr.eni.demo.bo.Formateur;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestRowMapper {
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	void test01_Update() {
		String sql = "INSERT INTO [FORMATEURS] ([email],[nom],[prenom]) VALUES ('jtrillard@campus-eni.fr','TRILLARD','Julien');";
		int nbUpdate = jdbcTemplate.update(sql);
		assertEquals(1, nbUpdate);
		sql = "INSERT INTO [FORMATEURS] ([email],[nom],[prenom]) VALUES ('fdelaschesnais@campus-eni.fr','DELACHESNAIS','Frédéric');";
		nbUpdate = jdbcTemplate.update(sql);
		assertEquals(1, nbUpdate);
	}

	@Test
	void test02_QueryForList() {
		String sql = "SELECT email, nom, prenom FROM FORMATEURS;";
		List<Formateur> formateurs = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Formateur.class));
		assertNotNull(formateurs);
		assertEquals(3, formateurs.size());
		logger.info("QueryForList");
		formateurs.forEach(e -> logger.info(e));
	}
	
	@Test
	void test03_QueryForList_FormateurRowMapper() {
		String sql = "SELECT email, nom, prenom FROM FORMATEURS;";
		List<Formateur> formateurs = jdbcTemplate.query(sql, new FormateurRowMapper());
		assertNotNull(formateurs);
		assertEquals(3, formateurs.size());
		logger.info("QueryForList_PersonneRowMapper");
		formateurs.forEach(e -> logger.info(e));
	}
	
	/**
	 * Voici ce que réalise l'instance de la classe BeanPropertyRowMapper pour lier la classe Formateur à la table FORMATEURS
	 *  
	 *	Dans notre cas, il n'est pas indispensable d'utiliser RowMapper, la table et la classe sont identiques
	 */
	class FormateurRowMapper implements RowMapper<Formateur>{

		@Override
		public Formateur mapRow(ResultSet rs, int rowNum) throws SQLException {
			Formateur f = new Formateur();
			f.setEmail(rs.getString("email"));
			f.setNom(rs.getString("nom"));
			f.setPrenom(rs.getString("prenom"));
			
			return f;
		}
		
	}
	
}
