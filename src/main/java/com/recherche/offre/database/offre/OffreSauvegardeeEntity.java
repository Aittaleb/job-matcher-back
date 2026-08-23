package com.recherche.offre.database.offre;

import com.recherche.offre.database.user.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "SAVED_OFFER")
@SequenceGenerator(name = "SSAVEDOFFER001", sequenceName = "SSAVEDOFFER001", allocationSize = 1)
@Getter
@Setter
@NoArgsConstructor
public class OffreSauvegardeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SSAVEDOFFER001")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USER_ID", nullable = false)
    private UserEntity user;

    @Column(name = "OFFER_ID", nullable = false)
    private Long offerId;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;
}
