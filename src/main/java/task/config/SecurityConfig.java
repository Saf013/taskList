package task.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import task.filter.JwtFilter;

@Configuration
@EnableWebSecurity
@ComponentScan({"task.util", "task.filter"})
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtFilter jwtFilter;


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/configuration/**",
                "/swagger-resources/**", "/swagger-ui.html", "/webjars/**",
                "/swagger-resources/configuration/ui", "/tl", "/tl/**", "/reg/registration", "/add-task");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers("/api/auth").permitAll()
                .antMatchers("/v2/api-docs", "/configuration/ui", "/configuration/**",
                        "/swagger-resources/**", "/swagger-ui.html", "/webjars/**",
                        "/swagger-resources/configuration/ui", "/home").permitAll()
                .antMatchers("/api/**").permitAll()
                .anyRequest().authenticated().and().formLogin().disable().httpBasic().disable()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authExp) -> {
          response.sendError(HttpStatus.UNAUTHORIZED.value(), authExp.getMessage());
        };
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
