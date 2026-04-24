package bg.nbu.banking_app.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Table(name = "company")
public class Company {

    @OneToOne
    @JoinColumn(name = "customer_id")
    @MapsId
    private Customer customer;

    @Setter
    @Column(name = "company_name")
    private String companyName;

    @Column(name = "eik")
    private String eik;

    @Setter
    @Column(name = "representative")
    private String representative;
}
