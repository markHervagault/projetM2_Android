package istic.m2.ila.firefighterapp.clientRabbitMQ;

import android.os.StrictMode;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import istic.m2.ila.firefighterapp.clientRabbitMQ.messages.MessageBus;
import istic.m2.ila.firefighterapp.clientRabbitMQ.messages.NewDroneMessage;
import istic.m2.ila.firefighterapp.clientRabbitMQ.messages.UpdateInfosDroneMessage;
import istic.m2.ila.firefighterapp.constantes.Endpoints;
import istic.m2.ila.firefighterapp.dto.DroneDTO;
import istic.m2.ila.firefighterapp.dto.DroneInfosDTO;

/**
 * Created by markh on 27/03/2018.
 */

public class RabbitMQThread extends Thread {

    private final String TAG = "RabbitMQThread => ";
    String incomingMessageHandler="";

    ConnectionFactory factory;
    Connection connection;

    private MessageBus messageBus;

    public RabbitMQThread(MessageBus messageBus)  {
        this.messageBus = messageBus;

        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        factory = new ConnectionFactory();

        setupConnectionFactory();

        try {
            connection = factory.newConnection();
        } catch (IOException e) {
            Log.e(TAG, "Error entrées/sorties");
            e.printStackTrace();
        } catch (TimeoutException e) {
            Log.e(TAG, "Error temps écoulé");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            Channel channel = connection.createChannel();
            channel.basicQos(1);

            if(messageBus instanceof NewDroneMessage) {
                NewDroneMessage newDroneMessage = (NewDroneMessage) messageBus;
                Log.d(TAG, "======================================================= Traitement d'un nouveau drone : ajout d'un abonnement queue à rabbitMQ ");
                String queue = newDroneMessage.getDroneId() + "_info";
                channel.queueDeclare(queue, false, false, false, null);

                Consumer consumer = new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope,
                                               AMQP.BasicProperties properties, byte[] body) throws IOException {
                        incomingMessageHandler = new String(body, "UTF-8");
                        Log.d(TAG, " [x] Received '" + envelope.getRoutingKey() + "':'" + incomingMessageHandler + "'");
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        DroneInfosDTO droneInfosDTO = gson.fromJson( incomingMessageHandler, DroneInfosDTO.class);
                        EventBus.getDefault().post(
                                new UpdateInfosDroneMessage(droneInfosDTO.id_drone, droneInfosDTO.position.longitude,
                                        droneInfosDTO.position.latitude, droneInfosDTO.battery_level, droneInfosDTO.orientation.yaw));
                    }
                };

                channel.basicConsume(queue, true, consumer);
            }



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupConnectionFactory() {
        //final URI uri = URI.create("amqp://guest:guest@nwanono.info:5672/");
        final URI uri = URI.create(Endpoints.RABBITMQ);
        try {
            factory.setAutomaticRecoveryEnabled(false);
            factory.setUri(uri);
            factory.setVirtualHost("/");
        } catch (KeyManagementException | NoSuchAlgorithmException | URISyntaxException e1) {
            e1.printStackTrace();
        }
    }

}
