package com.example.arefin.dagger2loginlistapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arefin.dagger2loginlistapplication.models.Todo;
import com.example.arefin.dagger2loginlistapplication.network.APIService;
import com.example.arefin.dagger2loginlistapplication.network.injections.DaggerRetrofitNetworkComponent;
import com.example.arefin.dagger2loginlistapplication.network.injections.RetrofitNetworkComponent;
import com.example.arefin.dagger2loginlistapplication.network.injections.RetrofitNetworkModule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodoListActivity extends AppCompatActivity {

    @Inject
    APIService apiService;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    TodosBasicListAdapter adapter;
    List<Todo> todos;

    //the component which will help dagger injection
    private RetrofitNetworkComponent retrofitNetworkComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_lists);
        initViews();
        initDependencyInjections();
        Observable<List<Todo>> allTodosObservable = apiService.getAllTodos();
        allTodosObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onNextListener,this::onErrorListener, this::onCompleteListener);
    }

    private void initViews() {
        progressBar = findViewById(R.id.progress_circular);
        recyclerView = findViewById(R.id.todosRecyclerView);
        todos = new ArrayList<>();
        adapter = new TodosBasicListAdapter(todos);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
    }

    private void onCompleteListener() {
        progressBar.setVisibility(View.GONE);
    }

    private void initDependencyInjections() {
        //the DaggerSharedPrefComponent name is generated after a rebuild
        retrofitNetworkComponent = DaggerRetrofitNetworkComponent
                .builder()
                //the number of modules declared in the pref module, all can be here to use
                .retrofitNetworkModule(new RetrofitNetworkModule())
                .build();
        retrofitNetworkComponent.inject(this);
    }
    private void onNextListener(List<Todo> todos) {
        if (todos != null && todos.size() != 0) {
            this.todos.addAll(todos);
            adapter.notifyDataSetChanged();
        }
        else
            Toast.makeText(this, "NO RESULTS FOUND", Toast.LENGTH_LONG).show();
    }

    private void onErrorListener(Throwable t) {
        progressBar.setVisibility(View.GONE);

        Toast.makeText(this, "ERROR IN FETCHING API RESPONSE. Try again",
                Toast.LENGTH_LONG).show();
    }

}
