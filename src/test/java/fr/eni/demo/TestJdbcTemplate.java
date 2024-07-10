package fr.eni.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestJdbcTemplate {
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	void test01_Execute() {
		String sql = "CREATE TABLE [FORMATEURS]([email] [NVARCHAR](200) PRIMARY KEY,[nom] [NVARCHAR](250) NOT NULL,[prenom] [NVARCHAR](250) NOT NULL) ;";
		jdbcTemplate.execute(sql);
	}

	@Test
	void test02_Update() {
		String sql = "INSERT INTO [FORMATEURS] ([email],[nom],[prenom]) VALUES ('abaille@campus-eni.fr','BAILLE','Anne-Lise');";
		int nbUpdate = jdbcTemplate.update(sql);
		assertEquals(1, nbUpdate);
	}

	@Test
	void test03_QueryForObject() {
		String sql = "SELECT count(*) FROM FORMATEURS;";
		Integer nbFormateurs = jdbcTemplate.queryForObject(sql, Integer.class);
		assertNotNull(nbFormateurs);
		assertEquals(1, nbFormateurs);
	}

	@Test
	void test04_QueryForList() {
		String sql = "SELECT email FROM FORMATEURS;";
		List<String> emails = jdbcTemplate.queryForList(sql, String.class);
		assertNotNull(emails);
		assertEquals(1, emails.size());
		logger.info("QueryForList");
		emails.forEach(e -> logger.info(e));
	}
}
