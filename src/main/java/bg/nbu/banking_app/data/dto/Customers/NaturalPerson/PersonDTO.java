package bg.nbu.banking_app.data.dto;

import lombok.*;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PersonDTO {
    private String firstName;
    private String lastName;
    private String pin;
}
