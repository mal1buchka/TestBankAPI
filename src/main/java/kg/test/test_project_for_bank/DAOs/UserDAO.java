package kg.test.test_project_for_bank.DAOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import kg.test.test_project_for_bank.Models.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDAO extends JpaRepository<User, Long> {
    @NonNull
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.accounts a WHERE u.id = :userId")
    Optional<User> findById(@Param ("userId") @NonNull Long userId);

    @NonNull
    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.accounts a")
    List<User> findAll();
    boolean existsByEmail(@NotBlank(message = "Email of teh user cannot be blank, try another one") @Email(message = "Not valid email, please try another one") String email);
}
