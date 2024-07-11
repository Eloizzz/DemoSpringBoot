package fr.eni.demo.dal;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import fr.eni.demo.bo.Cours;

@Repository
public class CoursDAOImpl implements CoursDAO {
	private final String FIND_BY_ID = "SELECT ID, TITRE, DUREE FROM COURS_ENI WHERE ID = :id";
	private final String FIND_ALL = "SELECT ID, TITRE, DUREE FROM COURS_ENI";
	private final String FIND_BY_FORMATEUR = "SELECT ID, TITRE, DUREE FROM COURS_ENI c "
			+ " INNER JOIN COURS_FORMATEUR cf ON c.ID = cf.ID_COURS WHERE cf.EMAIL_FORMATEUR = :email";
	private final String INSERT_COURS_FORMATEUR = "INSERT INTO COURS_FORMATEUR(email_formateur, id_cours) "
			+ " VALUES (:email, :id)";
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public Cours read(long id) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("id", id);
		return namedParameterJdbcTemplate.queryForObject(FIND_BY_ID, namedParameters,
				new BeanPropertyRowMapper<>(Cours.class));
	}

	@Override
	public List<Cours> findAll() {
		return namedParameterJdbcTemplate.query(FIND_ALL, new BeanPropertyRowMapper<>(Cours.class));
	}

	@Override
	public List<Cours> findByFormateur(String emailFormateur) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("email", emailFormateur);
		return namedParameterJdbcTemplate.query(FIND_BY_FORMATEUR, namedParameters,
				new BeanPropertyRowMapper<>(Cours.class));
	}

	@Override
	public void insertCoursFormateur(long idCours, String emailFormateur) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("id", idCours);
		namedParameters.addValue("email", emailFormateur);
		namedParameterJdbcTemplate.update(INSERT_COURS_FORMATEUR, namedParameters);
	}
}