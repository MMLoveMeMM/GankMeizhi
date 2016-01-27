package cn.chenyuanming.gankmeizhi;

import com.google.gson.Gson;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;

import cn.chenyuanming.gankmeizhi.beans.DayGoodsBean;
import cn.chenyuanming.gankmeizhi.beans.GoodsBean;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by Administrator on 2016/1/25.
 */
public class GankApi {
    private static GankApi instance;

    private final GankService gankService;
    private  GankApi(){
        Cache cache;
        OkHttpClient okHttpClient = null;
        try {
            File cacheDir = new File(GankApplication.getContext().getCacheDir().getPath(), "gank_cache.json");
            cache = new Cache(cacheDir, 10 * 1024 * 1024);
            okHttpClient = new OkHttpClient();
            okHttpClient.setCache(cache);
        } catch (Exception e) {
            e.printStackTrace();
        }

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constants.ENDPOINT)
                .setClient(new OkClient(okHttpClient))
                .setConverter(new GsonConverter(new Gson()))
//                .setRequestInterceptor(mRequestInterceptor)
                .build();
        gankService = restAdapter.create(GankService.class);
    }

    public  static GankApi getInstance() {
        if(instance==null){
            synchronized (GankApi.class){
                if(instance==null){
                    instance = new GankApi();
                }
            }
        }
        return instance;
    }

    public interface GankService {

        @GET("/data/Android/{limit}/{page}")
        Observable<GoodsBean> getAndroidGoods(
                @Path("limit") int limit,
                @Path("page") int page
        );

        @GET("/data/iOS/{limit}/{page}")
        Observable<GoodsBean> getIosGoods(
                @Path("limit") int limit,
                @Path("page") int page
        );

        @GET("/data/all/{limit}/{page}")
        Observable<GoodsBean> getAllGoods(
                @Path("limit") int limit,
                @Path("page") int page
        );

        @GET("/data/福利/{limit}/{page}")
        Observable<GoodsBean> getBenefitsGoods(
                @Path("limit") int limit,
                @Path("page") int page
        );

        @GET("/day/{year}/{month}/{day}")
        Observable<DayGoodsBean> getGoodsByDay(
                @Path("year") int year,
                @Path("month") int month,
                @Path("day") int day
        );
    }

    public Observable<GoodsBean> getCommonGoods(String type,int limit, int page) {
        if("Android".equalsIgnoreCase(type)){
            return gankService.getAndroidGoods(limit, page);
        }
        if("IOS".equalsIgnoreCase(type)){
            return gankService.getIosGoods(limit, page);
        }
        return gankService.getAndroidGoods(limit, page);
    }

    public Observable<GoodsBean> getAndroidGoods(int limit, int page) {
        return gankService.getAndroidGoods(limit, page);
    }

    public Observable<GoodsBean> getIosGoods(int limit, int page) {
        return gankService.getIosGoods(limit, page);
    }

    public Observable<GoodsBean> getAllGoods(int limit, int page) {
        return gankService.getAllGoods(limit, page);
    }

    public Observable<GoodsBean> getBenefitsGoods(int limit, int page) {
        return gankService.getBenefitsGoods(limit, page);
    }

    public Observable<DayGoodsBean> getGoodsByDay(int year,int month,int day) {
        return gankService.getGoodsByDay(year, month,day);
    }

}
