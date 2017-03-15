package StartNetty.SpringNettyDemo.services;

/**
 * Created by nyq on 2017/3/13.
 */

@Service(address = "127.0.0.1:8080")
public interface CalculateService {
    int add(int a, int b);
    int sub(int a, int b);
}
