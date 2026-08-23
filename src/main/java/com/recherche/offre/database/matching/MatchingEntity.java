package com.recherche.offre.database.matching;

import com.recherche.offre.database.offre.SavedOfferEntity;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "MATCHING")
@SequenceGenerator(name = "SMATCHING001", sequenceName = "SMATCHING001", allocationSize = 1)
@Getter
@Setter
@NoArgsConstructor
public class MatchingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SMATCHING001")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OFFER_ID", nullable = false)
    private SavedOfferEntity offer;

    @Column(name = "SCORE", nullable = false, precision = 5, scale = 2)
    private BigDecimal score;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
