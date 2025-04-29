package org.hyidi.dvdservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rental")
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    private DvdInstance dvdInstance;

    @Column(name = "renter_name")
    private String renterName;
    @Column(name = "rent_date")
    private Date rentDate;
    @Column(name = "return_date")
    private Date returnDate;
    @Column(name = "total_price")
    private Double totalPrice;
}
