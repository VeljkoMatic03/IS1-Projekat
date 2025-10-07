package entities;

import entities.Korisnik;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2025-07-08T20:01:27")
@StaticMetamodel(Mesto.class)
public class Mesto_ { 

    public static volatile SingularAttribute<Mesto, Integer> idMes;
    public static volatile SingularAttribute<Mesto, String> naziv;
    public static volatile ListAttribute<Mesto, Korisnik> korisnikList;

}