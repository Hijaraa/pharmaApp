package com.Pharmacy.Pharmacy.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Achat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    private LocalDate dateAchat;
    private Double montantTotal;

    @ManyToOne
    @JoinColumn(name = "fournisseur_id")
    private Fournisseur fournisseur;

    @OneToMany(mappedBy = "achat", cascade = CascadeType.ALL)
    private List<LigneAchat> ligneAchats;

    // Constructors
    public Achat() {}

    public Achat(LocalDate dateAchat, Fournisseur fournisseur, Double montantTotal) {
        this.dateAchat = dateAchat;
        this.fournisseur = fournisseur;
        this.montantTotal = montantTotal;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateAchat() {
        return dateAchat;
    }

    public void setDateAchat(LocalDate dateAchat) {
        this.dateAchat = dateAchat;
    }

    public Double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(Double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public Fournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public List<LigneAchat> getLigneAchats() {
        return ligneAchats;
    }

    public void setLigneAchats(List<LigneAchat> ligneAchats) {
        this.ligneAchats = ligneAchats;
    }
}
