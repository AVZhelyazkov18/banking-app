package bg.nbu.banking_app.data.dto.Loans.LoanTypes;
import bg.nbu.banking_app.validation.ValidLoanTypeRange;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ValidLoanTypeRange
public class LoanTypeDTO {
    private Long id;

    @NotBlank(message = "creditName is required")
    private String creditName;

    @PositiveOrZero(message = "creditDisbursedMin must be 0 or greater")
    private int creditDisbursedMin;

    @Positive(message = "creditDisbursedMax must be greater than 0")
    private int creditDisbursedMax;

    @Positive(message = "creditTermMin must be greater than 0")
    private int creditTermMin;

    @Positive(message = "creditTermMax must be greater than 0")
    private int creditTermMax;

    @Positive(message = "creditInterestRate must be greater than 0")
    private double creditInterestRate;
}