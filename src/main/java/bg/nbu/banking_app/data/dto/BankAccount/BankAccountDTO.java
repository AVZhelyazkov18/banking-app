package bg.nbu.banking_app.data.dto.BankAccount;
import lombok.*;

import java.math.BigDecimal;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BankAccountDTO {
    //private Long id; That is likely not needed here.
    private String IBAN;
    private BigDecimal balance;
    private boolean status;
}
