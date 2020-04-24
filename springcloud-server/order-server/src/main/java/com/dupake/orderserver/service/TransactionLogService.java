package com.dupake.orderserver.service;

import org.springframework.stereotype.Service;

/**
 * @author dupake
 * @version 1.0
 * @date 2020/4/21 15:06
 * @description 事务日志服务类
 */

public interface TransactionLogService {
    int getByTransactionId(String transactionId);
}
