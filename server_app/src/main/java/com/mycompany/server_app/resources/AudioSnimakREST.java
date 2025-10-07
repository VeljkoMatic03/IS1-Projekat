/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.server_app.resources;

import java.util.Base64;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**
 *
 * @author HP
 */
@Path("audio")
public class AudioSnimakREST {

    @Resource(lookup = "projectConnFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "podsistem2Topic")
    private Topic podsistem2Topic;

    @Resource(lookup = "appToP2Topic")
    private Topic appToP2Topic;

    @GET
    public Response getSnimci() {
        try {
            System.out.println("getSnimci.start");
            String correlationId = UUID.randomUUID().toString();

            JMSContext context = connectionFactory.createContext();
            JMSConsumer responseConsumer = context.createConsumer(appToP2Topic, "JMSCorrelationID='" + correlationId + "'");
            JMSProducer producer = context.createProducer();

            TextMessage message = context.createTextMessage("dohvatiSnimke");
            message.setJMSCorrelationID(correlationId);

            producer.send(podsistem2Topic, message);
            Message response = responseConsumer.receive();

            if (response == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            String txt = ((TextMessage) response).getText();
            String header = response.getStringProperty("header");
            txt = header.concat(txt);
            return Response.ok(txt).build();
        } catch (JMSException ex) {
            Logger.getLogger(AudioSnimakREST.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(404).build();
        }
    }

    @Path("promeni-naziv")
    @PUT
    public Response promeniNaziv(
            @QueryParam("stariNaziv") String stari,
            @QueryParam("autor") String email,
            @QueryParam("noviNaziv") String novi
    ) {
        try {
            System.out.println("promeniNaziv.start");
            String correlationId = UUID.randomUUID().toString();

            JMSContext context = connectionFactory.createContext();
            JMSConsumer responseConsumer = context.createConsumer(appToP2Topic, "JMSCorrelationID='" + correlationId + "'");
            JMSProducer producer = context.createProducer();

            TextMessage message = context.createTextMessage("promeniNazivSnimka");
            message.setJMSCorrelationID(correlationId);
            message.setStringProperty("stari", stari);
            message.setStringProperty("novi", novi);
            message.setStringProperty("email", email);

            producer.send(podsistem2Topic, message);
            Message response = responseConsumer.receive();

            if (response == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            String txt = ((TextMessage) response).getText();
            return Response.ok(txt).build();
        } catch (JMSException ex) {
            Logger.getLogger(AudioSnimakREST.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(404).build();
        }
    }

    @Path("kategorije")
    @GET
    public Response getKategorijeSnimak(
            @QueryParam("naziv") String naziv,
            @QueryParam("email") String email
    ) {
        try {
            System.out.println("getKategorijeSnimak.start");
            String correlationId = UUID.randomUUID().toString();

            JMSContext context = connectionFactory.createContext();
            JMSConsumer responseConsumer = context.createConsumer(appToP2Topic, "JMSCorrelationID='" + correlationId + "'");
            JMSProducer producer = context.createProducer();

            TextMessage message = context.createTextMessage("dohvatiKategorijeSnimak");
            message.setJMSCorrelationID(correlationId);
            message.setStringProperty("naziv", naziv);
            message.setStringProperty("email", email);

            producer.send(podsistem2Topic, message);
            Message response = responseConsumer.receive();

            if (response == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            String txt = ((TextMessage) response).getText();
            String header = response.getStringProperty("header");
            txt = header.concat(txt);
            return Response.ok(txt).build();
        } catch (JMSException ex) {
            Logger.getLogger(AudioSnimakREST.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(404).build();
        }
    }

    @Path("create")
    @POST
    public Response createAudio(
            @QueryParam("naziv") String naziv,
            @QueryParam("email") String email,
            @QueryParam("datum") String datum,
            @QueryParam("trajanje") int trajanje
    ) {
        try {
            System.out.println("createAudio.start");
            String correlationId = UUID.randomUUID().toString();

            JMSContext context = connectionFactory.createContext();
            JMSConsumer responseConsumer = context.createConsumer(appToP2Topic, "JMSCorrelationID='" + correlationId + "'");
            JMSProducer producer = context.createProducer();

            TextMessage message = context.createTextMessage("kreirajSnimak");
            String noviDatum = datum.replace('|', ' ');
            message.setJMSCorrelationID(correlationId);
            message.setStringProperty("naziv", naziv);
            message.setStringProperty("email", email);
            message.setStringProperty("datum", noviDatum);
            message.setIntProperty("trajanje", trajanje);

            producer.send(podsistem2Topic, message);
            Message response = responseConsumer.receive();

            if (response == null) {
                return Response.ok("NEMA").build();
            }
            String txt = ((TextMessage) response).getText();
            return Response.ok(txt).build();
        } catch (JMSException ex) {
            Logger.getLogger(AudioSnimakREST.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(409).build();
        }
    }

    @Path("{naziv}")
    @DELETE
    public Response deleteAudioSnimak(
            @PathParam("naziv") String naziv,
            @QueryParam("autor") String email,
            @HeaderParam("Authorization") String headerAuth
    ) {
        try {
            System.out.println("deleteAudioSnimak.start");
            String correlationId = UUID.randomUUID().toString();

            JMSContext context = connectionFactory.createContext();
            JMSConsumer responseConsumer = context.createConsumer(appToP2Topic, "JMSCorrelationID='" + correlationId + "'");
            JMSProducer producer = context.createProducer();
            
            String decoded = new String(Base64.getDecoder().decode(headerAuth));
            if(!decoded.equals(email)) return Response.ok("Not authorized").build();

            TextMessage message = context.createTextMessage("obrisiSnimak");
            message.setJMSCorrelationID(correlationId);
            message.setStringProperty("naziv", naziv);
            message.setStringProperty("email", email);
            producer.send(podsistem2Topic, message);
            Message response = responseConsumer.receive();

            if (response == null) {
                return Response.ok("NEMA").build();
            }
            String txt = ((TextMessage) response).getText();
            return Response.ok(txt).build();
        } catch (JMSException ex) {
            Logger.getLogger(AudioSnimakREST.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(404).build();
        }

    }

    @Path("{naziv}")
    @PUT
    public Response addKategorija(
            @PathParam("naziv") String naziv,
            @QueryParam("autor") String email,
            @QueryParam("kategorija") String kategorija
    ) {
        try {
            System.out.println("addKategorija.start");
            String correlationId = UUID.randomUUID().toString();
            
            JMSContext context = connectionFactory.createContext();
            JMSConsumer responseConsumer = context.createConsumer(appToP2Topic, "JMSCorrelationID='" + correlationId + "'");
            JMSProducer producer = context.createProducer();
            
            TextMessage message = context.createTextMessage("dodajKategorijuSnimku");
            message.setJMSCorrelationID(correlationId);
            message.setStringProperty("kategorija", kategorija);
            message.setStringProperty("snimak", naziv);
            message.setStringProperty("email", email);
            producer.send(podsistem2Topic, message);
            Message response = responseConsumer.receive();
            
            if (response == null) {
                return Response.ok("NEMA").build();
            }
            String txt = ((TextMessage) response).getText();
            return Response.ok(txt).build();
        } catch (JMSException ex) {
            Logger.getLogger(AudioSnimakREST.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(404).build();
        }
    }
}
