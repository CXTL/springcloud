package com.dupake.orderserver.service.impl;

import com.dupake.orderserver.mapper.TransactionLogMapper;
import com.dupake.orderserver.service.TransactionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author dupake
 * @version 1.0
 * @date 2020/4/21 15:07
 * @description
 */
@Service
public class TransactionLogServiceImpl implements TransactionLogService {

    @Resource
    private TransactionLogMapper transactionLogMapper;

    @Override
    public int getByTransactionId(String transactionId) {
        return transactionLogMapper.getByTransactionId(transactionId);
    }
}
