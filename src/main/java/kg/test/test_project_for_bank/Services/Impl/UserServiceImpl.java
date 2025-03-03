package kg.test.test_project_for_bank.Services.Impl;

import kg.test.test_project_for_bank.Repositories.UserRepository;
import kg.test.test_project_for_bank.Exceptions.UserExceptions.SuchUserNotFoundExeception;
import kg.test.test_project_for_bank.Exceptions.UserExceptions.UserAlreadyExistExeception;
import kg.test.test_project_for_bank.Models.User;
import kg.test.test_project_for_bank.REST.Requests.User.CreateUserRequest;
import kg.test.test_project_for_bank.Services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    @Override
    public User createUser(CreateUserRequest request) {
        log.debug("Attempting to create user with email: {}", request.getEmail());
        if(userRepository.existsByEmail(request.getEmail())){
            log.warn("User with such email:{} already exists", request.getEmail());
            throw new UserAlreadyExistExeception("User with such email already exists, please login by your email");
        }
        User newUser = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .build();
        log.info("User created successfully with email={}", newUser.getEmail());
        return userRepository.save(newUser);
    }

    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    @Override
    public User getUserById(Long userId) {
        log.debug("Fetching user by ID: {}", userId);
        return userRepository.findById(userId)
                .orElseThrow(() ->
                    new SuchUserNotFoundExeception("User not found with ID: " + userId)
                );
    }


    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    @Override
    public List<User> getAllUsers() {
        log.debug("Fetching all users");
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            log.warn("No users found in the database");
        }
        log.info("Fetching all users from db completed, the size of users: {}", users.size());
        return users;
    }
}
