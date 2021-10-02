package task.controller.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import task.controller.dto.UserDto;
import task.persistence.entity.UserEntity;

@Mapper
public interface UsersMapper {

    @Mappings(value = {@Mapping(source = "login", target = "userName"),
    @Mapping(source = "password", target = "userPassword")})
    UserDto userDtoToUser(UserEntity user);

    @Mappings(value = {@Mapping(source = "userName", target = "login"),
            @Mapping(source = "userPassword", target = "password")})
    UserEntity userEntityToUserDto(UserDto userDto);
}
