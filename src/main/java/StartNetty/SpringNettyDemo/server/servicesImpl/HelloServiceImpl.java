package StartNetty.SpringNettyDemo.server.servicesImpl;

import StartNetty.SpringNettyDemo.services.HelloService;
import StartNetty.SpringNettyDemo.services.Person;

/**
 * Created by nyq on 2017/3/13.
 */

@RPCService(HelloService.class)
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String name) {
        return "Hello, " + name;
    }

    @Override
    public Person hello(Person person) {
        return person;
    }
}
