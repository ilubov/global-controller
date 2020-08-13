package com.i.lubov.dto;

import com.i.lubov.entity.OperationLog;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OperationLogDTO {
    private List<Long> ids;
    private BigDecimal money;
    private String name;
    private Short type;
    private List<OperationLog> logs;
}
