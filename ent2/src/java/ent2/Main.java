/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ent2;

import entities.Audiosnimak;
import entities.Kategorija;
import entities.Korisnik;
import entities.Pripada;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 *
 * @author HP
 */
public class Main {

    @Resource(lookup = "projectConnFactory")
    private static ConnectionFactory connectionFactory;

    @Resource(lookup = "podsistem2Topic")
    private static Topic podsistem2Topic;

    @Resource(lookup = "appToP2Topic")
    private static Topic appToP2Topic;

//    @Resource(lookup = "q1")
//    private static Queue q1;
//
//    @Resource(lookup = "q2")
//    private static Queue q2;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ent2PU");
        if (emf == null) {
            System.out.println("null");
        }
        EntityManager em = emf.createEntityManager();

        EntityManagerFactory emf_p1 = Persistence.createEntityManagerFactory("ent1PU");
        if (emf_p1 == null) {
            System.out.println("null");
        }
        EntityManager em_p1 = emf_p1.createEntityManager();

        JMSContext context = connectionFactory.createContext();
        JMSConsumer consumer = context.createConsumer(podsistem2Topic);
        JMSProducer producer = context.createProducer();
        System.out.println("Start App 2");

        String reply, naziv, email;
        TextMessage replyMsg;
        while (true) {
            try {
                TextMessage request = (TextMessage) consumer.receive();
                String requestStr = request.getText();
                switch(requestStr) {
                    case "dohvatiKategorije" :
                        reply = dohvatiKategorije(em);
                        replyMsg = context.createTextMessage(reply);
                        replyMsg.setJMSCorrelationID(request.getJMSCorrelationID());
                        replyMsg.setStringProperty("header", "Naziv;");
                        producer.send(appToP2Topic, replyMsg);
                        System.out.println("Poruka poslata - dohvati kategorije");
                    break;
                    case "dohvatiSnimke" :
                        reply = dohvatiAudioSnimke(em, em_p1);
                        replyMsg = context.createTextMessage(reply);
                        replyMsg.setJMSCorrelationID(request.getJMSCorrelationID());
                        replyMsg.setStringProperty("header", "Naziv|Datum|Trajanje|Vlasnik;");
                        producer.send(appToP2Topic, replyMsg);
                        System.out.println("Poruka poslata - dohvati audio snimke");
                    break;
                    case "dohvatiKategorijeSnimak" :
                        naziv = request.getStringProperty("naziv");
                        email = request.getStringProperty("email");
                        reply = dohvatiKategorijeSnimak(em, em_p1, naziv, email);
                        replyMsg = context.createTextMessage(reply);
                        replyMsg.setJMSCorrelationID(request.getJMSCorrelationID());
                        replyMsg.setStringProperty("header", "Naziv;");
                        producer.send(appToP2Topic, replyMsg);
                        System.out.println("Poruka poslata - dohvati kategorije za audio snimak");
                    break;
                    case "kreirajKategoriju" :
                        naziv = request.getStringProperty("naziv");
                        reply = kreirajKategoriju(em, naziv);
                        replyMsg = context.createTextMessage(reply);
                        replyMsg.setJMSCorrelationID(request.getJMSCorrelationID());
                        producer.send(appToP2Topic, replyMsg);
                        System.out.println("Poruka poslata - kreiraj kategoriju");
                    break;
                    case "kreirajSnimak" :
                        int trajanje = request.getIntProperty("trajanje");
                        String datumStr = request.getStringProperty("datum");
                        naziv = request.getStringProperty("naziv");
                        email = request.getStringProperty("email");
                        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        Date datum;
                        try {
                            datum = dateFormat.parse(datumStr);
                        } catch (ParseException ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                            datum = new Date();
                        }
                        reply = kreirajAudioSnimak(em, em_p1, naziv, trajanje, datum, email);
                        replyMsg = context.createTextMessage(reply);
                        replyMsg.setJMSCorrelationID(request.getJMSCorrelationID());
                        producer.send(appToP2Topic, replyMsg);
                        System.out.println("Poruka poslata - kreiraj audio snimak");
                    break;
                    case "promeniNazivSnimka" :
                        email = request.getStringProperty("email");
                        String stari = request.getStringProperty("stari");
                        String novi = request.getStringProperty("novi");
                        reply = promeniImeSnimka(em, em_p1, stari, novi, email);
                        replyMsg = context.createTextMessage(reply);
                        replyMsg.setJMSCorrelationID(request.getJMSCorrelationID());
                        producer.send(appToP2Topic, replyMsg);
                        System.out.println("Poruka poslata - promeni naziv snimka");
                    break;
                    case "dodajKategorijuSnimku" :
                        email = request.getStringProperty("email");
                        String nazivKategorije = request.getStringProperty("kategorija");
                        String nazivSnimak = request.getStringProperty("snimak");
                        reply = dodajKategorijuSnimku(em, em_p1, nazivKategorije, nazivSnimak, email);
                        replyMsg = context.createTextMessage(reply);
                        replyMsg.setJMSCorrelationID(request.getJMSCorrelationID());
                        producer.send(appToP2Topic, replyMsg);
                        System.out.println("Poruka poslata - dodaj kategoriju snimku");
                    break;
                    case "obrisiSnimak" :
                        email = request.getStringProperty("email");
                        naziv = request.getStringProperty("naziv");
                        reply = obrisiSnimak(em, em_p1, email, naziv);
                        replyMsg = context.createTextMessage(reply);
                        replyMsg.setJMSCorrelationID(request.getJMSCorrelationID());
                        producer.send(appToP2Topic, replyMsg);
                        System.out.println("Poruka poslata - obrisi snimak");
                    break;
                }
            } catch (JMSException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static String kreirajKategoriju(EntityManager em, String naziv) {
        naziv = naziv.toLowerCase();
        TypedQuery<Kategorija> kategorijaQ = em.createNamedQuery("Kategorija.findByNaziv", Kategorija.class);
        kategorijaQ.setParameter("naziv", naziv);
        List<Kategorija> kategorijaL = kategorijaQ.getResultList();
        if (kategorijaL != null && !kategorijaL.isEmpty()) {
            return "Postoji kategorija vec";
        }
        Kategorija kat = new Kategorija();
        kat.setNaziv(naziv);
        em.getTransaction().begin();
        em.persist(kat);
        em.getTransaction().commit();
        return "Uspesno izvrsen unos";
    }

    private static String kreirajAudioSnimak(EntityManager em, EntityManager em1, String naziv, int trajanje, Date datum, String email) {
        TypedQuery<Korisnik> korisnikQ = em1.createNamedQuery("Korisnik.findByEmail", Korisnik.class);
        korisnikQ.setParameter("email", email);
        List<Korisnik> korisnikL = korisnikQ.getResultList();
        if (korisnikL == null || korisnikL.isEmpty()) {
            return "Nepostojeci vlasnik";
        }
        Korisnik vlasnik = korisnikL.get(0);
        List<Audiosnimak> audioL = em.createQuery("Select a from Audiosnimak a where a.naziv = '" + naziv + "' and a.idVlasnik = " + vlasnik.getIdKor(), Audiosnimak.class).getResultList();
        if (audioL != null && !audioL.isEmpty()) {
            return "Postoji audio snimak tog korisnika sa istim nazivom";
        }
        Audiosnimak audio = new Audiosnimak();
        audio.setDatum(datum);
        audio.setIdVlasnik(vlasnik.getIdKor());
        audio.setNaziv(naziv);
        audio.setTrajanje(trajanje);
        em.getTransaction().begin();
        em.persist(audio);
        em.getTransaction().commit();
        return "Uspesno izvrsen unos";
    }

    private static Audiosnimak dohvatiSnimak(EntityManager em, EntityManager em1, String naziv, String email) {
        TypedQuery<Korisnik> korisnikQ = em1.createNamedQuery("Korisnik.findByEmail", Korisnik.class);
        korisnikQ.setParameter("email", email);
        List<Korisnik> korisnikL = korisnikQ.getResultList();
        if (korisnikL == null || korisnikL.isEmpty()) {
            System.out.println("Nepostojeci korisnik");
            return null;
        }
        Korisnik vlasnik = korisnikL.get(0);
        TypedQuery<Audiosnimak> audioQ = em.createQuery("Select a from Audiosnimak a WHERE a.idVlasnik = :idv AND a.naziv = :naziv", Audiosnimak.class);
        audioQ.setParameter("idv", vlasnik.getIdKor()).setParameter("naziv", naziv);
        List<Audiosnimak> audioL = audioQ.getResultList();
        if (audioL == null || audioL.isEmpty()) {
            System.out.println("Nema audio snimka sa takvim imenom");
            return null;
        }
        Audiosnimak audioSnimak = audioL.get(0);
        return audioSnimak;
    }

    private static String promeniImeSnimka(EntityManager em, EntityManager em1, String stariNaziv, String noviNaziv, String email) {
        Audiosnimak audioSnimak = dohvatiSnimak(em, em1, stariNaziv, email);
        if (audioSnimak == null) {
            return "Nepostojeci audio snimak";
        }
        em.getTransaction().begin();
        audioSnimak.setNaziv(noviNaziv);
        em.getTransaction().commit();
        return "Uspesno izvrsena promena";
    }

    private static String dodajKategorijuSnimku(EntityManager em, EntityManager em1, String nazivKategorije, String nazivSnimka, String email) {
        TypedQuery<Kategorija> kategorijaQ = em.createNamedQuery("Kategorija.findByNaziv", Kategorija.class).setParameter("naziv", nazivKategorije);
        List<Kategorija> kategorijaL = kategorijaQ.getResultList();
        if (kategorijaL == null || kategorijaL.isEmpty()) {
            return "Nepostojeca kategorija";
        }
        Kategorija kategorija = kategorijaL.get(0);
        Audiosnimak audioSnimak = dohvatiSnimak(em, em1, nazivSnimka, email);
        if (audioSnimak == null) {
            return "Nepostojeci audio snimak";
        }
        TypedQuery<Pripada> pripadaQ = em.createQuery("select p from Pripada p where p.idAud.idAud = " + audioSnimak.getIdAud() + " and p.idKat.idKat = " + kategorija.getIdKat(), Pripada.class);
        List<Pripada> pripadaL = pripadaQ.getResultList();
        if (pripadaL != null && !pripadaL.isEmpty()) {
            return "Postoji pripadnost";
        }
        Pripada pripada = new Pripada();
        pripada.setIdAud(audioSnimak);
        pripada.setIdKat(kategorija);
        List<Pripada> novaLista = audioSnimak.getPripadaList();
        novaLista.add(pripada);
        audioSnimak.setPripadaList(novaLista);
        em.getTransaction().begin();
        em.persist(pripada);
        em.persist(audioSnimak);
        em.getTransaction().commit();
        return "Uspesno izvrsen unos";
    }

    private static String dohvatiKategorije(EntityManager em) {
        em.getTransaction().begin();
        TypedQuery<Kategorija> kategorijaQ = em.createNamedQuery("Kategorija.findAll", Kategorija.class);
        List<Kategorija> kategorijaL = kategorijaQ.getResultList();
        em.getTransaction().commit();
        if (kategorijaL == null || kategorijaL.isEmpty()) {
            System.out.println("Nema kategorija");
            return "Nema kategorija";
        }
        StringBuffer sb = new StringBuffer();
        for (Kategorija k : kategorijaL) {
            sb.append(k.getNaziv());
            if (!(k == kategorijaL.get(kategorijaL.size() - 1))) {
                sb.append(';');
            }
        }
        return sb.toString();
    }

    private static String dohvatiAudioSnimke(EntityManager em, EntityManager em1) {
        TypedQuery<Audiosnimak> audioQ = em.createNamedQuery("Audiosnimak.findAll", Audiosnimak.class);
        List<Audiosnimak> audioL = audioQ.getResultList();
        if (audioL == null || audioL.isEmpty()) {
            System.out.println("Nema audio snimaka");
            return "Nema audio snimaka";
        }
        StringBuffer sb = new StringBuffer();
        for (Audiosnimak a : audioL) {
            List<Korisnik> resultList = em1.createNamedQuery("Korisnik.findByIdKor", Korisnik.class).setParameter("idKor", a.getIdVlasnik()).getResultList();
            if (resultList == null || resultList.isEmpty()) {
                System.out.println("Greska");
                return "Greska";
            }
            Korisnik vlasnik = resultList.get(0);
            String audioString = "" + a.getNaziv() + "|" + a.getDatum() + "|" + a.getTrajanje() + "|" + vlasnik.getEmail();
            sb.append(audioString);
            if (!(a == audioL.get(audioL.size() - 1))) {
                sb.append(';');
            }
        }
        return sb.toString();
    }

    private static String dohvatiKategorijeSnimak(EntityManager em, EntityManager em1, String naziv, String email) {
        Audiosnimak audioSnimak = dohvatiSnimak(em, em1, naziv, email);
        if (audioSnimak == null) {
            return "Nepostojeci snimak";
        }
        StringBuffer sb = new StringBuffer();
        if (audioSnimak.getPripadaList().isEmpty()) {
            return "Nema kategoriju";
        }
        for (Pripada p : audioSnimak.getPripadaList()) {
            sb.append(p.getIdKat().getNaziv());
            if (!(p == audioSnimak.getPripadaList().get(audioSnimak.getPripadaList().size() - 1))) {
                sb.append(';');
            }
        }
        return sb.toString();
    }

    private static String obrisiSnimak(EntityManager em, EntityManager em1, String email, String naziv) {
        Audiosnimak audioSnimak = dohvatiSnimak(em, em1, naziv, email);
        if (audioSnimak == null) {
            return "Nepostojeci audio snimak";
        }
        em.getTransaction().begin();
        em.remove(audioSnimak);
        em.getTransaction().commit();
        return "Obrisano";
    }

}
