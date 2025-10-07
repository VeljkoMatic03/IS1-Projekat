package entities;

import entities.Pretplata;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2025-07-16T00:27:35")
@StaticMetamodel(Paket.class)
public class Paket_ { 

    public static volatile ListAttribute<Paket, Pretplata> pretplataList;
    public static volatile SingularAttribute<Paket, String> naziv;
    public static volatile SingularAttribute<Paket, Integer> idPak;
    public static volatile SingularAttribute<Paket, Float> cena;

}