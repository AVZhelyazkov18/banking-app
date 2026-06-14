package bg.nbu.banking_app.data.dto.Customers.Company;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CompanyDTO {
    @NotBlank(message = "companyName cannot be blank")
    private String companyName;
    @NotBlank(message = "eik cannot be blank")
    private String eik;
    @NotBlank(message = "representative cannot be blank")
    private String representative;
}
