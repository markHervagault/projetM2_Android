package istic.m2.ila.firefighterapp.clientRabbitMQ;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import com.rabbitmq.client.ConnectionFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import istic.m2.ila.firefighterapp.clientRabbitMQ.messages.NewDroneMessage;


public class ServiceRabbitMQ extends Service {

    public static String TAG = "Service RABBITMQ => ";
    List<RabbitMQThread> subscribeThreadList = new ArrayList<RabbitMQThread>();

    private BlockingDeque<String> queue = new LinkedBlockingDeque<String>();

    ConnectionFactory factory = new ConnectionFactory();

    // This is the object that receives interactions from clients.
    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public ServiceRabbitMQ getService() {
            return ServiceRabbitMQ.this;
        }
    }

    /** Called when the service is being created. */
    @Override
    public void onCreate() {
        EventBus.getDefault().register(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        for(RabbitMQThread thread: subscribeThreadList){
            thread.interrupt();
        }
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /*private void setupConnectionFactory() {
        final URI uri = URI.create("amqp://guest:guest@nwanono.info:5672/");
        //final URI uri = URI.create("amqp://guest:guest@148.60.11.57:6005/");
        try {
            factory.setAutomaticRecoveryEnabled(false);
            factory.setUri(uri);
            factory.setVirtualHost("/");
        } catch (KeyManagementException | NoSuchAlgorithmException | URISyntaxException e1) {
            e1.printStackTrace();
        }
    }*/

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEvent(NewDroneMessage event) {
        Log.d(TAG, "======================================================= Je viens de recevoir dans le bus de données le drone n° : "+event.getDroneId());
        RabbitMQThread subscribeThread = new RabbitMQThread(event);
        subscribeThread.start();
        subscribeThreadList.add(subscribeThread);
    }

    void start() {
    }

//    void subscribe() {
//        subscribeThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while(true) {
//                    try {
//                        Connection connection = factory.newConnection();
//                        Channel channel = connection.createChannel();
//                        channel.basicQos(1);
//
//                        channel.queueDeclare("0_info", false, false, false, null);
//
//                        Consumer consumer = new DefaultConsumer(channel) {
//                            @Override
//                            public void handleDelivery(String consumerTag, Envelope envelope,
//                                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
//                                incomingMessageHandler = new String(body, "UTF-8");
//                                Log.d(TAG, " [x] Received '" + envelope.getRoutingKey() + "':'" + incomingMessageHandler + "'");
//                            }
//                        };
//
//                        channel.basicConsume("0_info", true, consumer);
//
//                    } catch (Exception e1) {
//                        Log.d("", "Connection broken: " + e1.getClass().getName());
//                        try {
//                            Thread.sleep(4000); //sleep and then try again
//                        } catch (InterruptedException e) {
//                            break;
//                        }
//                    }
//                }
//            }
//        });
//        subscribeThread.start();
//    }
}