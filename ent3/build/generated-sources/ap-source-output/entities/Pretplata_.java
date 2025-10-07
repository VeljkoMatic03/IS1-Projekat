package entities;

import entities.Paket;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2025-07-16T00:27:35")
@StaticMetamodel(Pretplata.class)
public class Pretplata_ { 

    public static volatile SingularAttribute<Pretplata, Date> datumPocetka;
    public static volatile SingularAttribute<Pretplata, Integer> idKor;
    public static volatile SingularAttribute<Pretplata, Paket> idPak;
    public static volatile SingularAttribute<Pretplata, Float> cena;
    public static volatile SingularAttribute<Pretplata, Integer> idPre;

}