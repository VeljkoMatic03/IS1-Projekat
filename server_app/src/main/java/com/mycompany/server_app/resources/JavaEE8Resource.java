package com.mycompany.server_app.resources;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ws.rs.PUT;

/**
 *
 * @author
 */
@Path("korisnici")
public class JavaEE8Resource {

    @Resource(lookup = "projectConnFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "appToP1Topic")
    private Topic appToP1Topic;

    @Resource(lookup = "podsistem1Topic")
    private Topic podsistem1Topic;

    @GET
    public Response getKorisnici() {
        try {
            System.out.println("getKorisnici.start");
            String correlationId = UUID.randomUUID().toString();

            JMSContext context = connectionFactory.createContext();
            JMSConsumer responseConsumer = context.createConsumer(appToP1Topic, "JMSCorrelationID='" + correlationId + "'");
            JMSProducer producer = context.createProducer();

            TextMessage message = context.createTextMessage("dohvatiKorisnike");
            message.setJMSCorrelationID(correlationId);

            producer.send(podsistem1Topic, message);
            Message response = responseConsumer.receive();

            if (response == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            String txt = ((TextMessage) response).getText();
            String header = response.getStringProperty("header");
            txt = header.concat(txt);
            return Response.ok(txt).build();
        } catch (JMSException ex) {
            Logger.getLogger(JavaEE8Resource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.ok("Error").build();
        }
    }

//    @Path("{email}")
//    @GET
//    public Response getID(@PathParam("email") String email) {
//        try {
//            System.out.println("getID.start");
//            String correlationId = UUID.randomUUID().toString();
//            JMSContext context = connectionFactory.createContext();
//            JMSConsumer responseConsumer = context.createConsumer(appToP1Topic, "JMSCorrelationID='" + correlationId +  "'");
//            JMSProducer producer = context.createProducer();
//            
//            TextMessage message = context.createTextMessage("dohvatiID");
//            message.setJMSCorrelationID(correlationId);
//            message.setStringProperty("email", email);
//            
//            producer.send(podsistem1Topic, message);
//            //System.out.println("L1");
//            Message response = responseConsumer.receive();
//            //System.out.println("L2");
//            
//            if(response == null) return Response.status(Response.Status.NOT_FOUND).build();
//            String txt = ((TextMessage) response).getText();
//            return Response.ok(txt).build();
//        } catch (JMSException ex) {
//            Logger.getLogger(JavaEE8Resource.class.getName()).log(Level.SEVERE, null, ex);
//            return Response.ok("Error").build();
//        }
//    }
    
    
    @Path("novi")
    @POST
    public Response createKorisnik(
            @QueryParam("ime") String ime,
            @QueryParam("email") String email,
            @QueryParam("godiste") int godiste,
            @QueryParam("pol") String pol,
            @QueryParam("mesto") String mesto
    ) {
        try {
            System.out.println("createKorisnik.start");
            String patternString = "[abcdefghijklmnopqrstuvwxyz]+[@][abcdefghijklmnopqrstuvwxyz]+[.][abcdefghijklmnopqrstuvwxyz]+$";
            Pattern pattern = Pattern.compile(patternString);
            Matcher matcher = pattern.matcher(email);
            if (!matcher.find()) {
                return Response.ok("Neispravan format email-a").build();
            }
            if (!(pol.equals("Muski") || pol.equals("Zenski"))) {
                return Response.ok("Pol mora biti u formatu 'Muski' ili 'Zenski'").build();
            }

            String correlationId = UUID.randomUUID().toString();

            JMSContext context = connectionFactory.createContext();
            JMSConsumer responseConsumer = context.createConsumer(appToP1Topic, "JMSCorrelationID='" + correlationId + "'");
            JMSProducer producer = context.createProducer();

            String novoIme = ime.replace('-', ' ');

            TextMessage message = context.createTextMessage("kreirajKorisnika");
            message.setJMSCorrelationID(correlationId);
            message.setStringProperty("ime", novoIme);
            message.setStringProperty("email", email);
            message.setStringProperty("pol", pol);
            message.setStringProperty("mesto", mesto);
            message.setIntProperty("godiste", godiste);

            producer.send(podsistem1Topic, message);
            Message response = responseConsumer.receive();

            if (response == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            String txt = ((TextMessage) response).getText();
            return Response.ok(txt).build();
        } catch (JMSException ex) {
            Logger.getLogger(JavaEE8Resource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(409).build();
        }
    }

    @Path("email")
    @PUT
    public Response changeEmail(
            @QueryParam("stari") String stari,
            @QueryParam("novi") String novi
    ) {
        try {
            System.out.println("changeEmail.start");
            String patternString = "[abcdefghijklmnopqrstuvwxyz1234567890]+[@][abcdefghijklmnopqrstuvwxyz]+[.][abcdefghijklmnopqrstuvwxyz]+$";
            Pattern pattern = Pattern.compile(patternString);
            Matcher matcher = pattern.matcher(novi);
            if (!matcher.find()) {
                return Response.ok("Neispravan format email-a").build();
            }

            String correlationId = UUID.randomUUID().toString();
            JMSContext context = connectionFactory.createContext();
            JMSConsumer responseConsumer = context.createConsumer(appToP1Topic, "JMSCorrelationID='" + correlationId + "'");
            JMSProducer producer = context.createProducer();

            TextMessage message = context.createTextMessage("promeniEmail");
            message.setJMSCorrelationID(correlationId);
            message.setStringProperty("novi", novi);
            message.setStringProperty("stari", stari);

            producer.send(podsistem1Topic, message);
            Message response = responseConsumer.receive();

            if (response == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            String txt = ((TextMessage) response).getText();
            return Response.ok(txt).build();
        } catch (JMSException ex) {
            Logger.getLogger(JavaEE8Resource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(404).build();
        }
    }

    @Path("mesto/{email}")
    @PUT
    public Response changeMesto(
            @PathParam("email") String email,
            @QueryParam("mesto") String mesto
    ) {
        try {
            System.out.println("changeMesto.start");
            String correlationId = UUID.randomUUID().toString();
            JMSContext context = connectionFactory.createContext();
            JMSConsumer responseConsumer = context.createConsumer(appToP1Topic, "JMSCorrelationID='" + correlationId + "'");
            JMSProducer producer = context.createProducer();
            
            TextMessage message = context.createTextMessage("promeniMesto");
            message.setJMSCorrelationID(correlationId);
            message.setStringProperty("email", email);
            message.setStringProperty("mesto", mesto);
            
            producer.send(podsistem1Topic, message);
            Message response = responseConsumer.receive();
            
            if (response == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            String txt = ((TextMessage) response).getText();
            return Response.ok(txt).build();
        } catch (JMSException ex) {
            Logger.getLogger(JavaEE8Resource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(404).build();
        }
    }
}
