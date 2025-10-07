/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.server_app.resources;

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
@Path("ocena")
public class OcenaREST {

    @Resource(lookup = "projectConnFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "podsistem3Topic")
    private Topic podsistem3Topic;

    @Resource(lookup = "appToP3Topic")
    private Topic appToP3Topic;

    @Path("{naziv}")
    @POST
    public Response createOcena(
            @PathParam("naziv") String naziv,
            @QueryParam("autor") String emailAutor,
            @QueryParam("slusalac") String emailSlusalac,
            @QueryParam("ocena") int ocena
    ) {
        try {
            System.out.println("createOcena.start");
            String correlationId = UUID.randomUUID().toString();

            JMSContext context = connectionFactory.createContext();
            JMSConsumer responseConsumer = context.createConsumer(appToP3Topic, "JMSCorrelationID='" + correlationId + "'");
            JMSProducer producer = context.createProducer();

            TextMessage message = context.createTextMessage("kreirajOcenu");
            message.setJMSCorrelationID(correlationId);
            message.setStringProperty("naziv", naziv);
            message.setStringProperty("autor", emailAutor);
            message.setStringProperty("slusalac", emailSlusalac);
            message.setIntProperty("ocena", ocena);

            producer.send(podsistem3Topic, message);
            Message response = responseConsumer.receive();

            if (response == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            String txt = ((TextMessage) response).getText();
            return Response.ok(txt).build();
        } catch (JMSException ex) {
            Logger.getLogger(OcenaREST.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(409).build();
        }
    }

    @Path("update/{naziv}")
    @PUT
    public Response updateOcena(
            @PathParam("naziv") String naziv,
            @QueryParam("autor") String emailAutor,
            @QueryParam("slusalac") String emailSlusalac,
            @QueryParam("ocena") int ocena
    ) {
        try {
            System.out.println("updateOcena.start");
            String correlationId = UUID.randomUUID().toString();

            JMSContext context = connectionFactory.createContext();
            JMSConsumer responseConsumer = context.createConsumer(appToP3Topic, "JMSCorrelationID='" + correlationId + "'");
            JMSProducer producer = context.createProducer();

            TextMessage message = context.createTextMessage("promeniOcenu");
            message.setJMSCorrelationID(correlationId);
            message.setStringProperty("naziv", naziv);
            message.setStringProperty("autor", emailAutor);
            message.setStringProperty("slusalac", emailSlusalac);
            message.setIntProperty("ocena", ocena);

            producer.send(podsistem3Topic, message);
            Message response = responseConsumer.receive();

            if (response == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            String txt = ((TextMessage) response).getText();
            return Response.ok(txt).build();
        } catch (JMSException ex) {
            Logger.getLogger(OcenaREST.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(404).build();
        }
    }

    @Path("delete/{naziv}")
    @DELETE
    public Response deleteOcena(
            @PathParam("naziv") String naziv,
            @QueryParam("autor") String emailAutor,
            @QueryParam("slusalac") String emailSlusalac
    ) {
        try {
            System.out.println("deleteOcena.start");
            String correlationId = UUID.randomUUID().toString();

            JMSContext context = connectionFactory.createContext();
            JMSConsumer responseConsumer = context.createConsumer(appToP3Topic, "JMSCorrelationID='" + correlationId + "'");
            JMSProducer producer = context.createProducer();

            TextMessage message = context.createTextMessage("obrisiOcenu");
            message.setJMSCorrelationID(correlationId);
            message.setStringProperty("naziv", naziv);
            message.setStringProperty("autor", emailAutor);
            message.setStringProperty("slusalac", emailSlusalac);

            producer.send(podsistem3Topic, message);
            Message response = responseConsumer.receive();

            if (response == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            String txt = ((TextMessage) response).getText();
            return Response.ok(txt).build();
        } catch (JMSException ex) {
            Logger.getLogger(OcenaREST.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(404).build();
        }
    }
    
    @GET
    public Response getOcene(
            @QueryParam("naziv") String naziv,
            @QueryParam("autor") String email
    ) {
        try {
            System.out.println("getOcene.start");
            String correlationId = UUID.randomUUID().toString();
            
            JMSContext context = connectionFactory.createContext();
            JMSConsumer responseConsumer = context.createConsumer(appToP3Topic, "JMSCorrelationID='" + correlationId + "'");
            JMSProducer producer = context.createProducer();
            
            TextMessage message = context.createTextMessage("dohvatiOcene");
            message.setJMSCorrelationID(correlationId);
            message.setStringProperty("email", email);
            message.setStringProperty("naziv", naziv);
            
            producer.send(podsistem3Topic, message);
            Message response = responseConsumer.receive();
            
            if (response == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            String txt = ((TextMessage) response).getText();
            String header = response.getStringProperty("header");
            txt = header.concat(txt);
            return Response.ok(txt).build();
        } catch (JMSException ex) {
            Logger.getLogger(PaketREST.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(404).build();
        }
    }
}
