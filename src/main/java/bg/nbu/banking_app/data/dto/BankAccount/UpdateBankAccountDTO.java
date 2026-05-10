package bg.nbu.banking_app.data.dto.BankAccount;

import lombok.*;

import java.math.BigDecimal;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateBankAccountDTO {
    private BigDecimal balance;
    private boolean status;
}