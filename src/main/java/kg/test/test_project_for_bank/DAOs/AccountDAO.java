package kg.test.test_project_for_bank.DAOs;

import kg.test.test_project_for_bank.Models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountDAO extends JpaRepository<Account, Long> {

    List<Account> getAccountsByUserId(Long userId);
}
