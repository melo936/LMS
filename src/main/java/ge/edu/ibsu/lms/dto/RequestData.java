package ge.edu.ibsu.lms.dto;

public final class RequestData<T> {
    private T data;
    private Paging paging;

    public T data() {
        return data;
    }

    public Paging paging() {
        return paging;
    }

}