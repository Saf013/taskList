package task.controller.rest;

import io.jsonwebtoken.lang.Assert;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import task.controller.dto.AuthenticationRequestDto;
import task.controller.dto.AuthenticationResponseDto;
import task.service.DbClient;
import task.util.JwtUtil;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final DbClient dbClient;

    @PostMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
    public String auth(@RequestBody final AuthenticationRequestDto reqDto) {
        JSONObject jsonObject = new JSONObject();
        String pwd = "";
        Assert.notNull(reqDto, "AuthenticationRequest must ne null");
        if (reqDto.getLogin().isEmpty() || reqDto.getPassword().isEmpty()) {
            jsonObject.put("status", "400");
        } else if (dbClient.findByLogin(reqDto.getLogin()).isEmpty()) {
            jsonObject.put("status", "403");
        }else if (dbClient.findByLogin(reqDto.getLogin()).isPresent()) {
            Authentication authenticate =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(reqDto.getLogin(), reqDto.getPassword()));
            AuthenticationResponseDto build = AuthenticationResponseDto.builder().token(jwtUtil.generateToken(authenticate.getName())).build();
            jsonObject.put("status", "200");
            jsonObject.put("token", build.getToken());
        } else {
            jsonObject.put("starus", 503);
        }
        return jsonObject.toString();
    }
}
