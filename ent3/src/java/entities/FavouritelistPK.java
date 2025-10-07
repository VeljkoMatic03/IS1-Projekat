/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author HP
 */
@Embeddable
public class FavouritelistPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "IdKor")
    private int idKor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdAud")
    private int idAud;

    public FavouritelistPK() {
    }

    public FavouritelistPK(int idKor, int idAud) {
        this.idKor = idKor;
        this.idAud = idAud;
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
        hash += (int) idKor;
        hash += (int) idAud;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FavouritelistPK)) {
            return false;
        }
        FavouritelistPK other = (FavouritelistPK) object;
        if (this.idKor != other.idKor) {
            return false;
        }
        if (this.idAud != other.idAud) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.FavouritelistPK[ idKor=" + idKor + ", idAud=" + idAud + " ]";
    }
    
}
