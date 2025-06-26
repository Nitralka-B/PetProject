package com.bank.transfer.entities;



import com.bank.transfer.Util.Constants;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class PhoneTransfer {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Long phoneNumber;

    @Column(precision = Constants.AMOUNT_PRECISION, scale = Constants.AMOUNT_SCALE, nullable = false)
    private BigDecimal amount;

    @Column(columnDefinition = "TEXT")
    private String purpose;

    @Column(nullable = false)
    private Long accountDetailsId;


}
