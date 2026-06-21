package bg.nbu.banking_app.data.dto.Customers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerOptionDTO {
    private Long id;
    private String displayName;
    private String type;
}