package com.Pharmacy.Pharmacy.entities;

import javax.persistence.*;

@Entity
public class LigneVente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantite;
    private Double prixUnitaire;
    private Double montant;
    private Double sousTotal;

    @ManyToOne
    @JoinColumn(name = "vente_id")
    private Vente vente;

    @ManyToOne
    @JoinColumn(name = "medicament_id")
    private Medicament medicament;

    // Constructors
    public LigneVente() {}

    public LigneVente(Vente vente, Medicament medicament, Integer quantite, Double prixUnitaire) {
        this.vente = vente;
        this.medicament = medicament;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
        this.montant = quantite * prixUnitaire;
        this.sousTotal = this.montant;
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

    public Vente getVente() {
        return vente;
    }

    public void setVente(Vente vente) {
        this.vente = vente;
    }

    public Medicament getMedicament() {
        return medicament;
    }

    public void setMedicament(Medicament medicament) {
        this.medicament = medicament;
    }

    public Double getSousTotal() {
        return sousTotal;
    }

    public void setSousTotal(Double sousTotal) {
        this.sousTotal = sousTotal;
    }
}
