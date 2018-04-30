package istic.m2.ila.firefighterapp.rabbitMQ;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
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
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeoutException;

import istic.m2.ila.firefighterapp.eventbus.drone.DeclareDroneMessage;
import istic.m2.ila.firefighterapp.eventbus.drone.PauseMissionMessage;
import istic.m2.ila.firefighterapp.eventbus.drone.PlayMissionMessage;
import istic.m2.ila.firefighterapp.eventbus.drone.SelectedDroneChangedMessage;
import istic.m2.ila.firefighterapp.eventbus.drone.StopMissionMessage;
import istic.m2.ila.firefighterapp.constantes.Endpoints;
import istic.m2.ila.firefighterapp.dto.DroneInfosDTO;
import istic.m2.ila.firefighterapp.dto.MissionDTO;


public class RabbitMQDroneService extends Service {

    public static String TAG = "Service RABBITMQ => ";
    Connection _connection;

    // This is the object that receives interactions from clients.
    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder
    {
        public RabbitMQDroneService getService()
        {
            return RabbitMQDroneService.this;
        }
    }

    //region Init

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
    public IBinder onBind(Intent intent)
    {
        Log.i(TAG, "RabbitMQ - On Bind");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent)
    {
        Log.i(TAG, "RabbitMQ - On UnBind");
        return super.onUnbind(intent);
    }

    //endregion

    //region Basic Consumers

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void UpdateMissionDTO(SelectedDroneChangedMessage message) throws Exception
    {
        if(_connection == null)
        {
            Log.e(TAG, "UpdateMissionDTO : Connection null");
            return;
        }

        Channel channel = _connection.createChannel();
        channel.exchangeDeclare(Endpoints.RABBITMQ_EXCHANGE_NAME, Endpoints.RABBITMQ_EXCHANGE_TYPE);

        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, Endpoints.RABBITMQ_EXCHANGE_NAME, Endpoints.RABBITMQ_ALLMISSION_DTO);

        Consumer consumer = new DefaultConsumer(channel)
        {
            private String incomingMessageHandler = "";

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException
            {
                incomingMessageHandler = new String(body, "UTF-8");
                Log.i(TAG, "Received MissionDTO");
                GsonBuilder builder = new GsonBuilder()
                        .registerTypeAdapter(Date.class, new DateDeserializer())
                        .registerTypeAdapter(Date.class, new DateSerializer());
                Gson gson = builder.create();
                try
                {
                    MissionDTO missionDTO = gson.fromJson(incomingMessageHandler, MissionDTO.class);
                    EventBus.getDefault().post(missionDTO);
                }
                catch (JsonParseException e)
                {
                    Log.e(TAG, e.getMessage());
                }
            }
        };

        channel.basicConsume(queueName, true, consumer);
    }

    //endregion

    //region Event Bus

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onDeclareDroneEvent(final DeclareDroneMessage event) throws Exception
    {
        if (_connection == null) {
            Log.e(TAG, "DeclareDroneEvent : Connection null");
            return;
        }

        Channel channel = _connection.createChannel();
        channel.exchangeDeclare(Endpoints.RABBITMQ_EXCHANGE_NAME, Endpoints.RABBITMQ_EXCHANGE_TYPE);

        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, Endpoints.RABBITMQ_EXCHANGE_NAME, Endpoints.RABBITMQ_DRONE_INFO + event.getDroneDTO().getId());

        Consumer consumer = new DefaultConsumer(channel)
        {
            private String incomingMessageHandler = "";

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException
            {
                incomingMessageHandler = new String(body, "UTF-8");
                //Log.i(TAG, "Received DroneInfoDTO'");
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                try
                {
                    DroneInfosDTO droneInfos = gson.fromJson(incomingMessageHandler, DroneInfosDTO.class);
                    EventBus.getDefault().post(droneInfos);
                }
                catch (JsonParseException e)
                {
                    Log.e(TAG, e.getMessage());
                }
            }
        };

        channel.basicConsume(queueName, true, consumer);
    }

    //endregion

    //region Drone Commands

    /**
     * Envoie une commande STOP au drone dont l'id est indiqué dans StopMissionMessage
     * @param event
     *      Le message bus contenant  l'id du drone
     * @throws Exception
     */
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void OnStopCommandEvent(StopMissionMessage event) throws Exception
    {
        if (_connection == null)
            return;

        Channel channel = _connection.createChannel();

        channel.exchangeDeclare(Endpoints.RABBITMQ_EXCHANGE_NAME, Endpoints.RABBITMQ_EXCHANGE_TYPE);

        String message = "stop";
        channel.basicPublish(Endpoints.RABBITMQ_EXCHANGE_NAME, Endpoints.RABBITMQ_DRONE_COMMAND + event.getDroneId(), null, message.getBytes());
        System.out.println(" [x] Sent '" + "drone.command."+event.getDroneId() + "':'" + message + "'");
    }

    /**
     * Envoie une commande PLAY au drone dont l'id est indiqué dans PlayMissionMessage
     * @param event
     *      Le message bus contenant  l'id du drone
     * @throws Exception
     */
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void OnPlayCommandEvent(PlayMissionMessage event) throws Exception
    {
        if (_connection == null)
            return;

        Channel channel = _connection.createChannel();

        channel.exchangeDeclare(Endpoints.RABBITMQ_EXCHANGE_NAME, Endpoints.RABBITMQ_EXCHANGE_TYPE);

        String message = "play";
        channel.basicPublish(Endpoints.RABBITMQ_EXCHANGE_NAME, Endpoints.RABBITMQ_DRONE_COMMAND+event.getDroneId(), null, message.getBytes());
        System.out.println(" [x] Sent '" + "drone.command."+event.getDroneId() + "':'" + message + "'");
    }

    /**
     * Envoie une commande PAUSE au drone dont l'id est indiqué dans PauseMissionMessage
     * @param event
     *      Le message bus contenant  l'id du drone
     * @throws Exception
     */
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void OnPauseCommandEvent(PauseMissionMessage event) throws Exception
    {
        if (_connection == null)
            return;

        Channel channel = _connection.createChannel();

        channel.exchangeDeclare(Endpoints.RABBITMQ_EXCHANGE_NAME, Endpoints.RABBITMQ_EXCHANGE_TYPE);

        String message = "pause";
        channel.basicPublish(Endpoints.RABBITMQ_EXCHANGE_NAME, Endpoints.RABBITMQ_DRONE_COMMAND+event.getDroneId(), null, message.getBytes());
        System.out.println(" [x] Sent '" + "drone.command."+event.getDroneId() + "':'" + message + "'");
    }

    //endregion

    //region GSON Date

    //TODO: Centraliser un GSON builder
    class DateDeserializer implements JsonDeserializer {

        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
        {
            SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            if(json == null){
                return null;
            } else {
                try {
                    Date date = dateParser.parse(json.getAsString().substring(0,19));
                    return date;
                } catch (ParseException e) {
                    Log.e(TAG, e.toString());
                }
            }
            return null;
        }
    }

    class DateSerializer implements JsonSerializer<Date>
    {
        public JsonElement serialize(Date date, Type typeOfSrc, JsonSerializationContext context)
        {
            SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            if(date == null){
                return null;
            } else {
                String json = dateParser.format(date);
                return new JsonPrimitive(json);
            }
        }
    }
    //endregion
}