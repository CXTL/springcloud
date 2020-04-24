package com.dupake.commodityserver.service;

import com.dupake.commodityserver.entity.SysUser;
import com.github.pagehelper.PageInfo;

/**
 * @author dupake
 * @version 1.0
 * @date 2020/4/16 16:58
 * @description
 */
public interface SysUserService {

    SysUser findInfoById(Integer id);

    PageInfo<SysUser> findList(Integer pageNum, Integer pageSize);

    SysUser save(SysUser user);

    SysUser update(SysUser user);

    void deleteByIds(Integer ids);
}
