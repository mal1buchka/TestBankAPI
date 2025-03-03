package kg.test.test_project_for_bank.Mappers;

import kg.test.test_project_for_bank.DTOs.AccountDTO;
import kg.test.test_project_for_bank.Models.Account;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccountMapper {
    public AccountDTO toAccountDTO(Account account) {
        return AccountDTO.builder()
                .accountId(account.getId())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .userId(account.getUser().getId())
                .build();
    }
    public List<AccountDTO> toAccountDTOs(List<Account> accounts) {
        return accounts.stream().map(this::toAccountDTO).toList();
    }
}
