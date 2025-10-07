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
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 *
 * @author HP
 */
@Path("mesto")
public class MestoREST {

    @Resource(lookup = "projectConnFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "appToP1Topic")
    private Topic appToP1Topic;

    @Resource(lookup = "podsistem1Topic")
    private Topic podsistem1Topic;

    @GET
    public Response getMesta() {
        try {
            System.out.println("getMesta.start");
            String correlationId = UUID.randomUUID().toString();

            JMSContext context = connectionFactory.createContext();
            JMSConsumer responseConsumer = context.createConsumer(appToP1Topic, "JMSCorrelationID='" + correlationId + "'");
            JMSProducer producer = context.createProducer();

            TextMessage message = context.createTextMessage("dohvatiMesta");
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
            Logger.getLogger(MestoREST.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(404).build();
        }
    }

    @Path("{naziv}")
    @POST
    public Response createMesto(
            @PathParam("naziv") String naziv
    ) {
        try {
            System.out.println("createMesto.start");
            String correlationId = UUID.randomUUID().toString();
            
            JMSContext context = connectionFactory.createContext();
            JMSConsumer responseConsumer = context.createConsumer(appToP1Topic, "JMSCorrelationID='" + correlationId + "'");
            JMSProducer producer = context.createProducer();
            
            TextMessage message = context.createTextMessage("kreirajMesto");
            message.setJMSCorrelationID(correlationId);
            message.setStringProperty("naziv", naziv);
            
            producer.send(podsistem1Topic, message);
            Message response = responseConsumer.receive();
            
            if (response == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            String txt = ((TextMessage) response).getText();
            return Response.ok(txt).build();
        } catch (JMSException ex) {
            Logger.getLogger(MestoREST.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(409).build();
        }
    }
}
