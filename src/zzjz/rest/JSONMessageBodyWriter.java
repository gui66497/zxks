package zzjz.rest;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * json信息.
 * @author zhuxiaoxiao
 * @version 2016/09/13
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
public class JSONMessageBodyWriter implements MessageBodyWriter {

    /**
     * 输入.
     * @param type 类型
     * @param genericType generic类型
     * @param annotations 注释
     * @param mediaType media类型
     * @return true
     */
    public boolean isWriteable(Class type, Type genericType,
                               Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    /**
     *获取size.
     * @param t t对象
     * @param type 类型
     * @param genericType generic类型
     * @param annotations 注释
     * @param mediaType media类型
     * @return -1
     */
    public long getSize(Object t, Class type, Type genericType,
                        Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    /**
     * 写入.
     * @param target 目标
     * @param type 类型
     * @param genericType generic类型
     * @param annotations 注释
     * @param mediaType media类型
     * @param httpHeaders  httpHeaders
     * @param outputStream 输出流
     * @throws IOException IO异常
     * @throws WebApplicationException WebApplication异常
     */
    public void writeTo(Object target, Class type, Type genericType,
                        Annotation[] annotations, MediaType mediaType,
                        MultivaluedMap httpHeaders, OutputStream outputStream)
            throws IOException, WebApplicationException {
        new ObjectMapper().writeValue(outputStream, target);
    }
}