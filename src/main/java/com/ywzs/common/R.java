package com.ywzs.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

//通用的结果返回类
@Data
public class R<T> {
    private Integer Code; //1表示成功
    private String msg;
    private T data;
    private Map map = new HashMap();

    public static <T> R<T> success(T object){
        R<T> r = new R<>();
        r.Code=1;
        r.data=object;
        return r;
    }
    public static <T> R<T> error(String msg){
        R<T> r= new R<>();
        r.Code=0;
        r.msg=msg;
        return r;
    }
    public R<T> add(String key,Object value){
        this.map.put(key,value);
        return this;
    }
}
