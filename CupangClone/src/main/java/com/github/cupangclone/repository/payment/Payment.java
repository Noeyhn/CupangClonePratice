package com.github.cupangclone.repository.payment;

import com.github.cupangclone.repository.orders.Orders;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Orders orders;

    @Column(name = "total_price")
    private Long totalPrice;

    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "paid_at")
    @UpdateTimestamp
    private LocalDateTime paidAt;

}
