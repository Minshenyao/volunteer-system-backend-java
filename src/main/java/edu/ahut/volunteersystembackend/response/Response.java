package edu.ahut.volunteersystembackend.response;


public class Response <T> {
    private Integer code;
    private String status;
    private String message;
    private T data;

    public static <K>Response<K> success(String message, K data){
        Response<K> response = new Response<>();
        response.setCode(200);
        response.setStatus("success");
        response.setMessage(message);
        response.setData(data);
        return response;
    }
    public static <K>Response<K> error(Integer code,String message){
        Response<K> response = new Response<>();
        response.setCode(code);
        response.setStatus("error");
        response.setMessage(message);
        return response;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
