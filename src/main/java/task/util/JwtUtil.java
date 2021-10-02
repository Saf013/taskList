package task.util;

import com.google.common.net.HttpHeaders;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.lang.Strings;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    private static final String BEARER = "Bearer ";

    private final String secretKey = "219932nndskl100askdma1";

    public String getLoginFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsTFunction) {
        Claims body = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        return claimsTFunction.apply(body);
    }

    public String generateToken(String login) {
        return Jwts.builder().setSubject(login)
                .setExpiration(new Date(System.currentTimeMillis()+60*30*1000))
                .signWith(SignatureAlgorithm.HS512, secretKey).compact();
    }

    public boolean validate(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getTokenFromRequest(final HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (Strings.hasText(header) && header.startsWith(BEARER)) {
            return header.substring(7);
        }
        return null;
    }
}
