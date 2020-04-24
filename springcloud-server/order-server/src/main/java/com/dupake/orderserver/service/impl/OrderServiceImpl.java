package com.dupake.orderserver.service.impl;

import com.alibaba.fastjson.JSON;
import com.dupake.common.enums.YesNoSwitchEnum;
import com.dupake.orderserver.config.TransactionProducer;
import com.dupake.orderserver.entity.Order;
import com.dupake.common.dto.OrderDTO;
import com.dupake.orderserver.entity.TransactionLog;
import com.dupake.orderserver.mapper.OrderMapper;
import com.dupake.orderserver.mapper.TransactionLogMapper;
import com.dupake.orderserver.service.IdGeneratorService;
import com.dupake.orderserver.service.OrderService;
import com.dupake.common.utils.IdUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.rocketmq.client.exception.MQClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * @author dupake
 * @version 1.0
 * @date 2020/4/16 16:59
 * @description
 */
@Service
public class OrderServiceImpl implements OrderService {


    @Resource
    private OrderMapper orderMapper;

    @Resource
    TransactionLogMapper transactionLogMapper;

    @Resource
    TransactionProducer producer;

    @Resource
    IdGeneratorService idGeneratorService;


    IdUtil idUtil = new IdUtil(1, 1, 1);

    Logger logger = LoggerFactory.getLogger(this.getClass());

    //执行本地事务时调用，将订单数据和事务日志写入本地数据库
    @Transactional
    @Override
    public void createOrder(OrderDTO orderDTO, String transactionId) {

        //1.创建订单
        Order order = new Order();
        BeanUtils.copyProperties(orderDTO,order);
        order.setCreateTime(System.currentTimeMillis());
        order.setUpdateTime(System.currentTimeMillis());
        orderMapper.insert(order);

        //2.写入事务日志
        TransactionLog log = new TransactionLog();
        log.setId(transactionId);
        log.setBusiness("order");
        log.setForeignKey(String.valueOf(order.getId()));
        log.setCreateTime(System.currentTimeMillis());
        log.setUpdateTime(System.currentTimeMillis());
        transactionLogMapper.insert(log);

        logger.info("订单创建完成。{}", orderDTO);
    }

    //前端调用，只用于向RocketMQ发送事务消息
    @Override
    public void createOrder(OrderDTO order) throws MQClientException {
        order.setId(idUtil.nextId());
        order.setOrderNo(idGeneratorService.generatorId("order").toString());
        order.setAmount(new BigDecimal(100));
        order.setStatus(YesNoSwitchEnum.NO.getValue());
        producer.send(JSON.toJSONString(order), "order");
    }


    @Override
    public Order findInfoById(Integer id) {
        return orderMapper.findInfoById(id);
    }

    @Override
    public PageInfo<Order> findList(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Order> list = orderMapper.findList();
        PageInfo<Order> page = new PageInfo<>(list);
        return page;
    }

    @Override
    public Order insert(Order order) {
        order.setCreateTime(System.currentTimeMillis());
        order.setUpdateTime(System.currentTimeMillis());
        orderMapper.insert(order);
        return order;
    }

    @Override
    public Order update(Order order) {
        order.setUpdateTime(System.currentTimeMillis());
        orderMapper.update(order);
        return null;
    }

    @Override
    public void deleteByIds(Integer id) {
        orderMapper.delete(id);
    }


}
