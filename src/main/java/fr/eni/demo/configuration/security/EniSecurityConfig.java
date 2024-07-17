package fr.eni.demo.configuration.security;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class EniSecurityConfig {
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Bean
	UserDetailsManager userDetailsManager(DataSource dataSource) {
		JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
		jdbcUserDetailsManager.setUsersByUsernameQuery("SELECT pseudo, password, 1 FROM utilisateur WHERE pseudo=?");
		jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("SELECT pseudo, role FROM roles WHERE pseudo=?");
		
		return jdbcUserDetailsManager;
	}
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		http.authorizeHttpRequests(auth -> {auth
			//Permettre à tous les rôle d'accéder à la liste des formateurs
			.requestMatchers(HttpMethod.GET, "/formateurs").hasAnyRole("EMPLOYE","FORMATEUR","ADMIN")
			//restreindre la manipulation de l'url detail sur Get et Post et rôle FORMATEUR
			.requestMatchers(HttpMethod.GET, "/formateurs/detail").hasRole("FORMATEUR")
			.requestMatchers(HttpMethod.POST, "/formateurs/detail").hasRole("FORMATEUR")
			//restreindre la manipulation des URLs formateurs/creer et contexte/SessionEtRequest au rôle ADMIN
			.requestMatchers("/formateurs/creer").hasRole("ADMIN")
			.requestMatchers("/contexte/SessionEtRequest").hasRole("ADMIN");
			//permettre à tout le monde d'accéder à l'URL racine
			auth.requestMatchers("/*").permitAll();
			//Permettre à tous les utilisateurs d'afficher correctement les images et la css
			auth.requestMatchers("/css/*").permitAll();
			auth.requestMatchers("/images/*").permitAll();
			//Toutes autres url et méthodes HTTP ne sont pas permises
			auth.anyRequest().denyAll();
		});
		
		//Customiser le formulaire
		http.formLogin(form -> {
			form.loginPage("/login").permitAll();
			form.defaultSuccessUrl("/");
		});
		
		http.logout(logout -> logout
				.invalidateHttpSession(true)
				.clearAuthentication(true)
				.deleteCookies("JSESSIONID")
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/")
				.permitAll()
		);
		
		return http.build();
	}
}

