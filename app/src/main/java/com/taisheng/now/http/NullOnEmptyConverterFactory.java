package com.taisheng.now.http;

import com.google.gson.Gson;
import com.th.j.commonlibrary.utils.LogUtilH;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by zry on 2018/3/20.
 */

public class NullOnEmptyConverterFactory extends  Converter.Factory  {
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        final Converter<ResponseBody, ?> delegate = retrofit.nextResponseBodyConverter(this, type, annotations);
        return new Converter<ResponseBody, Object>() {
            @Override
            public Object convert(ResponseBody body) throws IOException {

                Gson gson = new Gson();
                String jsonBDID = gson.toJson(body);
                LogUtilH.e("请求参数--》"+jsonBDID);
                if (body.contentLength() == 0) return null;
                return delegate.convert(body);
            }
        };
    }
}
