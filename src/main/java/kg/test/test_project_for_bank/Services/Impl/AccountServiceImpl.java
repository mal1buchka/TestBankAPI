package kg.test.test_project_for_bank.Services.Impl;


import kg.test.test_project_for_bank.DAOs.AccountDAO;
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
public class AccountServiceImpl implements AccountService {


    private final AccountDAO accountDAO;
    private final UserService userService;

    public AccountServiceImpl(AccountDAO accountDAO, UserService userService) {
        this.accountDAO = accountDAO;
        this.userService = userService;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    @Override
    public Account createAccountForUser(Long userId) {

        User user = userService.getUserById(userId);
        String accountNumber = UUID.randomUUID().toString();
        Account account = Account.builder()
                .accountNumber(accountNumber)
                .balance(BigDecimal.ZERO)
                .user(user).build();
        return accountDAO.save(account);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public void depositToBalance(Long accountId, DepositToBalanceOfAccountRequest request) {
        if(request.getDepositAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount has to be only positive");
        }
        Account account = getAccountById(accountId);
        account.setBalance(account.getBalance().add(request.getDepositAmount()));
        log.info("Deposited {} to account {} successfully", request.getDepositAmount(), accountId);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public void withdrawFromBalance(Long accountId, WithdrawFromBalanceOfAccountRequest request) {
        if(request.getWithdrawalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount has to be only positive");
        }
        Account account = getAccountById(accountId);
        if (account.getBalance().compareTo(request.getWithdrawalAmount()) < 0) {
            throw new IllegalAccountOperationException(
                    String.format("Not enougth balance to withdraw from account %s \n Current balance: %s \n Requested withdraw: %s", accountId, account.getBalance(), request.getWithdrawalAmount())
            );
        }
        account.setBalance(account.getBalance().subtract(request.getWithdrawalAmount()));
        log.info("Withdraw operation successfully done from account: {}, and withdraw amount: {}", accountId, request.getWithdrawalAmount());
    }

    private Account getAccountById(Long accountId){
        return accountDAO.findById(accountId).orElseThrow(
                () -> new SuchAcccountForUserNotFoundException("Such Account Not Found in system with id " + accountId)
        );
    }

    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    @Override
    public List<Account> getAccountsByUserId(Long userId) {
        if(userId == null || userId <= 0 || userId.describeConstable().isEmpty()) {
            throw new IllegalArgumentException("UserId has to be greater than 0");
        }
        return accountDAO.getAccountsByUserId(userId);
    }
}
