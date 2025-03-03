package kg.test.test_project_for_bank.Services;

import kg.test.test_project_for_bank.Exceptions.UserExceptions.SuchUserNotFoundExeception;
import kg.test.test_project_for_bank.Models.User;
import kg.test.test_project_for_bank.REST.Requests.User.CreateUserRequest;
import kg.test.test_project_for_bank.Repositories.UserRepository;
import kg.test.test_project_for_bank.Exceptions.UserExceptions.UserAlreadyExistExeception;
import kg.test.test_project_for_bank.Services.Impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private final Long userId = 1L;
    private final String email = "testTestovich@email.com";
    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(userId)
                .name("Testino Userino")
                .email(email)
                .build();
    }

    @Test
    void createUserTest_Success() {
        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(
                new CreateUserRequest("Test Malik", email)
        );

        assertNotNull(createdUser);
        assertEquals(email, createdUser.getEmail());
        verify(userRepository, times(1)).existsByEmail(email);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUserTest_emailAlreadyExists_throwsException() {
        when(userRepository.existsByEmail(email)).thenReturn(true);

        assertThrows(UserAlreadyExistExeception.class, () ->
                userService.createUser(new CreateUserRequest("Tipa User", email))
        );
    }

    @Test
    void getUserByIdTest_Success() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User foundUser = userService.getUserById(userId);

        assertEquals(userId, foundUser.getId());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getUserByIdTest_NotFound_ThrowsException() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(SuchUserNotFoundExeception.class, () ->
                userService.getUserById(userId)
        );
    }

    @Test
    void getAllUsersTEst_EmptyList() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<User> users = userService.getAllUsers();

        assertTrue(users.isEmpty());
        verify(userRepository, times(1)).findAll();
    }
}
