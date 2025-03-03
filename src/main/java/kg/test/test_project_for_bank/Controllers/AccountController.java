package kg.test.test_project_for_bank.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Account controller", description = "This controller manages actions with user accounts")
public class AccountController {
    private final AccountService accountService;
    private final AccountMapper accountMapper;


    public AccountController(AccountService accountService, AccountMapper accountMapper) {
        this.accountService = accountService;
        this.accountMapper = accountMapper;
    }

    @Operation(summary = "Create Account for User", description = "Controller for creating an account for user")
    @ApiResponses (value = {
            @ApiResponse(responseCode = "201", description = "Account created successfully"),
            @ApiResponse (responseCode = "400", description = "Invalid user id or request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
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

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deposited successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @Operation(summary = "Deposit Amount", description = "Controller for depositing some amount to the balance")
    @PatchMapping ("/{accountId}/deposit")
    public ResponseEntity<String> depositToBalance(@PathVariable @Min (value = 1, message = "Account ID must be a positive number")
                                                   Long accountId, @Valid @RequestBody DepositToBalanceOfAccountRequest request) {
        accountService.depositToBalance(accountId, request);
        return ResponseEntity.status(200).body("Deposit operation done successfully");
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Withdraw operation successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid user request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @Operation(summary = "Withdraw Amount", description = "Controller for withdrawing some amount from the balance")
    @PatchMapping ("/{accountId}/withdraw")
    public ResponseEntity<String> withdrawFromBalance(@PathVariable @Min (value = 1, message = "Account ID must be a positive number")
                                                      Long accountId, @Valid @RequestBody WithdrawFromBalanceOfAccountRequest request) {
        accountService.withdrawFromBalance(accountId, request);
        return ResponseEntity.status(200).body("Withdraw operation done successfully");
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetching operation successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @Operation(summary = "Get All Accounts of User", description = "Controller which takes from db all the related accounts to the user")
    @GetMapping ("/{userId}")
    public ResponseEntity<List<AccountDTO>> getAllAccountsForUser(@PathVariable @Min (value = 1, message = "Account ID must be a positive number") Long userId) {
        List<Account> accountList = accountService.getAccountsByUserId(userId);
        List<AccountDTO> accountDTOList = accountMapper.toAccountDTOs(accountList);
        return ResponseEntity.status(200).body(accountDTOList);
    }
}
