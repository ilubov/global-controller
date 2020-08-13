package com.i.lubov.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ServiceVO implements Serializable {

    /**
     * 服务ID
     */
    private String serviceId;

    /**
     * 服务接口
     */
    private String serviceInterface;

    /**
     * 服务方法
     */
    private String serviceMethod;
}
