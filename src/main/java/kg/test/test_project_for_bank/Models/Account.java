package kg.test.test_project_for_bank.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_accounts")
@Builder
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_account_id")
    private Long id;

    @Column(name = "account_number", unique = true, nullable = false)
    private String accountNumber;

    @Column(name = "account_balance", nullable = false, columnDefinition = "DECIMAL(15,2)")
    @PositiveOrZero(message = "Balance cannot be fewer than 0, it has to be at least 0")
    private BigDecimal balance = BigDecimal.ZERO;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public BigDecimal getBalance() {
        return balance.setScale(2, RoundingMode.DOWN);
    }
}
