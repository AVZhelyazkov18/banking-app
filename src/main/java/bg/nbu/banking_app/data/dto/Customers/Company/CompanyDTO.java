package bg.nbu.banking_app.data.dto.Customers.Company;

import lombok.*;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CompanyDTO {
    private String companyName;
    private String eik;
    private String representative;
}
