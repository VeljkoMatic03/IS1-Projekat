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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**
 *
 * @author HP
 */
@Path("slusanje")
public class SlusanjeREST {
    @Resource(lookup = "projectConnFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "podsistem3Topic")
    private Topic podsistem3Topic;

    @Resource(lookup = "appToP3Topic")
    private Topic appToP3Topic;
    
    @Path("{slusalac}")
    @POST
    public Response createSlusanje(
            @PathParam("slusalac") String slusalac,
            @QueryParam("naziv") String naziv,
            @QueryParam("autor") String autor,
            @QueryParam("vremeSlusanja") int vremeSlusanja,
            @QueryParam("pocetak") int pocetnaSekunda
    ) {
        try {
            System.out.println("createPretplata.start");
            String correlationId = UUID.randomUUID().toString();

            JMSContext context = connectionFactory.createContext();
            JMSConsumer responseConsumer = context.createConsumer(appToP3Topic, "JMSCorrelationID='" + correlationId + "'");
            JMSProducer producer = context.createProducer();
            
            TextMessage message = context.createTextMessage("kreirajSlusanje");
            message.setJMSCorrelationID(correlationId);
            message.setStringProperty("naziv", naziv);
            message.setIntProperty("vreme", vremeSlusanja);
            message.setIntProperty("pocetak", pocetnaSekunda);
            message.setStringProperty("autor", autor);
            message.setStringProperty("slusalac", slusalac);

            producer.send(podsistem3Topic, message);
            Message response = responseConsumer.receive();

            if (response == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            String txt = ((TextMessage) response).getText();
            return Response.ok(txt).build();
        } catch (JMSException ex) {
            Logger.getLogger(SlusanjeREST.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(409).build();
        }
    }
    
    @GET
    public Response getSlusanja(
            @QueryParam("naziv") String naziv,
            @QueryParam("autor") String email
    ) {
        try {
            System.out.println("getSlusanja.start");
            String correlationId = UUID.randomUUID().toString();
            
            JMSContext context = connectionFactory.createContext();
            JMSConsumer responseConsumer = context.createConsumer(appToP3Topic, "JMSCorrelationID='" + correlationId + "'");
            JMSProducer producer = context.createProducer();
            
            TextMessage message = context.createTextMessage("dohvatiSlusanja");
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
