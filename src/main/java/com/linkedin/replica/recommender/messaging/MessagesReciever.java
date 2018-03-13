package com.linkedin.replica.recommender.messaging;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.linkedin.replica.recommender.utils.Configuration;
import com.rabbitmq.client.*;
import com.linkedin.replica.recommender.services.RecommendationService;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

public class MessagesReciever {
    private Configuration configReader = Configuration.getInstance();
    private RecommendationService recommenderService = new RecommendationService();
    private final String QUEUE_NAME = configReader.getAppConfig("rabbitmq.queue");
    private final String RABBIT_MQ_IP = configReader.getAppConfig("rabbitmq.ip");;

    private ConnectionFactory factory;
    private Channel channel;
    private Connection connection;

    public MessagesReciever() throws IOException, TimeoutException {
        factory = new ConnectionFactory();
        factory.setHost(RABBIT_MQ_IP);
        connection = factory.newConnection();
        channel = connection.createChannel();
        // declare the queue if it does not exist
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        System.out.println("Started recommendation receiver successfully.");

        // Create the consumer (listener) for the new messages
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {

                // extract notification info from body and send it
                JsonObject object = new JsonParser().parse(new String(body)).getAsJsonObject();
                String userId = object.get("userId").getAsString();
                String commandName = object.get("commandName").getAsString();
                HashMap<String, String> args = new HashMap<>();
                args.put("userId", userId);
                try {
                    recommenderService.serve(commandName, args);
                } catch (Exception e) {
                    // TODO write error to a log
                }
            }
        };

        // attach the consumer
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }

    public void closeConnection() throws IOException, TimeoutException {
        channel.close();
        connection.close();
    }
}