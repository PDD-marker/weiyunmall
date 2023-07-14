package com.taotao.weiyunmall.member.dao;

import com.taotao.weiyunmall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author guyu
 * @email ljt10086123@gmail.com
 * @date 2023-05-30 10:54:42
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
