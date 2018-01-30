package hr.from.bkoruznjak.rida.root.network;

import android.content.Context;
import android.net.ConnectivityManager;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import hr.from.bkoruznjak.rida.root.AppScope;
import hr.from.bkoruznjak.rida.root.RideApp;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by bkoruznjak on 30/01/2018.
 */

@Module
public class NetworkModule {

    String mBaseUrl;

    public NetworkModule(String baseUrl) {
        this.mBaseUrl = baseUrl;
    }

    @Provides
    @AppScope
    public Cache provideOkHttpCache(RideApp application) {
        int cacheSize = 2 * 1024; // 2 MB
        Cache cache = new Cache(application.getCacheDir(), cacheSize);
        return cache;
    }

    @Provides
    @AppScope
    public Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setLenient();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    @Provides
    @AppScope
    @Named("standardClient")
    public OkHttpClient provideOkHttpClient(Cache cache) {
        OkHttpClient client = new OkHttpClient.Builder()
                .cache(cache)
                .build();
        return client;
    }

    @Provides
    @AppScope
    @Named("debugClient")
    public OkHttpClient provideLoggingkHttpClient(Cache cache) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor())
                .cache(cache)
                .build();
        return client;
    }

    @Provides
    @AppScope
    @Named("sixtySecondTimeoutClient")
    public OkHttpClient provideLongTimeoutOkHttpClient(Cache cache) {
        OkHttpClient client = new OkHttpClient.Builder()
                .cache(cache)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
        return client;
    }

    @Provides
    @AppScope
    @Named("simpleRetrofit")
    public Retrofit provideRetrofit(Gson gson, @Named("standardClient") OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(mBaseUrl)
                .client(okHttpClient)
                .build();
        return retrofit;
    }

    @Provides
    @AppScope
    @Named("debugRetrofit")
    public Retrofit provideDebugRetrofit(Gson gson, @Named("debugClient") OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(mBaseUrl)
                .client(okHttpClient)
                .build();
        return retrofit;
    }

    @Provides
    @AppScope
    @Named("simpleWebApi")
    public RidaWebAPI providesSimpleWebApi(@Named("simpleRetrofit") Retrofit retrofit) {
        return retrofit.create(RidaWebAPI.class);
    }


    @Provides
    @AppScope
    @Named("debugWebApi")
    public RidaWebAPI providesDebugWebApi(@Named("debugRetrofit") Retrofit retrofit) {
        return retrofit.create(RidaWebAPI.class);
    }

    @Provides
    @AppScope
    public ConnectivityManager provideConnectivityManager(RideApp context) {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Provides
    @AppScope
    public NetworkManager provideNetworkManager(ConnectivityManager connectivityManager) {
        return new NetworkManager(connectivityManager);
    }
}