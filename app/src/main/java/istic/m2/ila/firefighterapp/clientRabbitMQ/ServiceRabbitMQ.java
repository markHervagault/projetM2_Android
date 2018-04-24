package istic.m2.ila.firefighterapp.clientRabbitMQ;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

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
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import istic.m2.ila.firefighterapp.clientRabbitMQ.messages.DeclareDroneMessage;
import istic.m2.ila.firefighterapp.clientRabbitMQ.messages.PauseMissionMessage;
import istic.m2.ila.firefighterapp.clientRabbitMQ.messages.PlayMissionMessage;
import istic.m2.ila.firefighterapp.clientRabbitMQ.messages.StopMissionMessage;
import istic.m2.ila.firefighterapp.constantes.Endpoints;
import istic.m2.ila.firefighterapp.dto.DroneInfosDTO;


public class ServiceRabbitMQ extends Service {

    public static String TAG = "Service RABBITMQ => ";
    Connection _connection;

    // This is the object that receives interactions from clients.
    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder
    {
        public ServiceRabbitMQ getService()
        {
            return ServiceRabbitMQ.this;
        }
    }
    /** Called when the service is being created. */
    @Override
    public void onCreate()
    {
        EventBus.getDefault().register(this);
        ConnectionFactory _factory = new ConnectionFactory();

        _factory.setHost(Endpoints.RABBITMQ_SERVERADRESS);
        _factory.setUsername(Endpoints.RABBITMQ_USERNAME);
        _factory.setPassword(Endpoints.RABBITMQ_USERPASSWORD);
        _factory.setPort(Endpoints.RABBITMQ_SERVERPORT);

        try {
            _connection = _factory.newConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    //SUBSCRIBING
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEvent(final DeclareDroneMessage event) throws Exception
    {
        if (_connection == null)
            return;

        Channel channel = _connection.createChannel();
        channel.exchangeDeclare(Endpoints.RABBITMQ_EXCHANGE_NAME, Endpoints.RABBITMQ_EXCHANGE_TYPE);

        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, Endpoints.RABBITMQ_EXCHANGE_NAME, "drone.info." + event.getDroneDTO().getId());

        Consumer consumer = new DefaultConsumer(channel) {
            private String incomingMessageHandler = "";

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                incomingMessageHandler = new String(body, "UTF-8");
                Log.i(TAG, "Received DroneInfoDTO'");
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                DroneInfosDTO droneInfos = gson.fromJson(incomingMessageHandler, DroneInfosDTO.class);
                EventBus.getDefault().post(droneInfos);
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }

    ///////////////////////// fonctions d'envoi de commandes au drone ///////////////////////////

    /**
     * Envoie une commande STOP au drone dont l'id est indiqué dans StopMissionMessage
     * @param event
     *      Le message bus contenant  l'id du drone
     * @throws Exception
     */
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEvent(StopMissionMessage event) throws Exception
    {

        if (_connection == null)
            return;

        Channel channel = _connection.createChannel();

        channel.exchangeDeclare(Endpoints.RABBITMQ_EXCHANGE_NAME, Endpoints.RABBITMQ_EXCHANGE_TYPE);

        String message = "stop";
        channel.basicPublish(Endpoints.RABBITMQ_EXCHANGE_NAME, "drone.command."+event.getDroneId(), null, message.getBytes());
        System.out.println(" [x] Sent '" + "drone.command."+event.getDroneId() + "':'" + message + "'");
    }

    /**
     * Envoie une commande PLAY au drone dont l'id est indiqué dans PlayMissionMessage
     * @param event
     *      Le message bus contenant  l'id du drone
     * @throws Exception
     */
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEvent(PlayMissionMessage event) throws Exception
    {

        if (_connection == null)
            return;

        Channel channel = _connection.createChannel();

        channel.exchangeDeclare(Endpoints.RABBITMQ_EXCHANGE_NAME, Endpoints.RABBITMQ_EXCHANGE_TYPE);

        String message = "play";
        channel.basicPublish(Endpoints.RABBITMQ_EXCHANGE_NAME, "drone.command."+event.getDroneId(), null, message.getBytes());
        System.out.println(" [x] Sent '" + "drone.command."+event.getDroneId() + "':'" + message + "'");
    }

    /**
     * Envoie une commande PAUSE au drone dont l'id est indiqué dans PauseMissionMessage
     * @param event
     *      Le message bus contenant  l'id du drone
     * @throws Exception
     */
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEvent(PauseMissionMessage event) throws Exception
    {

        if (_connection == null)
            return;

        Channel channel = _connection.createChannel();

        channel.exchangeDeclare(Endpoints.RABBITMQ_EXCHANGE_NAME, Endpoints.RABBITMQ_EXCHANGE_TYPE);

        String message = "pause";
        channel.basicPublish(Endpoints.RABBITMQ_EXCHANGE_NAME, "drone.command."+event.getDroneId(), null, message.getBytes());
        System.out.println(" [x] Sent '" + "drone.command."+event.getDroneId() + "':'" + message + "'");
    }

    void start()
    {

    }
}