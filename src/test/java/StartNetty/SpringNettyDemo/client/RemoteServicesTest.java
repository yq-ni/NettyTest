package StartNetty.SpringNettyDemo.client; 

import StartNetty.SpringNettyDemo.services.CalculateService;
import StartNetty.SpringNettyDemo.services.HelloService;
import StartNetty.SpringNettyDemo.services.Person;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.CountDownLatch;

/**
* RemoteServices Tester. 
* 
* @author <Authors name> 
* @since <pre>03/13/2017</pre> 
* @version 1.0 
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:client-spring.xml")
public class RemoteServicesTest { 

    @Autowired
    private StartNetty.SpringNettyDemo.client.RemoteServices remoteServices;

    @Test
    public void testAll() throws Exception {
        testCallSync();
        testCallAsync();
        remoteServices.stop();
    }



    public void testCallSync() throws Exception {
        Person person = new Person();
        Object o = remoteServices.create(HelloService.class).hello(person);
        assert o.toString().equals(person.toString());

        assert remoteServices.create(CalculateService.class).add(1, 2) == 1+2;
    }


    public void testCallAsync() throws Exception {
        CountDownLatch latch = new CountDownLatch(2);
        Person person = new Person();
        AsyncServiceProxy s = remoteServices.createAsync(HelloService.class);
        RPCFuture resFuture = s.call("hello", person);
        resFuture.addListener(new OnReceiveListener() {
            @Override
            public void onReceive(Object result) {
                assert result.toString().equals(person.toString());
                latch.countDown();
            }
        });

        remoteServices.createAsync(CalculateService.class).call("add", 1, 2).addListener(new OnReceiveListener() {
            @Override
            public void onReceive(Object result) {
                assert result.equals(1+2);
                latch.countDown();
            }
        });
        latch.await();
    }


    
}
