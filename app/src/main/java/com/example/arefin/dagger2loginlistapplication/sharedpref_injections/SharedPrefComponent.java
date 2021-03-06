package com.example.arefin.dagger2loginlistapplication.sharedpref_injections;

import com.example.arefin.dagger2loginlistapplication.LoginActivity;
import com.example.arefin.dagger2loginlistapplication.MainActivity;

import javax.inject.Singleton;
import dagger.Component;

//Declaring what modules will be using the component
@Component(modules = {SharedPreferenceModule.class})
@Singleton
public interface SharedPrefComponent {
    void inject(LoginActivity loginActivity);
    void inject(MainActivity mainActivity);
}
