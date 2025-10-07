/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ent1;

import entities.Korisnik;
import entities.Mesto;
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
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 *
 * @author HP
 */
public class Main {
    
    @Resource(lookup = "projectConnFactory")
    private static ConnectionFactory connectionFactory;
    
    @Resource(lookup = "appToP1Topic")
    private static Topic appToP1Topic;
    
    @Resource(lookup = "podsistem1Topic")
    private static Topic podsistem1Topic;
    
//    @Resource(lookup = "q1")
//    private static Queue q1;
//    
//    @Resource(lookup = "q2")
//    private static Queue q2;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ent1PU");
        if(emf == null) System.out.println("null");
        EntityManager em = emf.createEntityManager();
        
        JMSContext context = connectionFactory.createContext();
        JMSConsumer consumer = context.createConsumer(podsistem1Topic);
        JMSProducer producer = context.createProducer();
        System.out.println("Start App 1");
        
        String reply;
        TextMessage replyMsg;
        while(true) {
            try {
                TextMessage request = (TextMessage) consumer.receive();
                String requestStr = request.getText();
                switch(requestStr) {
                    case "dohvatiKorisnike" :
                        reply = dohvatiKorisnike(em);
                        replyMsg = context.createTextMessage(reply);
                        replyMsg.setJMSCorrelationID(request.getJMSCorrelationID());
                        replyMsg.setStringProperty("header", "Ime|Email|Godiste|Pol|Mesto;");
                        producer.send(appToP1Topic, replyMsg);
                        System.out.println("Poruka poslata - dohvati korisnike");
                    break;
                    case "kreirajKorisnika" :
                        String ime = request.getStringProperty("ime");
                        String email = request.getStringProperty("email");
                        String pol = request.getStringProperty("pol");
                        String mesto = request.getStringProperty("mesto");
                        int godiste = request.getIntProperty("godiste");
                        reply = kreirajKorisnika(em, ime, email, godiste, pol, mesto);
                        replyMsg = context.createTextMessage(reply);
                        replyMsg.setJMSCorrelationID(request.getJMSCorrelationID());
                        producer.send(appToP1Topic, replyMsg);
                        System.out.println("Poruka poslata - kreiraj korisnika");
                    break;
                    case "promeniEmail" :
                        String novi = request.getStringProperty("novi");
                        String stari = request.getStringProperty("stari");
                        reply = promeniEmail(em, stari, novi);
                        replyMsg = context.createTextMessage(reply);
                        replyMsg.setJMSCorrelationID(request.getJMSCorrelationID());
                        producer.send(appToP1Topic, replyMsg);
                        System.out.println("Poruka poslata - promeni email");
                    break;
                    case "promeniMesto" :
                        String emailK = request.getStringProperty("email");
                        String mestoK = request.getStringProperty("mesto");
                        reply = promeniMesto(em, emailK, mestoK);
                        replyMsg = context.createTextMessage(reply);
                        replyMsg.setJMSCorrelationID(request.getJMSCorrelationID());
                        producer.send(appToP1Topic, replyMsg);
                        System.out.println("Poruka poslata - promeni mesto");
                    break;
                    case "dohvatiMesta" :
                        reply = dohvatiMesta(em);
                        replyMsg = context.createTextMessage(reply);
                        replyMsg.setJMSCorrelationID(request.getJMSCorrelationID());
                        replyMsg.setStringProperty("header", "Naziv;");
                        producer.send(appToP1Topic, replyMsg);
                        System.out.println("Poruka poslata - dohvati mesta");
                    break;
                    case "kreirajMesto" :
                        String naziv = request.getStringProperty("naziv");
                        reply = kreirajMesto(em, naziv);
                        replyMsg = context.createTextMessage(reply);
                        replyMsg.setJMSCorrelationID(request.getJMSCorrelationID());
                        producer.send(appToP1Topic, replyMsg);
                        System.out.println("Poruka poslata - kreiraj mesto");
                    break;
                }
            } catch (JMSException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private static String kreirajMesto(EntityManager em, String naziv) {
        TypedQuery<Mesto> q1 = em.createQuery("SELECT m FROM Mesto m WHERE m.naziv = :naziv", Mesto.class);
        q1.setParameter("naziv", naziv);
        List<Mesto> mList = q1.getResultList();
        if(mList == null) return "Doslo je do greske.";
        if(!mList.isEmpty()) {
            Mesto m = mList.get(0);
            String retval = "Postoji mesto: " + m.getIdMes() + " " + m.getNaziv();
            return retval;
        }
        Mesto m = new Mesto();
        m.setNaziv(naziv);
        em.getTransaction().begin();
        em.persist(m);
        em.getTransaction().commit();
        return "Kreirano u bazi";
    }
    
    private static String kreirajKorisnika(EntityManager em, String ime, String email, int godiste, String pol, String nazivMesta) {
        TypedQuery<Mesto> mestoQuery = em.createNamedQuery("Mesto.findByNaziv", Mesto.class);
        mestoQuery.setParameter("naziv", nazivMesta);
        Mesto mesto = mestoQuery.getResultList().isEmpty() ? null : mestoQuery.getResultList().get(0);
        if(mesto == null) {
            return "Nema odgovarajuceg mesta u bazi";
        }
        TypedQuery<Korisnik> korisnikQuery = em.createNamedQuery("Korisnik.findByEmail", Korisnik.class);
        korisnikQuery.setParameter("email", email);
        if(!korisnikQuery.getResultList().isEmpty()) {
            return "Postoji korisnik sa datim e-mailom: " + korisnikQuery.getResultList().get(0).getIme();
        }
        Korisnik korisnik = new Korisnik();
        korisnik.setEmail(email);
        korisnik.setGodiste(godiste);
        korisnik.setIme(ime);
        korisnik.setPol(pol);
        korisnik.setIdMes(mesto);
        em.getTransaction().begin();
        em.persist(korisnik);
        em.getTransaction().commit();
        return "Uspesno unet korisnik";
    }
    
    private static String promeniEmail(EntityManager em, String stariEmail, String noviEmail) {
        TypedQuery<Korisnik> korisnikQ = em.createNamedQuery("Korisnik.findByEmail", Korisnik.class);
        korisnikQ.setParameter("email", stariEmail);
        List<Korisnik> korisnikList = korisnikQ.getResultList();
        if(korisnikList == null || korisnikList.isEmpty()) {
            return "Nepostojeci korisnik";
        }
        Korisnik korisnik = korisnikList.get(0);
        em.getTransaction().begin();
        korisnik.setEmail(noviEmail);
        em.getTransaction().commit();
        return "Uspesno izvrsena izmena";
    }
    private static String promeniMesto(EntityManager em, String email, String nazivMesta) {
        TypedQuery<Korisnik> korisnikQ = em.createNamedQuery("Korisnik.findByEmail", Korisnik.class);
        korisnikQ.setParameter("email", email);
        List<Korisnik> korisnikL = korisnikQ.getResultList();
        if(korisnikL == null || korisnikL.isEmpty()) {
            return "Nema korisnika sa datim e-mailom";
        }
        Korisnik korisnik = korisnikL.get(0);
        TypedQuery<Mesto> mestoQ = em.createNamedQuery("Mesto.findByNaziv", Mesto.class);
        mestoQ.setParameter("naziv", nazivMesta);
        List<Mesto> mestoL = mestoQ.getResultList();
        if(mestoL == null || mestoL.isEmpty()) {
            return "Nema mesta sa datim nazivom";
        }
        Mesto mesto = mestoL.get(0);
        em.getTransaction().begin();
        korisnik.setIdMes(mesto);
        em.getTransaction().commit();
        return "Uspesno izvrsena promena";
    }
    private static String dohvatiMesta(EntityManager em) {
        TypedQuery<Mesto> mestoQ = em.createNamedQuery("Mesto.findAll", Mesto.class);
        List<Mesto> mestoL = mestoQ.getResultList();
        StringBuffer sb = new StringBuffer();
        for (Mesto mesto : mestoL) {
            sb.append(mesto.getNaziv());
            if(!(mesto == mestoL.get(mestoL.size() - 1))) {
                sb.append(';');
            }
        }
        return sb.toString();
    }
    
    private static String dohvatiKorisnike(EntityManager em) {
        TypedQuery<Korisnik> korisnikQ = em.createNamedQuery("Korisnik.findAll", Korisnik.class);
        List<Korisnik> korisnikL = korisnikQ.getResultList();
        StringBuffer sb = new StringBuffer();
        for(Korisnik korisnik : korisnikL) {
            String korisnikString = "" + korisnik.getIme() + "|" + korisnik.getEmail() + "|" + korisnik.getGodiste() + "|" + korisnik.getPol() + "|" 
                    + korisnik.getIdMes().getNaziv();
            sb.append(korisnikString);
            if(!(korisnik == korisnikL.get(korisnikL.size() - 1))) {
                sb.append(';');
            }
        }
        return sb.toString();
    }
    private static String dohvatiID(EntityManager em, String email) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        TypedQuery<Korisnik> korisnikQ = em.createNamedQuery("Korisnik.findByEmail", Korisnik.class);
        korisnikQ.setParameter("email", email);
        Korisnik korisnik = korisnikQ.getResultList().isEmpty() ? null : korisnikQ.getResultList().get(0);
        tx.commit();
        if(korisnik == null) return "Nepostojeci korisnik";
        return "" + korisnik.getIdKor();
    }
    
}
