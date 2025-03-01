package kg.test.test_project_for_bank.REST.Responses.User;

import kg.test.test_project_for_bank.DTOs.UserDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Builder
@Getter
public class UserCreateResponse {
    private UserDTO user;
    private String message;
}
