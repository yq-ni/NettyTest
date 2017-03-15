package StartNetty.SpringNettyDemo.server.servicesImpl;

import StartNetty.SpringNettyDemo.services.CalculateService;

/**
 * Created by nyq on 2017/3/13.
 */

@RPCService(CalculateService.class)
public class CalculateServiceImpl implements CalculateService {
    @Override
    public int add(int a, int b) {
        return a + b;
    }

    @Override
    public int sub(int a, int b) {
        return a - b;
    }
}
