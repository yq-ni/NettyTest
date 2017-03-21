package StartNetty.SpringNettyDemo.client;


import StartNetty.SpringNettyDemo.services.CalculateService;
import StartNetty.SpringNettyDemo.services.HelloService;
import StartNetty.SpringNettyDemo.services.Person;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ClientTest {
    public static void main(String[] args) throws Exception {

        ApplicationContext ctx = new ClassPathXmlApplicationContext("client-spring.xml");
        RemoteServices remoteServices = ctx.getBean(RemoteServices.class);
        test(remoteServices);
        System.out.println("sleep");
        Thread.sleep(2000);
        test(remoteServices);
        System.out.print("quick");
        remoteServices.stop();
    }

    public static void test(RemoteServices remoteServices) throws Exception {
        int testNum = 1000;
        Person person = new Person();

        CountDownLatch latch = new CountDownLatch(testNum);
        for (int i = 0; i < testNum; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Person remote = remoteServices.create(HelloService.class).hello(person);
                    assert remote.toString().equals(person.toString());
                    int j = remoteServices.create(CalculateService.class).add(1, 2);
                    System.out.println(Thread.currentThread().getName() + " " + remote + j);
                    remoteServices.createAsync(HelloService.class).call("hello", "word").addListener(new OnReceiveListener() {
                        @Override
                        public void onReceive(Object result) {
                            System.out.println(result);
                        }
                    });
                    latch.countDown();
                }
            }).start();
        }

        latch.await(10, TimeUnit.SECONDS);
        System.out.println(latch.getCount());
    }
}