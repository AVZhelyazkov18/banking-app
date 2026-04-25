package bg.nbu.banking_app.data.dto;

import lombok.*;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdatePersonDTO {
    private String firstName;
    private String lastName;
}
