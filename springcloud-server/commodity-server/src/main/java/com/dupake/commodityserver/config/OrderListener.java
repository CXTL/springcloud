package com.dupake.commodityserver.config;

import com.alibaba.fastjson.JSONObject;
import com.dupake.commodityserver.service.PointService;
import com.dupake.common.dto.OrderDTO;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author dupake
 * @version 1.0
 * @date 2020/4/21 15:21
 * @description 监听到消息之后，调用业务服务类处理即可。处理完成则返回CONSUME_SUCCESS以提交，处理失败则返回RECONSUME_LATER来重试
 */
@Component
public class OrderListener implements MessageListenerConcurrently {

    @Resource
    PointService pointsService;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext context) {
        logger.info("消费者线程监听到消息。");
        try {
            for (MessageExt message : list) {
                if (!processor(message)) {//异常三次重试人工兜底
                    logger.info("开始处理订单数据，准备增加积分....");
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        } catch (Exception e) {
            logger.error("处理消费者数据发生异常。{}", e);

            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
    }

    /**
     * 消息处理，第3次处理失败后，发送邮件通知人工介入
     *
     * @param message
     * @return
     */
    private boolean processor(MessageExt message) {
        String body = new String(message.getBody());
        try {
            logger.info("消息处理....{}", body);
//            int k = 1/0;
            OrderDTO order = JSONObject.parseObject(message.getBody(), OrderDTO.class);
            pointsService.increasePoints(order);
            return true;
        } catch (Exception e) {
            if (message.getReconsumeTimes() >= 3) {
                logger.error("消息重试已达最大次数，将通知业务人员排查问题。{}", message.getMsgId());
//                sendMail(message);
                return true;
            }
            return false;
        }
    }
}

