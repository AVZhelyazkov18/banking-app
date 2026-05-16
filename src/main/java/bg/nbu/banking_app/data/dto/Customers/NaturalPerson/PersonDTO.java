package bg.nbu.banking_app.data.dto.Customers.NaturalPerson;

import lombok.*;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PersonDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String pin;
}
