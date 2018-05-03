package istic.m2.ila.firefighterapp.rabbitMQ.clientRabbitMqGeneric;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
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
import java.lang.reflect.Type;
import java.util.Date;
import java.util.concurrent.TimeoutException;

import istic.m2.ila.firefighterapp.constantes.Endpoints;
import istic.m2.ila.firefighterapp.dto.IDTO;
import istic.m2.ila.firefighterapp.dto.IRabbitDTO;
import istic.m2.ila.firefighterapp.rabbitMQ.clientRabbitMqGeneric.messages.MessageGeneric;

import static istic.m2.ila.firefighterapp.constantes.Endpoints.RABBITMQ_ANDROID_DELETE;
import static istic.m2.ila.firefighterapp.constantes.Endpoints.RABBITMQ_ANDROID_UPDATE;

/**
 * Provide an Abstract class for RabbitMqService
 * @param <T> Type of the DTO
 */
public abstract class ServiceRabbitMQGeneric<T extends IRabbitDTO, M extends MessageGeneric<T>> extends Service {

    public String TAG = "Service "+ getGenericClass().getName() +" => ";
    private Connection _connection;

    private String queueName;

    protected Class<T> getGenericClass()
    {
        return ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
    }

    private <E extends Object> void initConsumer(final Class<E> t, final String routeType, final SyncAction action) throws IOException {
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

                builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        return new Date(json.getAsJsonPrimitive().getAsLong());
                    }
                });

                Gson gson = builder.create();
                try
                {
                    T messageClass = null;
                    E typeInfoDTO = gson.fromJson(incomingMessageHandler, t);

                    if(typeInfoDTO instanceof Long){
                        //delete
                        Class<T> clazz = (Class<T>) Class.forName(getGenericClass().getName());
                        messageClass = clazz.newInstance();
                        messageClass.setId((Long)typeInfoDTO);
                    } else {
                        //update
                        messageClass = (T)typeInfoDTO;
                    }

                    M message = MessageGeneric.getInstance(messageClass, action);
                    EventBus.getDefault().post(message);
                }
                catch (JsonParseException e)
                {
                    Log.e(TAG, e.getMessage());
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
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
        final ConnectionFactory _factory = new ConnectionFactory();

        _factory.setHost(Endpoints.RABBITMQ_SERVERADRESS);
        _factory.setUsername(Endpoints.RABBITMQ_USERNAME);
        _factory.setPassword(Endpoints.RABBITMQ_USERPASSWORD);
        _factory.setPort(Endpoints.RABBITMQ_SERVERPORT);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    _connection = _factory.newConnection();
                    initConsumer(getGenericClass(),RABBITMQ_ANDROID_UPDATE,SyncAction.UPDATE);
                    initConsumer(Long.class,RABBITMQ_ANDROID_DELETE,SyncAction.DELETE);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
        });

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
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    void start()
    {

    }
}