package bg.nbu.banking_app.data.dto.Customers.NaturalPerson;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PersonDTO {
    @NotBlank(message = "firstName cannot be blank")
    private String firstName;
    @NotBlank(message = "lastName cannot be blank")
    private String lastName;
    @NotBlank(message = "pin cannot be blank")
    private String pin;
}
