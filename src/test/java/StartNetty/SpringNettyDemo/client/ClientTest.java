package StartNetty.SpringNettyDemo.client;


import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ClientTest {
    public static void main(String[] args) {

        new ClassPathXmlApplicationContext("client-spring.xml");
    }
}