package kg.test.test_project_for_bank.Mappers;

import kg.test.test_project_for_bank.DTOs.UserDTO;
import kg.test.test_project_for_bank.Models.User;
import org.springframework.stereotype.Component;
import java.util.List;


@Component
public class UserMapper {
    public UserDTO toUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .dateOfRegistration(user.getDateOfRegistration())
                .build();
    }
    public List<UserDTO> toUserDTOs(List<User> users) {
        return users.stream().map(this::toUserDTO).toList();
    }
}
