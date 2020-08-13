package com.i.lubov.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.i.lubov.exception.BusinessException;
import com.i.lubov.util.GlobalUtils;
import com.i.lubov.vo.ServiceVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.*;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/global")
public class GlobalController {

    @Autowired
    private ApplicationContext applicationContext;

    private static Map<String, ServiceVO> serviceMap = Maps.newHashMap();

    /**
     * 调用服务路由
     *
     * @param id      服务ID
     * @param request
     * @return
     */
    @RequestMapping(value = "/service/{id}")
    public Object global(@PathVariable String id, HttpServletRequest request) {
        // 获取服务方法
        ServiceVO serviceVO = this.getServiceVO(id);
        String serviceInterface = serviceVO.getServiceInterface();
        try {
            Class<?> c = Class.forName(serviceInterface);
            Method method = this.getMethod(c, serviceVO.getServiceMethod());
            // 获取服务方法参数个数以及参数类型
            Object[] parameterObjs = serviceParameterHandler(method, request);
            // 调用服务
            Object targetObject = applicationContext.getBean(c);
            return invokeMethod(method, parameterObjs, targetObject);
        } catch (ClassNotFoundException e) {
            log.error("没有加载到对应服务", e);
            throw new BusinessException("没有加载到对应服务");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("调用服务错误", e);
            throw new BusinessException("调用服务错误:" + serviceInterface);
        }
    }

    /**
     * 根据ID获取ServiceVO
     *
     * @param id 服务ID
     * @return
     */
    private ServiceVO getServiceVO(String id) {
        ServiceVO serviceVO = serviceMap.get(id);
        if (serviceVO == null) {
            throw new BusinessException("没有对应的服务");
        }
        return serviceVO;
    }

    /**
     * 根据服务Class、服务方法名 获取服务方法
     *
     * @param clazz
     * @param serviceMethod
     * @return
     */
    private Method getMethod(Class<?> clazz, String serviceMethod) {
        Method method = BeanUtils.findMethodWithMinimalParameters(clazz, serviceMethod);
        if (method == null) {
            throw new BusinessException("没有对应的服务方法");
        }
        return method;
    }

    /**
     * 服务参数封装
     *
     * @param method  服务方法
     * @param request
     * @return
     */
    private Object[] serviceParameterHandler(Method method, HttpServletRequest request) {
        Class<?>[] parameterTypeClasses = method.getParameterTypes();
        Object[] parameterObjs = null;
        try {
            if (parameterTypeClasses.length > 0) {
                // 转换请求参数到对应的参数类型中
                parameterObjs = new Object[parameterTypeClasses.length];
                for (int i = 0; i < parameterTypeClasses.length; i++) {
                    Object parameterObj = this.requestParameterHandler(request, parameterTypeClasses[i]);
                    parameterObjs[i] = parameterObj;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("封装请求参数错误");
        }
        return parameterObjs;
    }

    /**
     * 请求参数封装
     *
     * @param request
     * @param parameterTypeClass
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws IOException
     */
    private Object requestParameterHandler(HttpServletRequest request, Class<?> parameterTypeClass)
            throws InstantiationException, IllegalAccessException, SecurityException, IOException {
        String method = request.getMethod();
        boolean getOrDelete = "GET".equals(method) || "DELETE".equals(method);
        // 获取所有的Field
        List<Field> fieldList = GlobalUtils.getDeclaredField(parameterTypeClass, Object.class);
        Object parameterObj = parameterTypeClass.newInstance();
        Map<String, Object> parameter = this.getParameter(request);
        for (Field field : fieldList) {
            field.setAccessible(true);
            if (getOrDelete) {
                String fieldValue = request.getParameter(field.getName());
                if (StrUtil.isNotBlank(fieldValue)) {
                    field.set(parameterObj, GlobalUtils.convertDataType(fieldValue, field));
                }
            } else {
                String fieldJsonName = GlobalUtils.toLowerCaseFirst(field.getName());
                Object o = parameter.get(fieldJsonName);
                String fieldJsonValue = o == null ? null : o.toString();
                if (StrUtil.isNotBlank(fieldJsonValue)) {
                    Class<?> fieldClass = field.getType();
                    if (fieldClass.isAssignableFrom(List.class)) {
                        Type fieldType = field.getGenericType();
                        if (fieldType instanceof ParameterizedType) {
                            ParameterizedType parameterizedType = (ParameterizedType) fieldType;
                            Class<?> fieldParameterizedTypeClass = (Class<?>) parameterizedType.getActualTypeArguments()[0];
                            Object fieldObject = JSON.parseArray(fieldJsonValue, fieldParameterizedTypeClass);
                            field.set(parameterObj, fieldObject);
                        }
                    } else {
                        Object fieldObject = JSON.parseObject(fieldJsonValue, fieldClass);
                        field.set(parameterObj, fieldObject);
                    }
                }
            }
        }
        return parameterObj;
    }

    /**
     * 获取非GET请求的请求参数
     *
     * @param request
     * @return
     * @throws IOException
     */
    private Map<String, Object> getParameter(HttpServletRequest request) throws IOException {
        // 返回参数
        Map<String, Object> params = Maps.newHashMap();
        // 获取内容格式
        String contentType = request.getContentType();
        if (contentType != null && !contentType.equals("")) {
            contentType = contentType.split(";")[0];
        }
        // form表单格式 表单形式可以从 ParameterMap中获取
        if ("appliction/x-www-form-urlencoded".equalsIgnoreCase(contentType)) {
            // 获取参数
            Map<String, String[]> parameterMap = request.getParameterMap();
            if (parameterMap != null) {
                for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                    params.put(entry.getKey(), entry.getValue()[0]);
                }
            }
        }
        // json格式 json格式需要从request的输入流中解析获取
        if ("application/json".equalsIgnoreCase(contentType)) {
            // 使用 commons-io中 IOUtils 类快速获取输入流内容
            String paramJson = IOUtils.toString(request.getInputStream(), "UTF-8");
            Map parseObject = JSON.parseObject(paramJson, Map.class);
            params.putAll(parseObject);
        }
        return params;
    }

    /**
     * 调用服务方法
     *
     * @param method
     * @param parameterObjs
     * @param targetObject
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private Object invokeMethod(Method method, Object[] parameterObjs, Object targetObject)
            throws IllegalAccessException, InvocationTargetException {
        Object result;
        if (parameterObjs == null) {
            result = method.invoke(targetObject);
        } else {
            result = method.invoke(targetObject, parameterObjs);
        }
        return result;
    }

    /**
     * 初始化
     *
     * @throws IOException
     */
    @PostConstruct
    public void init() throws IOException {
        Resource resource = new PathMatchingResourcePatternResolver().getResource("service.json");
        InputStream inputStream = resource.getInputStream();
        byte[] by = new byte[inputStream.available()];
        inputStream.read(by);
        String strJSON = new String(by, "UTF-8");
        List<ServiceVO> list = JSON.parseArray(strJSON, ServiceVO.class);
        for (ServiceVO vo : list) {
            serviceMap.put(vo.getServiceId(), vo);
        }
    }
}
