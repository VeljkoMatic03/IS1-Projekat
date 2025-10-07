/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author HP
 */
@Entity
@Table(name = "pripada")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pripada.findAll", query = "SELECT p FROM Pripada p"),
    @NamedQuery(name = "Pripada.findByIdPri", query = "SELECT p FROM Pripada p WHERE p.idPri = :idPri")})
public class Pripada implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdPri")
    private Integer idPri;
    @JoinColumn(name = "IdAud", referencedColumnName = "IdAud")
    @ManyToOne(optional = false)
    private Audiosnimak idAud;
    @JoinColumn(name = "IdKat", referencedColumnName = "IdKat")
    @ManyToOne(optional = false)
    private Kategorija idKat;

    public Pripada() {
    }

    public Pripada(Integer idPri) {
        this.idPri = idPri;
    }

    public Integer getIdPri() {
        return idPri;
    }

    public void setIdPri(Integer idPri) {
        this.idPri = idPri;
    }

    public Audiosnimak getIdAud() {
        return idAud;
    }

    public void setIdAud(Audiosnimak idAud) {
        this.idAud = idAud;
    }

    public Kategorija getIdKat() {
        return idKat;
    }

    public void setIdKat(Kategorija idKat) {
        this.idKat = idKat;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPri != null ? idPri.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pripada)) {
            return false;
        }
        Pripada other = (Pripada) object;
        if ((this.idPri == null && other.idPri != null) || (this.idPri != null && !this.idPri.equals(other.idPri))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Pripada[ idPri=" + idPri + " ]";
    }
    
}
