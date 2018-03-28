package istic.m2.ila.firefighterapp.clientRabbitMQ;

import android.os.StrictMode;
import android.util.Log;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import istic.m2.ila.firefighterapp.dto.DroneDTO;

/**
 * Created by markh on 27/03/2018.
 */

public class RabbitMQThread extends Thread {

    List<DroneDTO> drones = new ArrayList<DroneDTO>();

    private final String TAG = "RabbitMQThread => ";
    String incomingMessageHandler="";

    ConnectionFactory factory;
    Connection connection;

    public RabbitMQThread()  {

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
        while(true) {
            try {
                Channel channel = connection.createChannel();
                channel.basicQos(1);

                channel.queueDeclare("0_info", false, false, false, null);

                Consumer consumer = new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope,
                                               AMQP.BasicProperties properties, byte[] body) throws IOException {
                        incomingMessageHandler = new String(body, "UTF-8");
                        Log.d(TAG, " [x] Received '" + envelope.getRoutingKey() + "':'" + incomingMessageHandler + "'");
                    }
                };

                channel.basicConsume("0_info", true, consumer);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setupConnectionFactory() {
        final URI uri = URI.create("amqp://guest:guest@nwanono.info:5672/");
        //final URI uri = URI.create("amqp://guest:guest@148.60.11.57:6005/");
        try {
            factory.setAutomaticRecoveryEnabled(false);
            factory.setUri(uri);
            factory.setVirtualHost("/");
        } catch (KeyManagementException | NoSuchAlgorithmException | URISyntaxException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Ajoute un listener sur la queue correspondant au drone sur RabbitMQ
     * @param drone
     *      drone à suivre
     */
    public void addSubscriptionForDrone(DroneDTO drone){

    }

}
