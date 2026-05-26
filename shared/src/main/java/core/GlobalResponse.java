package core;

import lombok.Getter;

import java.util.List;

@Getter
public class GlobalResponse<T> {
    public static String SUCCESS = "success";
    public static String FAILURE = "failure";

    public long code;
    public String status;
    public T data;
    public List<ErrorItem> errorItems;

    public GlobalResponse(List<ErrorItem> errorItems) {
        this.errorItems = errorItems;
        this.status = FAILURE;
        this.data = null;
    }

    public GlobalResponse(T data) {
        this.errorItems = null;
        this.status = SUCCESS;
        this.data = data;
    }

    public record ErrorItem(String message) {

    }
}
