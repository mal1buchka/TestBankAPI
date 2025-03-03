package kg.test.test_project_for_bank.Services;


import kg.test.test_project_for_bank.Models.User;
import kg.test.test_project_for_bank.REST.Requests.User.CreateUserRequest;

import java.util.List;


public interface UserService {
    User createUser(CreateUserRequest request);
    User getUserById(Long userId);
    List<User> getAllUsers();
}
