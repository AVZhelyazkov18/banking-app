package bg.nbu.banking_app.data.dto;

import lombok.*;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateCompanyDTO {
    private String companyName;
    private String representative;
}
