package com.example.arefin.dagger2loginlistapplication.network;

import com.example.arefin.dagger2loginlistapplication.models.Todo;
import java.util.List;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface APIService {
    @GET("todos/")
    Observable<List<Todo>> getAllTodos();

    @GET("todos/{todoid}")
    Observable<Todo> getTodoDetails(@Path("todoid") int id);

    @GET("users/{userid}")
    Observable getUserInfo(@Path("userid") int userId);
}
