package kg.test.test_project_for_bank.REST.Requests.User;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;



@Getter
@AllArgsConstructor
public class CreateUserRequest {
    @NotBlank(message = "Name of the user cannot be empty, please try again")
    private String name;

    @NotBlank(message = "Email of teh user cannot be blank, try another one")
    @Email (message = "Not valid email, please try another one")
    private String email;
}
