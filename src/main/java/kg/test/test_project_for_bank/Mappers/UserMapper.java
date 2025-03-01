package kg.test.test_project_for_bank.Mappers;

import kg.test.test_project_for_bank.DTOs.AccountDTO;
import kg.test.test_project_for_bank.DTOs.UserDTO;
import kg.test.test_project_for_bank.Models.Account;
import kg.test.test_project_for_bank.Models.User;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    private final AccountMapper accountMapper;

    public UserMapper(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    public UserDTO toUserDTO(User user) {
        Map<Long, Account> accounts = user.getAccounts() != null ? user.getAccounts() : new LinkedHashMap<>();

        Map<String, AccountDTO> accountDTOMap = accounts
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().toString(),
                        entry -> accountMapper.toAccountDTO(entry.getValue())
                ));
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .accounts(accountDTOMap).build();
    }
    public List<UserDTO> toUserDTOs(List<User> users) {
        return users.stream().map(this::toUserDTO).toList();
    }
}
