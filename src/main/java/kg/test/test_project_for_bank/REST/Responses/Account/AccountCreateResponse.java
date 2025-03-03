package kg.test.test_project_for_bank.REST.Responses.Account;

import kg.test.test_project_for_bank.DTOs.AccountDTO;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccountCreateResponse {
    private AccountDTO account;
    private String message;
}
