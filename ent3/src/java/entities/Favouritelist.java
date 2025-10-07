/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author HP
 */
@Entity
@Table(name = "favouritelist")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Favouritelist.findAll", query = "SELECT f FROM Favouritelist f"),
    @NamedQuery(name = "Favouritelist.findByIdKor", query = "SELECT f FROM Favouritelist f WHERE f.favouritelistPK.idKor = :idKor"),
    @NamedQuery(name = "Favouritelist.findByIdAud", query = "SELECT f FROM Favouritelist f WHERE f.favouritelistPK.idAud = :idAud")})
public class Favouritelist implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FavouritelistPK favouritelistPK;

    public Favouritelist() {
    }

    public Favouritelist(FavouritelistPK favouritelistPK) {
        this.favouritelistPK = favouritelistPK;
    }

    public Favouritelist(int idKor, int idAud) {
        this.favouritelistPK = new FavouritelistPK(idKor, idAud);
    }

    public FavouritelistPK getFavouritelistPK() {
        return favouritelistPK;
    }

    public void setFavouritelistPK(FavouritelistPK favouritelistPK) {
        this.favouritelistPK = favouritelistPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (favouritelistPK != null ? favouritelistPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Favouritelist)) {
            return false;
        }
        Favouritelist other = (Favouritelist) object;
        if ((this.favouritelistPK == null && other.favouritelistPK != null) || (this.favouritelistPK != null && !this.favouritelistPK.equals(other.favouritelistPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Favouritelist[ favouritelistPK=" + favouritelistPK + " ]";
    }
    
}
