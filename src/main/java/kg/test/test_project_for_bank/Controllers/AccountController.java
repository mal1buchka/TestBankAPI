package kg.test.test_project_for_bank.Controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import kg.test.test_project_for_bank.DTOs.AccountDTO;
import kg.test.test_project_for_bank.Mappers.AccountMapper;
import kg.test.test_project_for_bank.Models.Account;
import kg.test.test_project_for_bank.REST.Requests.Account.DepositToBalanceOfAccountRequest;
import kg.test.test_project_for_bank.REST.Requests.Account.WithdrawFromBalanceOfAccountRequest;
import kg.test.test_project_for_bank.REST.Responses.Account.AccountCreateResponse;
import kg.test.test_project_for_bank.Services.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping ("/api/v1/accounts")
public class AccountController {
    private final AccountService accountService;
    private final AccountMapper accountMapper;


    public AccountController(AccountService accountService, AccountMapper accountMapper) {
        this.accountService = accountService;
        this.accountMapper = accountMapper;
    }

    @PostMapping ("/create-account/{userId}")
    public ResponseEntity<AccountCreateResponse> createAccount(@PathVariable @Min (value = 1, message = "User ID must be a positive number") Long userId) {
        Account createdAccount = accountService.createAccountForUser(userId);
        AccountDTO accountDTO = accountMapper.toAccountDTO(createdAccount);
        AccountCreateResponse response = AccountCreateResponse.builder()
                .account(accountDTO)
                .message(String.format("Account with id: %s created successfully for user with id: %s", createdAccount.getId(), userId))
                .build();
        return ResponseEntity.status(201).body(response);
    }

    @PatchMapping ("/{accountId}/deposit")
    public ResponseEntity<String> depositToBalance(@PathVariable @Min (value = 1, message = "Account ID must be a positive number")
                                                   Long accountId, @Valid @RequestBody DepositToBalanceOfAccountRequest request) {
        accountService.depositToBalance(accountId, request);
        return ResponseEntity.status(200).body("Deposit operation done successfully");
    }

    @PatchMapping ("/{accountId}/withdraw")
    public ResponseEntity<String> withdrawFromBalance(@PathVariable @Min (value = 1, message = "Account ID must be a positive number")
                                                      Long accountId, @Valid @RequestBody WithdrawFromBalanceOfAccountRequest request) {
        accountService.withdrawFromBalance(accountId, request);
        return ResponseEntity.status(200).body("Withdraw operation done successfully");
    }

    @GetMapping ("/{userId}")
    public ResponseEntity<List<AccountDTO>> getAllAccountsForUser(@PathVariable @Min (value = 1, message = "Account ID must be a positive number") Long userId) {
        List<Account> accountList = accountService.getAccountsByUserId(userId);
        List<AccountDTO> accountDTOList = accountMapper.toAccountDTOs(accountList);
        return ResponseEntity.status(200).body(accountDTOList);
    }
}
