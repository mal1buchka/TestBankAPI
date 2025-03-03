package kg.test.test_project_for_bank.Services;

import kg.test.test_project_for_bank.Exceptions.AccountExceptions.IllegalAccountOperationException;
import kg.test.test_project_for_bank.Exceptions.AccountExceptions.SuchAcccountForUserNotFoundException;
import kg.test.test_project_for_bank.Exceptions.UserExceptions.SuchUserNotFoundExeception;
import kg.test.test_project_for_bank.Models.Account;
import kg.test.test_project_for_bank.Models.User;
import kg.test.test_project_for_bank.REST.Requests.Account.DepositToBalanceOfAccountRequest;
import kg.test.test_project_for_bank.REST.Requests.Account.WithdrawFromBalanceOfAccountRequest;
import kg.test.test_project_for_bank.Repositories.AccountRepository;
import kg.test.test_project_for_bank.Services.Impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith (MockitoExtension.class)
public class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private AccountServiceImpl accountService;

    private User user;
    private Account account;
    private final Long userId = 2L;
    private final Long accountId = 3L;
    private final BigDecimal initialBalance = BigDecimal.ZERO;

    @BeforeEach
    void setUp() {
        user = User.builder().id(userId).build();
        account = Account.builder()
                .id(accountId)
                .accountNumber("UUID-9999-9999-9999-9999")
                .balance(initialBalance)
                .user(user)
                .build();
    }

    @Test
    void createAccountForUserTest_Success() {
        when(userService.getUserById(userId)).thenReturn(user);
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account createdAccount = accountService.createAccountForUser(userId);

        assertNotNull(createdAccount);
        assertEquals(0, createdAccount.getBalance().compareTo(BigDecimal.ZERO));
        verify(accountRepository, times(1)).save(any(Account.class));
    }
    @Test
    void createAccountForUserTest_ForNotExistingUser(){
        when(userService.getUserById(802L)).thenThrow(new SuchUserNotFoundExeception("Such user with this id does not exist in the system"));
        assertThrows(SuchUserNotFoundExeception.class, () -> accountService.createAccountForUser(802L));
        verify(accountRepository, times(0)).save(any(Account.class));
    }

    @Test
    void depositToBalanceTest_PositiveAmount_Success() {
        BigDecimal depositAmount = new BigDecimal("500.00");
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        accountService.depositToBalance(accountId, new DepositToBalanceOfAccountRequest(depositAmount));

        verify(accountRepository, times(1)).depositToBalance(accountId, depositAmount);
    }

    @Test
    void depositToBalanceTest_NegativeAmount_ThrowsException() {
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        BigDecimal depositAmount = new BigDecimal("-100.00");
        assertThrows(IllegalArgumentException.class, () ->
                accountService.depositToBalance(accountId, new DepositToBalanceOfAccountRequest(depositAmount))
        );
    }

    @Test
    void depositToBalanceTest_AccountNotFound_ThrowsException() {
        Long nonExistentAccountId = 4L;
        when(accountRepository.findById(nonExistentAccountId)).thenReturn(Optional.empty());

        assertThrows(SuchAcccountForUserNotFoundException.class, () ->
                accountService.depositToBalance(nonExistentAccountId, new DepositToBalanceOfAccountRequest(new BigDecimal("100.00")))
        );
    }

    @Test
    void withdrawFromBalanceTest_WithMoneyOnBalance_Success() {
        BigDecimal withdrawalAmount = new BigDecimal("200.00");
        account.setBalance(new BigDecimal("500.00"));
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        accountService.withdrawFromBalance(accountId, new WithdrawFromBalanceOfAccountRequest(withdrawalAmount));

        verify(accountRepository, times(1)).withdrawFromBalance(accountId, withdrawalAmount);
    }

    @Test
    void withdrawFromBalanceTest_WhereWithdrawalAmountMoreThanBalance_ThrowsException() {
        BigDecimal withdrawalAmount = new BigDecimal("300.00");
        account.setBalance(new BigDecimal("200.00"));
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        assertThrows(IllegalAccountOperationException.class, () ->
                accountService.withdrawFromBalance(accountId, new WithdrawFromBalanceOfAccountRequest(withdrawalAmount))
        );
    }
    @Test
    void withdrawFromBalanceTest_AcoountNotFound_ThrowsException() {
        BigDecimal balance = new BigDecimal("300.00");
        account.setBalance(balance);
        when(accountRepository.findById(802L)).thenThrow(new SuchAcccountForUserNotFoundException("Such account with this id does not exist in the system"));
        assertThrows(SuchAcccountForUserNotFoundException.class, () ->
                accountService.withdrawFromBalance(802L, new WithdrawFromBalanceOfAccountRequest(BigDecimal.TEN))
        );
        assertEquals(account.getBalance(), balance);
        verify(accountRepository, times(0)).withdrawFromBalance(802L, BigDecimal.ZERO);
    }

    @Test
    void getAccountsByUserIdTest_Success() {
        when(accountRepository.getAccountsByUserId(userId)).thenReturn(List.of(account));

        List<Account> accounts = accountService.getAccountsByUserId(userId);

        assertFalse(accounts.isEmpty());
        assertEquals(accountId, accounts.get(0).getId());
    }

    @Test
    void getAccountsByUserIdTest_InvalidId_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                accountService.getAccountsByUserId(-1L)
        );
    }
}
