package task.controller.rest;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import task.controller.converter.UsersMapper;
import task.controller.dto.UserDto;
import task.service.DbClient;
import task.service.security.CustomUserDetailService;

import javax.servlet.http.HttpServletResponse;


@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class RegistrationRestController {

    private DbClient dbClient;
    private UsersMapper mapper;
    private CustomUserDetailService userDetailService;

    @SneakyThrows
    @PostMapping(value = "/registration", produces = MediaType.APPLICATION_JSON_VALUE)
    public String reg(@RequestBody UserDto userDto, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        if (userDto.getUserName().isEmpty() || userDto.getUserPassword().isEmpty() || dbClient.findByLogin(userDto.getUserName()).isPresent()) {
            jsonObject.put("status", 400);
        } else if (dbClient.findByLogin(userDto.getUserName()).isEmpty()) {

            dbClient.create(mapper.userEntityToUserDto(userDto));
            jsonObject.put("status", 201);
        } else {
            jsonObject.put("status", response.getStatus());
        }

        return jsonObject.toString();
    }
}
