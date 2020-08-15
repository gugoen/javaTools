package com.guo.tools.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;

import java.util.Map;


public class GsonUtil {

    public static Gson gson = null;
    static {
        GsonBuilder gb = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss");
        gb.setLongSerializationPolicy(LongSerializationPolicy.STRING);
        gson = gb.create();
    }

    /**
     * obj -> json
     * @param obj
     * @return
     */
    public static String gObject2Json(Object obj) {
        String json = gson.toJson(obj, obj.getClass());
        return json;
    }


    public static <T> T gObject2Json(String json,Class<T> classes) {
        return gson.fromJson(json, classes);
    }


    /**
     * json -> map
     * @param str
     * @return
     */
    public static Map<String,Object> gJson2Map(String str) {
        Map<String,Object> result = gson.fromJson(str,Map.class);
        return result;
    }

    /**
     * obj -> json
     * @param obj
     * @return
     */
    public static Map<String,Object> gObject2Map(Object obj) {
        String json = gson.toJson(obj, obj.getClass());
        Map<String,Object> result = gson.fromJson(json,Map.class);
        return result;
    }

}
