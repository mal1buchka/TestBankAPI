package kg.test.test_project_for_bank.DTOs;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class AccountDTO {
    private Long accountId;
    private String accountNumber;
    private BigDecimal balance;
    private Long userId;
}
