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
@Table(name = "slusanje")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Slusanje.findAll", query = "SELECT s FROM Slusanje s"),
    @NamedQuery(name = "Slusanje.findByIdSlu", query = "SELECT s FROM Slusanje s WHERE s.idSlu = :idSlu"),
    @NamedQuery(name = "Slusanje.findByDatum", query = "SELECT s FROM Slusanje s WHERE s.datum = :datum"),
    @NamedQuery(name = "Slusanje.findByPocetakSek", query = "SELECT s FROM Slusanje s WHERE s.pocetakSek = :pocetakSek"),
    @NamedQuery(name = "Slusanje.findByVremeSlusanja", query = "SELECT s FROM Slusanje s WHERE s.vremeSlusanja = :vremeSlusanja"),
    @NamedQuery(name = "Slusanje.findByIdKor", query = "SELECT s FROM Slusanje s WHERE s.idKor = :idKor"),
    @NamedQuery(name = "Slusanje.findByIdAud", query = "SELECT s FROM Slusanje s WHERE s.idAud = :idAud")})
public class Slusanje implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdSlu")
    private Integer idSlu;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Datum")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datum;
    @Column(name = "PocetakSek")
    private Integer pocetakSek;
    @Column(name = "VremeSlusanja")
    private Integer vremeSlusanja;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdKor")
    private int idKor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdAud")
    private int idAud;

    public Slusanje() {
    }

    public Slusanje(Integer idSlu) {
        this.idSlu = idSlu;
    }

    public Slusanje(Integer idSlu, Date datum, int idKor, int idAud) {
        this.idSlu = idSlu;
        this.datum = datum;
        this.idKor = idKor;
        this.idAud = idAud;
    }

    public Integer getIdSlu() {
        return idSlu;
    }

    public void setIdSlu(Integer idSlu) {
        this.idSlu = idSlu;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public Integer getPocetakSek() {
        return pocetakSek;
    }

    public void setPocetakSek(Integer pocetakSek) {
        this.pocetakSek = pocetakSek;
    }

    public Integer getVremeSlusanja() {
        return vremeSlusanja;
    }

    public void setVremeSlusanja(Integer vremeSlusanja) {
        this.vremeSlusanja = vremeSlusanja;
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
        hash += (idSlu != null ? idSlu.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Slusanje)) {
            return false;
        }
        Slusanje other = (Slusanje) object;
        if ((this.idSlu == null && other.idSlu != null) || (this.idSlu != null && !this.idSlu.equals(other.idSlu))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Slusanje[ idSlu=" + idSlu + " ]";
    }
    
}
