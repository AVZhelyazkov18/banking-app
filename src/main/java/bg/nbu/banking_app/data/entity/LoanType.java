package bg.nbu.banking_app.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Setter
public class LoanType extends BaseEntity {
    @Column(name = "credit_name")
    private String creditName;

    // Credit Disbursed are Integers Because Increment
    // And Decrement Will be by a certain amount. (ex: 500 Euro.)

    @Column(name = "credit_disbursed_min")
    private int creditDisbursedMin;

    @Column(name = "credit_disbursed_max")
    private int creditDisbursedMax;

    @Column(name = "credit_term_min")
    private int creditTermMin;

    @Column(name = "credit_term_max")
    private int creditTermMax;

    @Column(name = "credit_interest_rate")
    private double creditInterestRate;
}
