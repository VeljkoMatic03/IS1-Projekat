/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ent3;

import entities.Audiosnimak;
import entities.Favouritelist;
import entities.Korisnik;
import entities.Ocena;
import entities.Paket;
import entities.Pretplata;
import entities.Slusanje;
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

    @Resource(lookup = "podsistem3Topic")
    private static Topic podsistem3Topic;

    @Resource(lookup = "appToP3Topic")
    private static Topic appToP3Topic;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ent3PU");
        if (emf == null) {
            System.out.println("emf == null");
        }
        EntityManagerFactory emf2 = Persistence.createEntityManagerFactory("ent2PU");
        if (emf2 == null) {
            System.out.println("emf2 == null");
        }
        EntityManagerFactory emf1 = Persistence.createEntityManagerFactory("ent1PU");
        if (emf1 == null) {
            System.out.println("emf1 == null");
        }

        EntityManager em = emf.createEntityManager();
        EntityManager em2 = emf2.createEntityManager();
        EntityManager em1 = emf1.createEntityManager();

        JMSContext context = connectionFactory.createContext();
        JMSConsumer consumer = context.createConsumer(podsistem3Topic);
        JMSProducer producer = context.createProducer();
        System.out.println("Start App 3");

        String reply, naziv, email, datumStr, emailSlusalac, emailAutor;
        int ocena;
        float cena;
        Date datum;
        TextMessage replyMsg;
        while (true) {
            try {
                TextMessage request = (TextMessage) consumer.receive();
                String requestStr = request.getText();
                switch (requestStr) {
                    case "kreirajPaket":
                        naziv = request.getStringProperty("naziv");
                        cena = (float) request.getIntProperty("cena");
                        reply = kreirajPaket(em, naziv, cena);
                        replyMsg = context.createTextMessage(reply);
                        replyMsg.setJMSCorrelationID(request.getJMSCorrelationID());
                        producer.send(appToP3Topic, replyMsg);
                        System.out.println("Poruka poslata - kreiraj paket");
                    break;
                    case "promeniCenuPaket":
                        naziv = request.getStringProperty("naziv");
                        cena = (float) request.getIntProperty("cena");
                        reply = promeniCenu(em, naziv, cena);
                        replyMsg = context.createTextMessage(reply);
                        replyMsg.setJMSCorrelationID(request.getJMSCorrelationID());
                        producer.send(appToP3Topic, replyMsg);
                        System.out.println("Poruka poslata - promeni cenu paketa");
                    break;
                    case "kreirajPretplatu":
                        email = request.getStringProperty("email");
                        naziv = request.getStringProperty("naziv");
                        cena = (float) request.getIntProperty("cena");
                        datumStr = request.getStringProperty("datum");
                        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        try {
                            datum = dateFormat.parse(datumStr);
                        } catch (ParseException ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                            datum = new Date();
                        }
                        reply = kreirajPretplatu(em, em1, email, naziv, cena, datum);
                        replyMsg = context.createTextMessage(reply);
                        replyMsg.setJMSCorrelationID(request.getJMSCorrelationID());
                        producer.send(appToP3Topic, replyMsg);
                        System.out.println("Poruka poslata - kreiraj pretplatu");
                    break;
                    case "kreirajSlusanje":
                        emailSlusalac = request.getStringProperty("slusalac");
                        emailAutor = request.getStringProperty("autor");
                        naziv = request.getStringProperty("naziv");
                        int vremeSlusanja = request.getIntProperty("vreme");
                        int pocetnaSekunda = request.getIntProperty("pocetak");
                        reply = kreirajSlusanje(em, em1, em2, emailSlusalac, naziv, emailAutor, vremeSlusanja, pocetnaSekunda);
                        replyMsg = context.createTextMessage(reply);
                        replyMsg.setJMSCorrelationID(request.getJMSCorrelationID());
                        producer.send(appToP3Topic, replyMsg);
                        System.out.println("Poruka poslata - kreiraj slusanje");
                    break;
                    case "dodajOmiljeni" :
                        emailSlusalac = request.getStringProperty("slusalac");
                        emailAutor = request.getStringProperty("autor");
                        naziv = request.getStringProperty("naziv");
                        reply = dodajOmiljeni(em, em1, em2, naziv, emailAutor, emailSlusalac);
                        replyMsg = context.createTextMessage(reply);
                        replyMsg.setJMSCorrelationID(request.getJMSCorrelationID());
                        producer.send(appToP3Topic, replyMsg);
                        System.out.println("Poruka poslata - dodaj omiljeni");
                    break;
                    case "kreirajOcenu" :
                        emailSlusalac = request.getStringProperty("slusalac");
                        emailAutor = request.getStringProperty("autor");
                        naziv = request.getStringProperty("naziv");
                        ocena = request.getIntProperty("ocena");
                        reply = kreirajOcenu(em, em1, em2, naziv, emailSlusalac, emailAutor, ocena);
                        replyMsg = context.createTextMessage(reply);
                        replyMsg.setJMSCorrelationID(request.getJMSCorrelationID());
                        producer.send(appToP3Topic, replyMsg);
                        System.out.println("Poruka poslata - kreiraj ocenu");
                    break;
                    case "promeniOcenu" :
                        emailSlusalac = request.getStringProperty("slusalac");
                        emailAutor = request.getStringProperty("autor");
                        naziv = request.getStringProperty("naziv");
                        ocena = request.getIntProperty("ocena");
                        reply = promeniOcenu(em, em1, em2, naziv, emailSlusalac, emailAutor, ocena);
                        replyMsg = context.createTextMessage(reply);
                        replyMsg.setJMSCorrelationID(request.getJMSCorrelationID());
                        producer.send(appToP3Topic, replyMsg);
                        System.out.println("Poruka poslata - promeni ocenu");
                    break;
                    case "obrisiOcenu" :
                        emailSlusalac = request.getStringProperty("slusalac");
                        emailAutor = request.getStringProperty("autor");
                        naziv = request.getStringProperty("naziv");
                        reply = obrisiOcenu(em, em1, em2, naziv, emailSlusalac, emailAutor);
                        replyMsg = context.createTextMessage(reply);
                        replyMsg.setJMSCorrelationID(request.getJMSCorrelationID());
                        producer.send(appToP3Topic, replyMsg);
                        System.out.println("Poruka poslata - obrisi ocenu");
                    break;
                    case "dohvatiPakete" :
                        reply = dohvatiPakete(em);
                        replyMsg = context.createTextMessage(reply);
                        replyMsg.setJMSCorrelationID(request.getJMSCorrelationID());
                        replyMsg.setStringProperty("header", "Naziv|Cena;");
                        producer.send(appToP3Topic, replyMsg);
                        System.out.println("Poruka poslata - dohvati pakete");
                    break;
                    case "dohvatiPretplate" :
                        email = request.getStringProperty("email");
                        reply = dohvatiPretplate(em, em1, email);
                        replyMsg = context.createTextMessage(reply);
                        replyMsg.setJMSCorrelationID(request.getJMSCorrelationID());
                        replyMsg.setStringProperty("header", "Naziv|Datum|Cena;");
                        producer.send(appToP3Topic, replyMsg);
                        System.out.println("Poruka poslata - dohvati pretplate");
                    break;
                    case "dohvatiSlusanja" :
                        naziv = request.getStringProperty("naziv");
                        email = request.getStringProperty("email");
                        reply = dohvatiSlusanjaAudio(em, em1, em2, naziv, email);
                        replyMsg = context.createTextMessage(reply);
                        replyMsg.setJMSCorrelationID(request.getJMSCorrelationID());
                        replyMsg.setStringProperty("header", "Ime|Datum|Pocetak|Vreme;");
                        producer.send(appToP3Topic, replyMsg);
                        System.out.println("Poruka poslata - dohvati slusanja");
                    break;
                    case "dohvatiOcene" :
                        naziv = request.getStringProperty("naziv");
                        email = request.getStringProperty("email");
                        reply = dohvatiOceneAudio(em, em1, em2, naziv, email);
                        replyMsg = context.createTextMessage(reply);
                        replyMsg.setJMSCorrelationID(request.getJMSCorrelationID());
                        replyMsg.setStringProperty("header", "Ime|Ocena|Datum;");
                        producer.send(appToP3Topic, replyMsg);
                        System.out.println("Poruka poslata - dohvati ocene");
                    break;
                    case "dohvatiOmiljene" :
                        email = request.getStringProperty("email");
                        reply = dohvatiListuOmiljenih(em, em1, em2, email);
                        replyMsg = context.createTextMessage(reply);
                        replyMsg.setJMSCorrelationID(request.getJMSCorrelationID());
                        replyMsg.setStringProperty("header", "Naziv|Ime|Trajanje|Datum;");
                        producer.send(appToP3Topic, replyMsg);
                        System.out.println("Poruka poslata - dohvati omiljene");
                    break;
                }
            } catch (JMSException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static String kreirajPaket(EntityManager em, String naziv, float cena) {
        naziv = naziv.toLowerCase();
        TypedQuery<Paket> paketQ = em.createNamedQuery("Paket.findByNaziv", Paket.class);
        paketQ.setParameter("naziv", naziv);
        List<Paket> paketL = paketQ.getResultList();
        if (paketL != null && !paketL.isEmpty()) {
            return "Postoji paket sa datim imenom";
        }
        Paket paket = new Paket();
        paket.setCena(cena);
        paket.setNaziv(naziv);
        em.getTransaction().begin();
        em.persist(paket);
        em.getTransaction().commit();
        return "Uspesno izvrsen unos";
    }

    private static Paket dohvatiPaket(EntityManager em, String naziv) {
        naziv = naziv.toLowerCase();
        TypedQuery<Paket> paketQ = em.createNamedQuery("Paket.findByNaziv", Paket.class);
        paketQ.setParameter("naziv", naziv);
        List<Paket> paketL = paketQ.getResultList();
        if (paketL == null || paketL.isEmpty()) {
            System.out.println("Nepostojeci paket");
            return null;
        }
        return paketL.get(0);
    }

    private static String promeniCenu(EntityManager em, String naziv, float novaCena) {
        Paket paket = dohvatiPaket(em, naziv);
        if (paket == null) {
            return "Nepostojeci paket";
        }
        em.getTransaction().begin();
        paket.setCena(novaCena);
        em.getTransaction().commit();
        return "Uspesno izvrsena izmena";
    }

    private static Korisnik dohvatiKorisnika(EntityManager em1, String email) {
        TypedQuery<Korisnik> korisnikQ = em1.createNamedQuery("Korisnik.findByEmail", Korisnik.class);
        korisnikQ.setParameter("email", email);
        List<Korisnik> korisnikL = korisnikQ.getResultList();
        if (korisnikL == null || korisnikL.isEmpty()) {
            System.out.println("Nepostojeci korisnik");
            return null;
        }
        Korisnik korisnik = korisnikL.get(0);
        return korisnik;
    }

    // Ukoliko korisnik ima pretplatu, ta pretplata se azurira novom cenom, paketom i datumom; u suprotnom se kreira pretplata za korisnika
    private static String kreirajPretplatu(EntityManager em, EntityManager em1, String email, String nazivPaketa, float cena, Date datum) {
        if (datum == null) {
            datum = new Date();
        }
        Paket paket = dohvatiPaket(em, nazivPaketa);
        if (paket == null) {
            return "Nepostojeci paket";
        }
        if (cena == -1 || cena > paket.getCena()) {
            cena = paket.getCena();
        }

        Korisnik korisnik = dohvatiKorisnika(em1, email);
        if (korisnik == null) {
            return "Nepostojeci korisnik";
        }

        TypedQuery<Pretplata> pretplataQ = em.createNamedQuery("Pretplata.findByIdKor", Pretplata.class);
        pretplataQ.setParameter("idKor", korisnik.getIdKor());
        List<Pretplata> pretplataL = pretplataQ.getResultList();
        if (pretplataL == null || pretplataL.isEmpty()) {
            Pretplata pretplata = new Pretplata();
            pretplata.setCena(cena);
            pretplata.setDatumPocetka(datum);
            pretplata.setIdKor(korisnik.getIdKor());
            pretplata.setIdPak(paket);
            List<Pretplata> nova = paket.getPretplataList();
            nova.add(pretplata);
            paket.setPretplataList(nova);
            em.getTransaction().begin();
            em.persist(pretplata);
            em.persist(paket);
            em.getTransaction().commit();
            return "Uspesno kreirana pretplata";
        }

        int max = -1;
        Pretplata pretplata = null;
        for (Pretplata p : pretplataL) {
            if (p.getIdPre() > max) {
                pretplata = p;
            }
        }
        if ((datum.getTime() - pretplata.getDatumPocetka().getTime()) / 1000 < 30 * 24 * 60 * 60) {
            em.getTransaction().begin();
            pretplata.setCena(cena);
            pretplata.setDatumPocetka(datum);
            pretplata.setIdPak(paket);
            em.getTransaction().commit();
            return "Uspesno azurirana pretplata";
        }
        pretplata = new Pretplata();
        pretplata.setCena(cena);
        pretplata.setDatumPocetka(datum);
        pretplata.setIdKor(korisnik.getIdKor());
        pretplata.setIdPak(paket);
        List<Pretplata> nova = paket.getPretplataList();
        nova.add(pretplata);
        paket.setPretplataList(nova);
        em.getTransaction().begin();
        em.persist(pretplata);
        em.persist(paket);
        em.getTransaction().commit();
        return "Uspesno kreirana pretplata";
    }

    private static Audiosnimak dohvatiAudio(EntityManager em2, String nazivSnimka, Korisnik vlasnik) {
        TypedQuery<Audiosnimak> audioQ = em2.createQuery("select a from Audiosnimak a where a.idVlasnik = " + vlasnik.getIdKor() + " and a.naziv = '" + nazivSnimka + "'", Audiosnimak.class);
        List<Audiosnimak> audioL = audioQ.getResultList();
        if (audioL == null || audioL.isEmpty()) {
            System.out.println("Nepostojeci audio snimak");
            return null;
        }
        return audioL.get(0);
    }

    private static String kreirajSlusanje(EntityManager em, EntityManager em1, EntityManager em2, String email, String nazivSnimka, String emailVlasnika,
            int vremeSlusanja, int pocetnaSek) {
        if (vremeSlusanja < 0 || pocetnaSek < 0) {
            return "Neispravni podaci";
        }

        Korisnik korisnik = dohvatiKorisnika(em1, email);
        if (korisnik == null) {
            return "Nepostojeci korisnik";
        }

        Korisnik vlasnik = dohvatiKorisnika(em1, emailVlasnika);
        if (vlasnik == null) {
            return "Nepostojeci vlasnik/izvodjac pesme";
        }

        Audiosnimak audio = dohvatiAudio(em2, nazivSnimka, vlasnik);
        if (audio == null) {
            return "Nepostojeci audio snimak";
        }

        if (audio.getTrajanje() < vremeSlusanja + pocetnaSek) {
            return "Neispravno vreme slusanja";
        }

        Slusanje slusanje = new Slusanje();
        slusanje.setDatum(new Date());
        slusanje.setPocetakSek(pocetnaSek);
        slusanje.setVremeSlusanja(vremeSlusanja);
        slusanje.setIdAud(audio.getIdAud());
        slusanje.setIdKor(korisnik.getIdKor());

        em.getTransaction().begin();
        em.persist(slusanje);
        em.getTransaction().commit();
        return "Uspesno kreirano novo slusanje";
    }

    private static String dodajOmiljeni(EntityManager em, EntityManager em1, EntityManager em2, String naziv, String emailVlasnik, String emailKorisnik) {
        Korisnik korisnik = dohvatiKorisnika(em1, emailKorisnik);
        if (korisnik == null) {
            return "Nepostojeci korisnik";
        }

        Korisnik vlasnik = dohvatiKorisnika(em1, emailVlasnik);
        if (vlasnik == null) {
            return "Nepostojeci vlasnik snimka";
        }

        Audiosnimak audio = dohvatiAudio(em2, naziv, vlasnik);
        if (audio == null) {
            return "Nepostojeci audio snimak";
        }

        TypedQuery<Favouritelist> favListQ = em.createNamedQuery("Favouritelist.findByIdKor", Favouritelist.class);
        favListQ.setParameter("idKor", korisnik.getIdKor());
        List<Favouritelist> favList = favListQ.getResultList();
        for (Favouritelist favSong : favList) {
            if (favSong.getFavouritelistPK().getIdAud() == audio.getIdAud()) {
                return "Pesma je vec na listi omiljenih pesama";
            }
        }
        Favouritelist newFavSong = new Favouritelist(korisnik.getIdKor(), audio.getIdAud());
        em.getTransaction().begin();
        em.persist(newFavSong);
        em.getTransaction().commit();
        return "Pesma dodata na listu";
    }

    private static String kreirajOcenu(EntityManager em, EntityManager em1, EntityManager em2, String naziv, String emailKorisnik, String emailVlasnik, int ocena) {
        if (ocena < 0 || ocena > 10) {
            return "Nevalidna ocena";
        }
        String result = promeniOcenu(em, em1, em2, naziv, emailKorisnik, emailVlasnik, ocena);
        System.out.println(result);
        if (result.equalsIgnoreCase("Uspesno izvrsena promena ocene")) {
            return "Uspesno izvrsena promena ocene";
        }
        Korisnik korisnik = dohvatiKorisnika(em1, emailKorisnik);
        if (korisnik == null) {
            return "Nepostojeci korisnik";
        }
        Korisnik vlasnik = dohvatiKorisnika(em1, emailVlasnik);
        if (vlasnik == null) {
            return "Nepostojeci vlasnik";
        }
        Audiosnimak audio = dohvatiAudio(em2, naziv, vlasnik);
        if (audio == null) {
            return "Nepostojeci audio snimak";
        }
        Ocena ocenaE = new Ocena();
        ocenaE.setDatum(new Date());
        ocenaE.setOcena(ocena);
        ocenaE.setIdAud(audio.getIdAud());
        ocenaE.setIdKor(korisnik.getIdKor());
        em.getTransaction().begin();
        em.persist(ocenaE);
        em.getTransaction().commit();
        return "Uspesno kreirana ocena";
    }

    private static String promeniOcenu(EntityManager em, EntityManager em1, EntityManager em2, String naziv, String emailKorisnik, String emailVlasnik, int ocena) {
        if (ocena < 0 || ocena > 10) {
            return "Nevalidna ocena";
        }
        Korisnik korisnik = dohvatiKorisnika(em1, emailKorisnik);
        if (korisnik == null) {
            return "Nepostojeci korisnik";
        }
        Korisnik vlasnik = dohvatiKorisnika(em1, emailVlasnik);
        if (vlasnik == null) {
            return "Nepostojeci vlasnik";
        }
        Audiosnimak audio = dohvatiAudio(em2, naziv, vlasnik);
        if (audio == null) {
            return "Nepostojeci audio snimak";
        }

        TypedQuery<Ocena> ocenaQ = em.createQuery("Select o from Ocena o where o.idKor = " + korisnik.getIdKor() + " and o.idAud = " + audio.getIdAud(), Ocena.class);
        List<Ocena> ocenaL = ocenaQ.getResultList();
        if (ocenaL == null || ocenaL.isEmpty()) {
            return "Korisnik nije dodelio ocenu ovom audio snimku";
        }
        Ocena ocenaE = ocenaL.get(0);
        em.getTransaction().begin();
        ocenaE.setOcena(ocena);
        ocenaE.setDatum(new Date());
        em.getTransaction().commit();
        return "Uspesno izvrsena promena ocene";
    }

    private static String obrisiOcenu(EntityManager em, EntityManager em1, EntityManager em2, String naziv, String emailKorisnik, String emailVlasnik) {
        Korisnik korisnik = dohvatiKorisnika(em1, emailKorisnik);
        if (korisnik == null) {
            return "Nepostojeci korisnik";
        }
        Korisnik vlasnik = dohvatiKorisnika(em1, emailVlasnik);
        if (vlasnik == null) {
            return "Nepostojeci vlasnik audio snimka";
        }
        Audiosnimak audio = dohvatiAudio(em2, naziv, vlasnik);
        if (audio == null) {
            return "Nepostojeci audio snimak";
        }

        TypedQuery<Ocena> ocenaQ = em.createQuery("select o from Ocena o where o.idAud = " + audio.getIdAud() + " and o.idKor = " + korisnik.getIdKor(), Ocena.class);
        List<Ocena> ocenaL = ocenaQ.getResultList();
        if (ocenaL == null || ocenaL.isEmpty()) {
            return "Korisnik nije ocenio audio snimak";
        }
        Ocena ocena = ocenaL.get(0);
        em.getTransaction().begin();
        em.remove(ocena);
        em.getTransaction().commit();
        return "Uspesno obrisana ocena";
    }

    private static String dohvatiPakete(EntityManager em) {
        List<Paket> paketL = em.createNamedQuery("Paket.findAll", Paket.class).getResultList();
        if (paketL == null || paketL.isEmpty()) {
            return "Nema nijednog paketa";
        }
        StringBuffer sb = new StringBuffer();
        for (Paket paket : paketL) {
            sb.append(paket.getNaziv()).append("|").append(paket.getCena());
            if (!(paket == paketL.get(paketL.size() - 1))) {
                sb.append(';');
            }
        }
        return sb.toString();
    }

    private static String dohvatiPretplate(EntityManager em, EntityManager em1, String email) {
        Korisnik korisnik = dohvatiKorisnika(em1, email);
        if (korisnik == null) {
            return "Nepostojeci korisnik";
        }
        TypedQuery<Pretplata> pretplataQ = em.createNamedQuery("Pretplata.findByIdKor", Pretplata.class).setParameter("idKor", korisnik.getIdKor());
        List<Pretplata> pretplataL = pretplataQ.getResultList();
        if (pretplataL == null || pretplataL.isEmpty()) {
            return "Ne postoje pretplate za datog korisnika";
        }
        StringBuffer sb = new StringBuffer();
        for (Pretplata pretplata : pretplataL) {
            Paket paket = pretplata.getIdPak();
            sb.append(paket.getNaziv()).append('|').append(pretplata.getDatumPocetka()).append('|').append(pretplata.getCena());
            if (!(pretplata == pretplataL.get(pretplataL.size() - 1))) {
                sb.append(';');
            }
        }
        return sb.toString();
    }

    private static String dohvatiSlusanjaAudio(EntityManager em, EntityManager em1, EntityManager em2, String naziv, String email) {
        Korisnik vlasnik = dohvatiKorisnika(em1, email);
        if (vlasnik == null) {
            return "Nepostojeci izvodjac/vlasnik snimka";
        }
        Audiosnimak audio = dohvatiAudio(em2, naziv, vlasnik);
        if (audio == null) {
            return "Nepostojeci audio snimak";
        }
        List<Slusanje> slusanjeL = em.createNamedQuery("Slusanje.findByIdAud", Slusanje.class).setParameter("idAud", audio.getIdAud()).getResultList();
        if (slusanjeL == null || slusanjeL.isEmpty()) {
            return "Nema slusanja za ovaj audio snimak";
        }
        StringBuffer sb = new StringBuffer();
        for (Slusanje slusanje : slusanjeL) {
            Korisnik slusalac = em1.find(Korisnik.class, slusanje.getIdKor());
            sb.append(slusalac.getIme()).append('|').append(slusanje.getDatum()).append('|').
                    append(slusanje.getPocetakSek()).append('|').append(slusanje.getVremeSlusanja());
            if (!(slusanje == slusanjeL.get(slusanjeL.size() - 1))) {
                sb.append(';');
            }
        }
        return sb.toString();
    }

    private static String dohvatiOceneAudio(EntityManager em, EntityManager em1, EntityManager em2, String naziv, String email) {
        Korisnik vlasnik = dohvatiKorisnika(em1, email);
        if (vlasnik == null) {
            return "Nepostojeci izvodjac/vlasnik snimka";
        }
        Audiosnimak audio = dohvatiAudio(em2, naziv, vlasnik);
        if (audio == null) {
            return "Nepostojeci audio snimak";
        }
        List<Ocena> ocenaL = em.createNamedQuery("Ocena.findByIdAud", Ocena.class).setParameter("idAud", audio.getIdAud()).getResultList();
        if (ocenaL == null || ocenaL.isEmpty()) {
            return "Ne postoje ocene za ovaj audio snimak";
        }
        StringBuffer sb = new StringBuffer();
        for (Ocena ocena : ocenaL) {
            Korisnik recenzent = em1.find(Korisnik.class, ocena.getIdKor());
            sb.append(recenzent.getIme()).append('|').append(ocena.getOcena()).append('|').append(ocena.getDatum());
            if (!(ocena == ocenaL.get(ocenaL.size() - 1))) {
                sb.append(';');
            }
        }
        return sb.toString();
    }

    private static String dohvatiListuOmiljenih(EntityManager em, EntityManager em1, EntityManager em2, String email) {
        Korisnik korisnik = dohvatiKorisnika(em1, email);
        if (korisnik == null) {
            return "Nepostojeci korisnik";
        }
        List<Favouritelist> favList = em.createNamedQuery("Favouritelist.findByIdKor", Favouritelist.class).setParameter("idKor", korisnik.getIdKor()).getResultList();
        if (favList == null || favList.isEmpty()) {
            return "Korisnik nema omiljene pesme";
        }
        StringBuffer sb = new StringBuffer();
        for (Favouritelist favListElem : favList) {
            Audiosnimak audio = em2.find(Audiosnimak.class, favListElem.getFavouritelistPK().getIdAud());
            Korisnik vlasnik = em1.find(Korisnik.class, audio.getIdVlasnik());
            sb.append(audio.getNaziv()).append('|').append(vlasnik.getIme()).append('|').append(audio.getTrajanje()).append('|').append(audio.getDatum());
            if (!(favListElem == favList.get(favList.size() - 1))) {
                sb.append(';');
            }
        }
        return sb.toString();
    }

}
