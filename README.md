IS1 Projekat

This is a repo containing my student project for Information Systems 1 class at School of Electrical Engineering at University of Belgrade.

Project is made up of 3 Java Enterprise apps (subsystems) that represent each of 3 databases. Each subsystem is connected to the main server by JMS topics, using Glassfish 5 server.

Server is built as a Java Web App, using Glassfish 5 as server. Server contains endpoints which can be called using REST API. Client communicates with server using HTTP protocol. 
Server then communicates with subsystems using JMS, and subsystems make changes in databases.

I used JPA for establishing connection to my mySql databases. 
