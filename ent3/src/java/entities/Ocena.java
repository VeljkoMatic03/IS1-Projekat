/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author HP
 */
@Entity
@Table(name = "ocena")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ocena.findAll", query = "SELECT o FROM Ocena o"),
    @NamedQuery(name = "Ocena.findByIdOce", query = "SELECT o FROM Ocena o WHERE o.idOce = :idOce"),
    @NamedQuery(name = "Ocena.findByOcena", query = "SELECT o FROM Ocena o WHERE o.ocena = :ocena"),
    @NamedQuery(name = "Ocena.findByDatum", query = "SELECT o FROM Ocena o WHERE o.datum = :datum"),
    @NamedQuery(name = "Ocena.findByIdKor", query = "SELECT o FROM Ocena o WHERE o.idKor = :idKor"),
    @NamedQuery(name = "Ocena.findByIdAud", query = "SELECT o FROM Ocena o WHERE o.idAud = :idAud")})
public class Ocena implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdOce")
    private Integer idOce;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Ocena")
    private int ocena;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Datum")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datum;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdKor")
    private int idKor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdAud")
    private int idAud;

    public Ocena() {
    }

    public Ocena(Integer idOce) {
        this.idOce = idOce;
    }

    public Ocena(Integer idOce, int ocena, Date datum, int idKor, int idAud) {
        this.idOce = idOce;
        this.ocena = ocena;
        this.datum = datum;
        this.idKor = idKor;
        this.idAud = idAud;
    }

    public Integer getIdOce() {
        return idOce;
    }

    public void setIdOce(Integer idOce) {
        this.idOce = idOce;
    }

    public int getOcena() {
        return ocena;
    }

    public void setOcena(int ocena) {
        this.ocena = ocena;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public int getIdKor() {
        return idKor;
    }

    public void setIdKor(int idKor) {
        this.idKor = idKor;
    }

    public int getIdAud() {
        return idAud;
    }

    public void setIdAud(int idAud) {
        this.idAud = idAud;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idOce != null ? idOce.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ocena)) {
            return false;
        }
        Ocena other = (Ocena) object;
        if ((this.idOce == null && other.idOce != null) || (this.idOce != null && !this.idOce.equals(other.idOce))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Ocena[ idOce=" + idOce + " ]";
    }
    
}
