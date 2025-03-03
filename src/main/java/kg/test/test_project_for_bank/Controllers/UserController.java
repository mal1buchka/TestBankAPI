package kg.test.test_project_for_bank.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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
@Tag(name = "User Controller", description = "This controller manages actions with users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }
    @ApiResponses (value = {
            @ApiResponse (responseCode = "201", description = "User created successfully"),
            @ApiResponse (responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @Operation(summary = "Create User", description = "Controller for creating user")
    @PostMapping("/create-user")
    ResponseEntity<UserCreateResponse> createUser(@RequestBody @Valid CreateUserRequest request) {
        User createdUser = userService.createUser(request);
        UserDTO userDTO = userMapper.toUserDTO(createdUser);
        return ResponseEntity.status(201).body(UserCreateResponse.builder()
                .user(userDTO)
                .message("User has created successfully").build());
    }

    @ApiResponses (value = {
            @ApiResponse(responseCode = "201", description = "Fetching 1 user successfully"),
            @ApiResponse (responseCode = "400", description = "Invalid user id or request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @Operation(summary = "Get User by ID", description = "Controller for fetching one user from DB")
    @GetMapping("/{userId}")
    ResponseEntity<UserDTO> getUserById(@PathVariable @Min(value = 1, message = "User id must be a positive number")Long userId) {
        User user = userService.getUserById(userId);
        UserDTO userDTO = userMapper.toUserDTO(user);
        return ResponseEntity.status(200).body(userDTO);
    }

    @ApiResponses (value = {
            @ApiResponse(responseCode = "201", description = "Fetching all users successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @Operation(summary = "Get all Users", description = "Controller for fetching all the users from DB")
    @GetMapping
    ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserDTO> userDTOList = userMapper.toUserDTOs(users);
        return ResponseEntity.status(200).body(userDTOList);
    }
}

