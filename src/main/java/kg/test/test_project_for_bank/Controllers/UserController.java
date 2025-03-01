package kg.test.test_project_for_bank.Controllers;

import jakarta.validation.Valid;
import kg.test.test_project_for_bank.DTOs.UserDTO;
import kg.test.test_project_for_bank.Mappers.UserMapper;
import kg.test.test_project_for_bank.Models.User;
import kg.test.test_project_for_bank.REST.Requests.User.CreateUserRequest;
import kg.test.test_project_for_bank.REST.Responses.User.UserCreateResponse;
import kg.test.test_project_for_bank.Services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping ("api/v1/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping("/create-user")
    ResponseEntity<UserCreateResponse> createUser(@RequestBody @Valid CreateUserRequest request) {
        User createdUser = userService.createUser(request);
        UserDTO userDTO = userMapper.toUserDTO(createdUser);
        return ResponseEntity.status(201).body(UserCreateResponse.builder()
                .user(userDTO)
                .message("User has created successfully").build());
    }
    @GetMapping("/{userId}")
    ResponseEntity<UserDTO> getUserById(@PathVariable long userId) {
        User user = userService.getUserById(userId);
        UserDTO userDTO = userMapper.toUserDTO(user);
        return ResponseEntity.status(200).body(userDTO);
    }
    @GetMapping
    ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserDTO> userDTOList = userMapper.toUserDTOs(users);
        return ResponseEntity.status(200).body(userDTOList);
    }
    @GetMapping("/all-info")
    ResponseEntity<List<User>> getAllInfoAboutUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.status(200).body(users);
    }
}

