package StartNetty.SpringNettyDemo.server;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by nyq on 2017/3/5.
 */
public class ServerTest {
    public static void main(String[] args) {

        new ClassPathXmlApplicationContext("server-spring.xml");
    }
}
