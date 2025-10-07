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
@Path("paket")
public class PaketREST {

    @Resource(lookup = "projectConnFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "podsistem3Topic")
    private Topic podsistem3Topic;

    @Resource(lookup = "appToP3Topic")
    private Topic appToP3Topic;

    /*@Path("echo")
    @GET
    public Response echo() {
        try {
            System.out.println("echo.start");
            String correlationId = UUID.randomUUID().toString();
            
            JMSContext context = connectionFactory.createContext();
            JMSConsumer responseConsumer = context.createConsumer(appToP3Topic, "JMSCorrelationID='" + correlationId + "'");
            JMSProducer producer = context.createProducer();
            
            TextMessage message = context.createTextMessage("echo from REST");
            message.setJMSCorrelationID(correlationId);
            
            producer.send(podsistem3Topic, message);
            //System.out.println("L1");
            Message response = responseConsumer.receive();
            //System.out.println("L2");
            
            if (response == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            String txt = ((TextMessage) response).getText();
            return Response.ok(txt).build();
        } catch (JMSException ex) {
            Logger.getLogger(PaketREST.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(404).build();
        }
    }*/
    @Path("{naziv}")
    @POST
    public Response createPaket(
            @PathParam("naziv") String naziv,
            @QueryParam("cena") int cena
    ) {
        try {
            System.out.println("createPaket.start");
            String correlationId = UUID.randomUUID().toString();

            JMSContext context = connectionFactory.createContext();
            JMSConsumer responseConsumer = context.createConsumer(appToP3Topic, "JMSCorrelationID='" + correlationId + "'");
            JMSProducer producer = context.createProducer();

            TextMessage message = context.createTextMessage("kreirajPaket");
            message.setJMSCorrelationID(correlationId);
            message.setStringProperty("naziv", naziv);
            message.setIntProperty("cena", cena);

            producer.send(podsistem3Topic, message);
            Message response = responseConsumer.receive();

            if (response == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            String txt = ((TextMessage) response).getText();
            return Response.ok(txt).build();
        } catch (JMSException ex) {
            Logger.getLogger(PaketREST.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(409).build();
        }
    }

    @Path("{naziv}")
    @PUT
    public Response updateCenaPaket(
            @PathParam("naziv") String naziv,
            @QueryParam("novaCena") int cena
    ) {
        try {
            System.out.println("updateCenaPaket.start");
            String correlationId = UUID.randomUUID().toString();

            JMSContext context = connectionFactory.createContext();
            JMSConsumer responseConsumer = context.createConsumer(appToP3Topic, "JMSCorrelationID='" + correlationId + "'");
            JMSProducer producer = context.createProducer();

            TextMessage message = context.createTextMessage("promeniCenuPaket");
            message.setJMSCorrelationID(correlationId);
            message.setStringProperty("naziv", naziv);
            message.setIntProperty("cena", cena);

            producer.send(podsistem3Topic, message);
            Message response = responseConsumer.receive();

            if (response == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            String txt = ((TextMessage) response).getText();
            return Response.ok(txt).build();
        } catch (JMSException ex) {
            Logger.getLogger(PaketREST.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(404).build();
        }
    }

    @GET
    public Response getPaketi() {
        try {
            System.out.println("getPaketi.start");
            String correlationId = UUID.randomUUID().toString();
            
            JMSContext context = connectionFactory.createContext();
            JMSConsumer responseConsumer = context.createConsumer(appToP3Topic, "JMSCorrelationID='" + correlationId + "'");
            JMSProducer producer = context.createProducer();
            
            TextMessage message = context.createTextMessage("dohvatiPakete");
            message.setJMSCorrelationID(correlationId);
            
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
