package com.Pharmacy.Pharmacy.entities;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class LigneAchat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantite;
    private Double prixUnitaire;
    private Double montant;
    private LocalDate dateExpiration;

    @ManyToOne
    @JoinColumn(name = "achat_id")
    private Achat achat;

    @ManyToOne
    @JoinColumn(name = "medicament_id")
    private Medicament medicament;

    // Constructors
    public LigneAchat() {}

    public LigneAchat(Achat achat, Medicament medicament, Integer quantite, 
                     Double prixUnitaire, LocalDate dateExpiration) {
        this.achat = achat;
        this.medicament = medicament;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
        this.dateExpiration = dateExpiration;
        this.montant = quantite * prixUnitaire;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
        if (this.prixUnitaire != null) {
            this.montant = this.quantite * this.prixUnitaire;
        }
    }

    public Double getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(Double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
        if (this.quantite != null) {
            this.montant = this.quantite * this.prixUnitaire;
        }
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public LocalDate getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(LocalDate dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public Achat getAchat() {
        return achat;
    }

    public void setAchat(Achat achat) {
        this.achat = achat;
    }

    public Medicament getMedicament() {
        return medicament;
    }

    public void setMedicament(Medicament medicament) {
        this.medicament = medicament;
    }
}
