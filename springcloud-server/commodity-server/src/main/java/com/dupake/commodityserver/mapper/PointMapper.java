package com.dupake.commodityserver.mapper;

import com.dupake.commodityserver.entity.Point;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author dupake
 * @version 1.0
 * @date 2020/4/16 17:00
 * @description
 */
@Mapper
public interface PointMapper {

    int getByOrderNo(@Param("orderNo") String orderNo);

    void insert(Point points);
}
