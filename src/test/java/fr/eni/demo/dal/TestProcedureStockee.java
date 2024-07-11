package fr.eni.demo.dal;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestProcedureStockee {
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	void testNbCoursDispenses01_JdbcTemplate() {
		String email = "abaille@campus-eni.fr";
		
		List<SqlParameter> parameters = Arrays.asList(
	            new SqlParameter(Types.NVARCHAR), new SqlOutParameter("nbCours", Types.INTEGER));
		
		CallableStatementCreator csc = new CallableStatementCreator() {

			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				CallableStatement callableStatement = con.prepareCall("{call NbCoursDispenses (?, ?)}");
	            callableStatement.setString(1, email);
	            callableStatement.registerOutParameter(2, Types.INTEGER);
	            return callableStatement;
			}
	       
	    };

	    Map<String, Object> out = jdbcTemplate.call(csc, parameters);
		
		
		Integer res = (Integer) out.get("nbCours");
		logger.info(out);
		assertEquals(14, res);
	}

	
	@Test
	void testNbCoursDispenses02_SimpleJdbcCall() {
		SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("NbCoursDispenses");

		String email = "jtrillard@campus-eni.fr";
		SqlParameterSource in = new MapSqlParameterSource().addValue("email", email);
		Map<String, Object> out = jdbcCall.execute(in);

		Integer res = (Integer) out.get("nbCours");

		logger.info(out);
		assertEquals(13, res);
	}

}
