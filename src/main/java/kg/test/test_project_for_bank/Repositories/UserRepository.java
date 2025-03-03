package kg.test.test_project_for_bank.Repositories;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import kg.test.test_project_for_bank.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(@NotBlank(message = "Email of teh user cannot be blank, try another one") @Email(message = "Not valid email, please try another one") String email);
}
