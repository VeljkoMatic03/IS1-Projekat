package entities;

import entities.Pripada;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2025-07-14T21:17:09")
@StaticMetamodel(Audiosnimak.class)
public class Audiosnimak_ { 

    public static volatile SingularAttribute<Audiosnimak, Date> datum;
    public static volatile ListAttribute<Audiosnimak, Pripada> pripadaList;
    public static volatile SingularAttribute<Audiosnimak, Integer> idAud;
    public static volatile SingularAttribute<Audiosnimak, Integer> trajanje;
    public static volatile SingularAttribute<Audiosnimak, String> naziv;
    public static volatile SingularAttribute<Audiosnimak, Integer> idVlasnik;

}