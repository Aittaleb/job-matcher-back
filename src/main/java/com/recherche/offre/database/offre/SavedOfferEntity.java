package com.recherche.offre.database.offre;

import com.recherche.offre.database.user.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "SAVED_OFFER")
@SequenceGenerator(name = "SSAVEDOFFER001", sequenceName = "SSAVEDOFFER001", allocationSize = 1)
@Getter
@Setter
@NoArgsConstructor
public class SavedOfferEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SSAVEDOFFER001")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OFFER_ID", nullable = false)
    private OffreSauvegardeeEntity offer;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
