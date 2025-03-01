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
public class DepositToBalanceOfAccountRequest {
    @NotNull
    @Positive(message = "Deposited amount cannto be negative or zero, only positive")
    private BigDecimal depositAmount;

    public BigDecimal getDepositAmount() {
        return depositAmount.setScale(2, RoundingMode.DOWN);
    }
}
