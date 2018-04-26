package istic.m2.ila.firefighterapp.clientRabbitMqGeneric;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
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
import java.lang.reflect.ParameterizedType;
import java.util.concurrent.TimeoutException;

import istic.m2.ila.firefighterapp.clientRabbitMQ.messages.DeclareDroneMessage;
import istic.m2.ila.firefighterapp.constantes.Endpoints;
import istic.m2.ila.firefighterapp.dto.DroneInfosDTO;

/**
 * Provide an Abstract class for RabbitMqService
 * @param <T> Type of the DTO
 * @param <E> Type of the Message
 */
public abstract class ServiceRabbitMQGeneric<T,E> extends Service {

    public static String TAG = "Service TEST => ";
    Connection _connection;

    protected Class<T> getGenericClass()
    {
        return ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
    }

    // This is the object that receives interactions from clients.
    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder
    {
        public ServiceRabbitMQGeneric getService()
        {
            return ServiceRabbitMQGeneric.this;
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
    public void onEvent(final E event) throws Exception
    {
        if (_connection == null)
            return;

        Channel channel = _connection.createChannel();
        channel.exchangeDeclare(Endpoints.RABBITMQ_EXCHANGE_NAME, "topic");

        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, Endpoints.RABBITMQ_EXCHANGE_NAME, getGenericClass().getSimpleName() + ".#");

        Consumer consumer = new DefaultConsumer(channel) {
            private String incomingMessageHandler = "";

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                incomingMessageHandler = new String(body, "UTF-8");
                //Log.i(TAG, "Received '" + envelope.getRoutingKey() + "':'" + incomingMessageHandler + "'");
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                try
                {
                    T typeInfoDTO = gson.fromJson(incomingMessageHandler, getGenericClass());
                    EventBus.getDefault().post(typeInfoDTO);
                }
                catch (JsonParseException e)
                {
                    Log.e(TAG, e.getMessage());
                }
            }
        };

        channel.basicConsume(queueName, true, consumer);
    }

    void start()
    {

    }
}