package kg.test.test_project_for_bank.Services;

import kg.test.test_project_for_bank.Models.Account;
import kg.test.test_project_for_bank.REST.Requests.Account.DepositToBalanceOfAccountRequest;
import kg.test.test_project_for_bank.REST.Requests.Account.WithdrawFromBalanceOfAccountRequest;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    Account createAccountForUser(Long userId);
    void depositToBalance(Long accountId, DepositToBalanceOfAccountRequest request);
    void withdrawFromBalance(Long accountId, WithdrawFromBalanceOfAccountRequest request);
    List<Account> getAccountsByUserId(Long userId);
}
