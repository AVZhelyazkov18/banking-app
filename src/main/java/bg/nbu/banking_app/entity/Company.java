package bg.nbu.banking_app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "company")
public class Company {

    @Id
    @OneToOne
    @PrimaryKeyJoinColumn(name = "id")
    private Customer customer;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "eik")
    private String eik;

    @Column(name = "representative")
    private String representative;
}
