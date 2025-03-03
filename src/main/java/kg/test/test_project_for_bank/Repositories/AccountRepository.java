package kg.test.test_project_for_bank.Repositories;

import kg.test.test_project_for_bank.Models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("select a from Account a where a.user.id=:userId")
    List<Account> getAccountsByUserId(@Param ("userId") Long userId);

    @Modifying
    @Query("update Account a set a.balance = a.balance + :amount where a.id = :accountId")
    void depositToBalance(@Param("accountId") Long accountId, @Param("amount") BigDecimal amount);

    @Modifying
    @Query("update Account a set a.balance = a.balance - :amount where a.id = :accountId")
    void withdrawFromBalance(@Param("accountId") Long accountId, @Param("amount") BigDecimal amount);
}
