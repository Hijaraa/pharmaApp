package com.Pharmacy.Pharmacy.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Medicament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private Double prixAchat;
    private Double prixVente;
    private Integer quantiteStock;
    private Integer seuilAlerte;
    private LocalDate dateExpiration;

    @ManyToOne
    @JoinColumn(name = "fournisseur_id")
    private Fournisseur fournisseur;

    @OneToMany(mappedBy = "medicament")
    private List<LigneAchat> ligneAchats;

    @OneToMany(mappedBy = "medicament")
    private List<LigneVente> ligneVentes;

    // Constructors
    public Medicament() {}

    public Medicament(String nom, Double prixAchat, Double prixVente, Integer quantiteStock, 
                     Integer seuilAlerte, LocalDate dateExpiration, Fournisseur fournisseur) {
        this.nom = nom;
        this.prixAchat = prixAchat;
        this.prixVente = prixVente;
        this.quantiteStock = quantiteStock;
        this.seuilAlerte = seuilAlerte;
        this.dateExpiration = dateExpiration;
        this.fournisseur = fournisseur;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Double getPrixAchat() {
        return prixAchat;
    }

    public void setPrixAchat(Double prixAchat) {
        this.prixAchat = prixAchat;
    }

    public Double getPrixVente() {
        return prixVente;
    }

    public void setPrixVente(Double prixVente) {
        this.prixVente = prixVente;
    }

    public Integer getQuantiteStock() {
        return quantiteStock;
    }

    public void setQuantiteStock(Integer quantiteStock) {
        this.quantiteStock = quantiteStock;
    }

    public Integer getSeuilAlerte() {
        return seuilAlerte;
    }

    public void setSeuilAlerte(Integer seuilAlerte) {
        this.seuilAlerte = seuilAlerte;
    }

    public LocalDate getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(LocalDate dateExpiration) {
        this.dateExpiration = dateExpiration;
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

    public List<LigneVente> getLigneVentes() {
        return ligneVentes;
    }

    public void setLigneVentes(List<LigneVente> ligneVentes) {
        this.ligneVentes = ligneVentes;
    }
}
