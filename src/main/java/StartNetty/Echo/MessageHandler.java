package StartNetty.Echo;

/**
 * Created by nyq on 2017/1/25.
 */
public class MessageHandler {
    public static final String DELIMITER = "$_$";
    public static String wrapMessage(String message) {
        return message + DELIMITER;
    }
}
