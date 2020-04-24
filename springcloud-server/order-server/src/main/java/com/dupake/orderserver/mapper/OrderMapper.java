package com.dupake.orderserver.mapper;

import com.dupake.orderserver.entity.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author dupake
 * @version 1.0
 * @date 2020/4/16 17:00
 * @description
 */
@Mapper
public interface OrderMapper {
    Order findInfoById(Integer id);

    List<Order> findList();

    void insert(Order user);

    void update(Order user);

    void delete(Integer id);
}
