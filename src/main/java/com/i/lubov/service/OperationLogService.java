package com.i.lubov.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.i.lubov.dto.OperationLogDTO;
import com.i.lubov.entity.OperationLog;

public interface OperationLogService extends IService<OperationLog> {

    String test(OperationLogDTO dto);
}
