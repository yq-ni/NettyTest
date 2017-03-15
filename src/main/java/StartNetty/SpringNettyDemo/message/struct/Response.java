package StartNetty.SpringNettyDemo.message.struct;

import java.io.Serializable;

/**
 * Created by nyq on 2017/3/12.
 */
public class Response implements Serializable{
    private String ID;
    private String error;
    private Object result;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
