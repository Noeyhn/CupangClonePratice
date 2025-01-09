package com.github.cupangclone.repository.orders;

import com.github.cupangclone.repository.itemOption.ItemOption;
import com.github.cupangclone.repository.items.Items;
import com.github.cupangclone.repository.userPrincipal.UserPrincipal;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Items items;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_option_id")
    private ItemOption itemOption;

    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "total_price")
    private Long totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_principal_id")
    private UserPrincipal userPrincipal;

    @Column(name = "receiver_name")
    private String receiverName;

    @Column(name = "receiver_adress")
    private String receiverAdress;

    @Column(name = "receiver_postcode")
    private Long receiverPostCode;

    @Column(name = "receiver_phone_number")
    private String receiverPhoneNumber;

    @Column(name = "payment_status")
    @ColumnDefault("false")
    private Boolean paymentStatus;

    @Column(name = "order_at")
    @UpdateTimestamp
    private LocalDateTime orderAt;

}
