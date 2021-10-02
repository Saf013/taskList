package task.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {

        Pair<String, Boolean> passwordAndStatus =
                jdbcTemplate.queryForObject("select password, enabled from users where username = ?",
        (row, iteration)->Pair.of(row.getString(1), row.getBoolean(2)), name);

        List<String> authorities = jdbcTemplate.queryForList("select authority from users where authority = ?",
                String.class, name);
        User.UserBuilder builder = User.builder();
        return builder.password(passwordAndStatus.getFirst()).username(name)
                .disabled(!passwordAndStatus.getSecond())
                .authorities(authorities.toArray(String[]::new)).build();
    }

}
