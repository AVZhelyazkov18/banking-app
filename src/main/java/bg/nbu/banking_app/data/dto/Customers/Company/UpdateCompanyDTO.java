package bg.nbu.banking_app.data.dto.Customers.Company;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateCompanyDTO {
    @NotBlank(message = "companyName cannot be blank")
    private String companyName;
    @NotBlank(message = "representative cannot be blank")
    private String representative;
}
