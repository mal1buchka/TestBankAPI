package kg.test.test_project_for_bank.Services.Impl;

import kg.test.test_project_for_bank.DAOs.UserDAO;
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

    private final UserDAO userDAO;

    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    @Override
    public User createUser(CreateUserRequest request) {
        log.info("Attempting to create user");
        if(userDAO.existsByEmail(request.getEmail())){
            log.warn("User with such email:{} already exists", request.getEmail());
            throw new UserAlreadyExistExeception("User with such email already exists, please login by your email");
        }
        User newUser = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .build();
        log.info("User has been created and saved with email: {}", newUser.getEmail());
        return userDAO.save(newUser);
    }
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    @Override
    public User getUserById(Long userId) {
        log.info("Attempting to get user by id: {}", userId);
        User user = userDAO.findById(userId).orElseThrow(
                () -> new SuchUserNotFoundExeception("Such user not found"));
        log.info("Successfully took user: id {}", userId);
        return user;
    }
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    @Override
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }
}
