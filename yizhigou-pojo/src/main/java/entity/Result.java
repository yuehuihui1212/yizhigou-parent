package entity;

import java.io.Serializable;

/**
 * @Author: yuehuihui
 * @Description:
 * @Date: created in 16:25 2018/5/2
 * @Modified By:
 **/
public class Result implements Serializable {
    private boolean success;
    private String message;

    public Result() {
    }

    public Result(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
