package kg.test.test_project_for_bank.REST.Requests.Account;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawFromBalanceOfAccountRequest {
    @NotNull(message = "withdraw amount cannot be null")
    @Positive(message = "withdraw amount cannot be negative or zero, only positive")
    private BigDecimal withdrawalAmount;

    public BigDecimal getWithdrawalAmount() {
        return withdrawalAmount.setScale(2, RoundingMode.DOWN);
    }
}
