package com.taisheng.now.http;



import com.taisheng.now.Constants;
import com.taisheng.now.SampleAppLike;
import com.taisheng.now.util.Apputil;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.fastjson.FastJsonConverterFactory;


/**
 * 封装Retrofit的工具类
 * <p/>
 * Created by long
 */
public class ApiUtils {

    private static ApiService taishengApiService;


    private static int TIME_OUT_CONNECT = 10;//连接超时时间
    private static int TIME_OUT_READ = 30;//读取超时时间
    private static int TIME_OUT_WRITE = 120;//写入超时时间

    private static int TIME_OUT_READ_FILE = 60;//文件读写超时
    private static int TIME_OUT_WRITE_FIlE = 60;


    /**
     *
     * @return
     */
    public static ApiService getApiService() {
        taishengApiService = getRetrofitClient(Constants.Url.Host).create(ApiService.class);
        return taishengApiService;
    }

    /**
     * 文件上传端口不同
     */
    public static ApiService getFileApiService(){
        taishengApiService = getRetrofitClient(Constants.Url.File_Host).create(ApiService.class);
        return taishengApiService;
    }




    /**
     * 获取RetrofitClient
     *
     * @param baseUrl  baseUrl
     * @return Retrofit
     */
    private static Retrofit getRetrofitClient(String baseUrl) {
        //统一添加log的过滤器；
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //统一添加Header信息的过滤器；
        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                // 添加 统一的Header
                Request.Builder requestBuilder = original.newBuilder()
                        .header("X-OS", "Android"+android.os.Build.VERSION.RELEASE)
                        .header("X-App-Version", Apputil.getVersionName(SampleAppLike.mcontext))
                        .header("device_model", URLEncoder.encode(android.os.Build.MODEL.replaceAll(" ", ""), "UTF-8"))
                        .header("x_os_int",android.os.Build.VERSION.SDK_INT+"")
                        .method(original.method(), original.body());

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };

        Retrofit retrofit;
        OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        OkHttpClient httpClient;
// 普通请求
            httpBuilder.connectTimeout(TIME_OUT_CONNECT, TimeUnit.SECONDS)//超时时间
                    .writeTimeout(TIME_OUT_WRITE, TimeUnit.SECONDS)
                    .readTimeout(TIME_OUT_READ, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)//重试机制
                    .addInterceptor(headerInterceptor)//请求拦截器
                    .addInterceptor(logInterceptor);//Log拦截器
//            HttpsSwitcher.switchHttp(httpBuilder);
            httpClient = httpBuilder.build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(FastJsonConverterFactory.create())//使用Gson解析数据。
                    .client(httpClient)//添加统一的Header和打印Logger的时候需要使用过滤器，需要使用到OKHttpClient。必须要注意添加的方式，不同版本（早期版本）不一样。
                    .build();

        return retrofit;
    }
}
