package com.dupake.orderserver.controller;

import com.dupake.orderserver.entity.Order;
import com.dupake.orderserver.service.IdGeneratorService;
import com.dupake.orderserver.utils.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author dupake
 * @version 1.0
 * @date 2020/4/21 9:40
 * @description
 */
@RestController
@Api(tags = "Test控制器")
public class TestController {


    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private IdGeneratorService idGeneratorService;


    @ApiOperation(value = "redis测试", notes = "redis测试 - for-web")
    @GetMapping("/testRedis")
    public void testRedis() throws MQClientException {
        Order order = new Order();
        order.setId(1L);
        order.setName("测试REDIS");
        order.setCreateTime(System.currentTimeMillis());
        redisUtil.set("redistest", order);
        boolean exists = redisUtil.hasKey("redistest");
        System.out.println("redis是否存在相应的key" + exists);
        Order getUser = (Order) redisUtil.get("redistest");
        System.out.println("从redis数据库获取的user:" + getUser.toString());
    }

    @ApiOperation(value = "订单号测试", notes = "订单号测试 - for-web")
    @GetMapping("/testOrderNo")
    public void testOrderNo() {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            cachedThreadPool.execute(() -> {
                for (int j = 0; j < 20000; j++) {
                    System.out.println(idGeneratorService.generatorId("log") + "----------" + idGeneratorService.generatorId("order"));
                }

            });
        }
    }


    @ApiOperation(value = "gateway测试", notes = "gateway测试 - for-web")
    @GetMapping(value = "/get")
    public String testGateway() {
        int i = 1/0;
        try {
            Thread.sleep(50000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "hello gateway";
    }


}
