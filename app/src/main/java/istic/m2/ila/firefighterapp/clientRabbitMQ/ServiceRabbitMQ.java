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

import istic.m2.ila.firefighterapp.clientRabbitMQ.messages.NewDroneMessage;
import istic.m2.ila.firefighterapp.clientRabbitMQ.messages.UpdateInfosDroneMessage;
import istic.m2.ila.firefighterapp.constantes.Endpoints;
import istic.m2.ila.firefighterapp.dto.DroneInfosDTO;


public class ServiceRabbitMQ extends Service {

    public static String TAG = "Service RABBITMQ => ";
    private ConnectionFactory _factory;
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
        _factory = new ConnectionFactory();

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


    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEvent(final NewDroneMessage event) throws Exception
    {
        if (_connection == null)
            return;

        Channel channel = _connection.createChannel();
        channel.exchangeDeclare(Endpoints.RABBITMQ_EXCHANGE_NAME, "topic");

        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, Endpoints.RABBITMQ_EXCHANGE_NAME, "drone.info." + event.getDroneId());

        new DefaultConsumer(channel) {
            private String incomingMessageHandler = "";

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                incomingMessageHandler = new String(body, "UTF-8");
                Log.i(TAG, "Received '" + envelope.getRoutingKey() + "':'" + incomingMessageHandler + "'");
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                DroneInfosDTO droneInfosDTO = gson.fromJson(incomingMessageHandler, DroneInfosDTO.class);
                EventBus.getDefault().post(
                        new UpdateInfosDroneMessage(droneInfosDTO.id_drone, droneInfosDTO.position.longitude,
                                droneInfosDTO.position.latitude, droneInfosDTO.battery_level, droneInfosDTO.orientation.yaw));
            }
        };
    }

    void start()
    {

    }
}