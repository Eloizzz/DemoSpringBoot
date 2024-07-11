package fr.eni.demo.dal;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import fr.eni.demo.bo.Formateur;

@Repository
public class FormateurDAOImpl implements FormateurDAO {
	private final String INSERT = "INSERT INTO FORMATEURS(email, nom, prenom) VALUES (:email, :nom, :prenom)";
	private final String FIND_BY_EMAIL = "SELECT email, nom, prenom FROM FORMATEURS WHERE EMAIL = :email";
	private final String FIND_ALL = "SELECT email, nom, prenom FROM FORMATEURS";
	private final String UPDATE = "UPDATE FORMATEURS SET nom = :nom, prenom = :prenom WHERE email = :email";
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public void create(Formateur formateur) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("email", formateur.getEmail());
		namedParameters.addValue("nom", formateur.getNom());
		namedParameters.addValue("prenom", formateur.getPrenom());
		namedParameterJdbcTemplate.update(INSERT, namedParameters);
	}

	@Override
	public List<Formateur> findAll() {
		return namedParameterJdbcTemplate.query(FIND_ALL, new BeanPropertyRowMapper<>(Formateur.class));
	}

	@Override
	public Formateur read(String emailFormateur) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("email", emailFormateur);
		return namedParameterJdbcTemplate.queryForObject(FIND_BY_EMAIL, namedParameters,
				new BeanPropertyRowMapper<>(Formateur.class));
	}

	@Override
	public void update(Formateur formateur) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("email", formateur.getEmail());
		namedParameters.addValue("nom", formateur.getNom());
		namedParameters.addValue("prenom", formateur.getPrenom());
		namedParameterJdbcTemplate.update(UPDATE, namedParameters);
	}
}