package kg.test.test_project_for_bank.Services.Impl;


import kg.test.test_project_for_bank.Repositories.AccountRepository;
import kg.test.test_project_for_bank.Exceptions.AccountExceptions.IllegalAccountOperationException;
import kg.test.test_project_for_bank.Exceptions.AccountExceptions.SuchAcccountForUserNotFoundException;
import kg.test.test_project_for_bank.Models.Account;
import kg.test.test_project_for_bank.Models.User;
import kg.test.test_project_for_bank.REST.Requests.Account.DepositToBalanceOfAccountRequest;
import kg.test.test_project_for_bank.REST.Requests.Account.WithdrawFromBalanceOfAccountRequest;
import kg.test.test_project_for_bank.Services.AccountService;
import kg.test.test_project_for_bank.Services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService<Account> {


    private final AccountRepository accountRepository;
    private final UserService<User> userService;

    public AccountServiceImpl(AccountRepository accountRepository, UserService<User> userService) {
        this.accountRepository = accountRepository;
        this.userService = userService;
    }

    @Transactional (propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    @Override
    public Account createAccountForUser(Long userId) {
        log.debug("Creating account for user {}", userId);

        User user = userService.getUserById(userId);
        String accountNumber = UUID.randomUUID().toString();
        Account account = Account.builder()
                .accountNumber(accountNumber)
                .balance(BigDecimal.ZERO)
                .user(user).build();
        log.info("Created account successfully  for userId: {} and accountNumber: {}",  userId, accountNumber);
        return accountRepository.save(account);
    }

    @Transactional (isolation = Isolation.READ_COMMITTED)
    @Override
    public void depositToBalance(Long accountId, DepositToBalanceOfAccountRequest request) {
        log.debug("Depositing to balance for account {} and amount is: {}", accountId, request.getDepositAmount());

        Account account = getAccountById(accountId);
        BigDecimal oldBalance = account.getBalance();

        if (request.getDepositAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount has to be only positive");
        }

        //тут не стал добавлять ограничение на максимально возможное пополнение, надо ли, у меня 15 знаков, из них 13 целых, 2 дроби
        accountRepository.depositToBalance(accountId, request.getDepositAmount());
        BigDecimal newBalance = oldBalance.add(request.getDepositAmount());
        log.info("Deposited {} kgs to accountId {} successfully. Old balance: {}. New Balance: {}", request.getDepositAmount(), accountId, oldBalance, newBalance);
    }

    @Transactional (isolation = Isolation.READ_COMMITTED)
    @Override
    public void withdrawFromBalance(Long accountId, WithdrawFromBalanceOfAccountRequest request) {
        log.debug("Starting withdraw operation from accountId: {} with amount: {}", accountId, request.getWithdrawalAmount());

        Account account = getAccountById(accountId);
        BigDecimal oldBalance = account.getBalance();

        if (request.getWithdrawalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            log.warn("UserId: {} tried to withdraw negative amount: {}", accountId, request.getWithdrawalAmount());
            throw new IllegalArgumentException("Withdrawal amount has to be only positive");
        }

        if (account.getBalance().compareTo(request.getWithdrawalAmount()) < 0) {
            log.error("Not enough balance for withdrawal. Account ID: {}, Current Balance: {}, Requested Withdrawal: {}",
                    accountId, account.getBalance(), request.getWithdrawalAmount());
            //экранирование и <br> в postman сериализуется как текст
            throw new IllegalAccountOperationException(
                    String.format("Not enougth balance to withdraw from account %s Current balance: %s Requested withdrawal: %s",
                            accountId, account.getBalance(), request.getWithdrawalAmount())
            );
        }

        accountRepository.withdrawFromBalance(accountId, request.getWithdrawalAmount());
        BigDecimal newBalance = oldBalance.subtract(request.getWithdrawalAmount());
        log.info("Withdraw operation successfully done. Account id: {}, Withdraw Amount: {}, Old Balance: {}, New Balance: {}",
                accountId, request.getWithdrawalAmount(), oldBalance, newBalance);
    }

    private Account getAccountById(Long accountId) {
        log.debug("Trying to get account: id={}", accountId);
        return accountRepository.findById(accountId).orElseThrow(() -> {
            log.error("Account not found: id={}", accountId);
            return new SuchAcccountForUserNotFoundException("Account not found");
        });
    }

    @Transactional (readOnly = true, isolation = Isolation.READ_COMMITTED)
    @Override
    public List<Account> getAccountsByUserId(Long userId) {
        log.debug("Trying to get accounts for user: id={}", userId);
        if (userId == null || userId <= 0) {
            log.warn("Invalid user ID: {}", userId);
            throw new IllegalArgumentException("User ID must be positive");
        }
        return accountRepository.getAccountsByUserId(userId);
    }
}
