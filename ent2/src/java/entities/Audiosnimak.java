/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author HP
 */
@Entity
@Table(name = "audiosnimak")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Audiosnimak.findAll", query = "SELECT a FROM Audiosnimak a"),
    @NamedQuery(name = "Audiosnimak.findByIdAud", query = "SELECT a FROM Audiosnimak a WHERE a.idAud = :idAud"),
    @NamedQuery(name = "Audiosnimak.findByNaziv", query = "SELECT a FROM Audiosnimak a WHERE a.naziv = :naziv"),
    @NamedQuery(name = "Audiosnimak.findByTrajanje", query = "SELECT a FROM Audiosnimak a WHERE a.trajanje = :trajanje"),
    @NamedQuery(name = "Audiosnimak.findByDatum", query = "SELECT a FROM Audiosnimak a WHERE a.datum = :datum"),
    @NamedQuery(name = "Audiosnimak.findByIdVlasnik", query = "SELECT a FROM Audiosnimak a WHERE a.idVlasnik = :idVlasnik")})
public class Audiosnimak implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdAud")
    private Integer idAud;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "Naziv")
    private String naziv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Trajanje")
    private int trajanje;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Datum")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datum;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdVlasnik")
    private int idVlasnik;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idAud")
    private List<Pripada> pripadaList;

    public Audiosnimak() {
    }

    public Audiosnimak(Integer idAud) {
        this.idAud = idAud;
    }

    public Audiosnimak(Integer idAud, String naziv, int trajanje, Date datum, int idVlasnik) {
        this.idAud = idAud;
        this.naziv = naziv;
        this.trajanje = trajanje;
        this.datum = datum;
        this.idVlasnik = idVlasnik;
    }

    public Integer getIdAud() {
        return idAud;
    }

    public void setIdAud(Integer idAud) {
        this.idAud = idAud;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public int getTrajanje() {
        return trajanje;
    }

    public void setTrajanje(int trajanje) {
        this.trajanje = trajanje;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public int getIdVlasnik() {
        return idVlasnik;
    }

    public void setIdVlasnik(int idVlasnik) {
        this.idVlasnik = idVlasnik;
    }

    @XmlTransient
    public List<Pripada> getPripadaList() {
        return pripadaList;
    }

    public void setPripadaList(List<Pripada> pripadaList) {
        this.pripadaList = pripadaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAud != null ? idAud.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Audiosnimak)) {
            return false;
        }
        Audiosnimak other = (Audiosnimak) object;
        if ((this.idAud == null && other.idAud != null) || (this.idAud != null && !this.idAud.equals(other.idAud))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Audiosnimak[ idAud=" + idAud + " ]";
    }
    
}
