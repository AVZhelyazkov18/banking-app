package bg.nbu.banking_app.data.dto;
import lombok.*;

import java.math.BigDecimal;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BankAccountDTO {
    private String IBAN;
    private BigDecimal balance;
    private Boolean status; //This could be named something more specific.
}
