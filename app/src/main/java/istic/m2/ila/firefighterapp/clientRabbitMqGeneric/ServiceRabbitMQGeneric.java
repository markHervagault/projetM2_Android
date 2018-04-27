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

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.concurrent.TimeoutException;

import istic.m2.ila.firefighterapp.constantes.Endpoints;

import static istic.m2.ila.firefighterapp.constantes.Endpoints.RABBITMQ_ANDROID_DELETE;
import static istic.m2.ila.firefighterapp.constantes.Endpoints.RABBITMQ_ANDROID_UPDATE;

/**
 * Provide an Abstract class for RabbitMqService
 * @param <T> Type of the DTO
 */
public abstract class ServiceRabbitMQGeneric<T> extends Service {

    public String TAG = "Service "+ getGenericClass().getName() +" => ";
    private Connection _connection;

    private String queueName;

    protected Class<T> getGenericClass()
    {
        return ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
    }

    private void initConsumer(String routeType, final SyncAction action) throws IOException {
        Channel channel = _connection.createChannel();
        channel.exchangeDeclare(Endpoints.RABBITMQ_EXCHANGE_NAME, "topic");

        queueName = channel.queueDeclare().getQueue();

        String updateRouteKey = routeType + getGenericClass().getSimpleName() + ".#";
        channel.queueBind(queueName, Endpoints.RABBITMQ_EXCHANGE_NAME, updateRouteKey);

        Log.i(TAG,"Bind to queue : " + routeType + getGenericClass().getSimpleName());

        Consumer consumer = new DefaultConsumer(channel) {
            private String incomingMessageHandler = "";

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                incomingMessageHandler = new String(body, "UTF-8");
                Log.i(TAG, "Received '" + envelope.getRoutingKey() + "':'" + incomingMessageHandler + "'");
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                try
                {
                    T typeInfoDTO = gson.fromJson(incomingMessageHandler, getGenericClass());

                    MessageGeneric<T> message = new MessageGeneric<>(typeInfoDTO, action);
                    EventBus.getDefault().post(message);
                }
                catch (JsonParseException e)
                {
                    Log.e(TAG, e.getMessage());
                }
            }
        };

        channel.basicConsume(queueName, true, consumer);
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
        ConnectionFactory _factory = new ConnectionFactory();

        _factory.setHost(Endpoints.RABBITMQ_SERVERADRESS);
        _factory.setUsername(Endpoints.RABBITMQ_USERNAME);
        _factory.setPassword(Endpoints.RABBITMQ_USERPASSWORD);
        _factory.setPort(Endpoints.RABBITMQ_SERVERPORT);

        try {
            _connection = _factory.newConnection();
            initConsumer(RABBITMQ_ANDROID_UPDATE,SyncAction.UPDATE);
            initConsumer(RABBITMQ_ANDROID_DELETE,SyncAction.DELETE);
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

    void start()
    {

    }
}