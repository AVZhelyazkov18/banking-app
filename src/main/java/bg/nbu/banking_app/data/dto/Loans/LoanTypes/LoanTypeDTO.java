package bg.nbu.banking_app.data.dto.Loans.LoanTypes;

import lombok.*;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoanTypeDTO {
    private Long id;
    private String creditName;
    private int creditDisbursedMin;
    private int creditDisbursedMax;
    private int creditTermMin;
    private int creditTermMax;
    private double creditInterestRate;
}
