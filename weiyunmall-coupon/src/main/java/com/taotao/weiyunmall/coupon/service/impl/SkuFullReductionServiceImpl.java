package com.taotao.weiyunmall.coupon.service.impl;

import com.taotao.common.to.MemberPrice;
import com.taotao.common.to.SkuReductionTo;
import com.taotao.weiyunmall.coupon.entity.MemberPriceEntity;
import com.taotao.weiyunmall.coupon.entity.SkuLadderEntity;
import com.taotao.weiyunmall.coupon.service.MemberPriceService;
import com.taotao.weiyunmall.coupon.service.SkuLadderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.common.utils.PageUtils;
import com.taotao.common.utils.Query;

import com.taotao.weiyunmall.coupon.dao.SkuFullReductionDao;
import com.taotao.weiyunmall.coupon.entity.SkuFullReductionEntity;
import com.taotao.weiyunmall.coupon.service.SkuFullReductionService;


@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {

    @Autowired
    SkuLadderService skuLadderService;

    @Autowired
    MemberPriceService memberPriceService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<SkuFullReductionEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuReductionTo(SkuReductionTo reductionTo) {
        //5.4）、保存sku的优惠、满减等信息：
        //这部操作需要跨越数据库：
        //sms_sku_ladder：打折表
        //sms_sku_full_reduction：满减表
        //sms_member_price：会员等级对应的价格
        SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
        skuLadderEntity.setSkuId(reductionTo.getSkuId());
        skuLadderEntity.setFullCount(reductionTo.getFullCount());
        skuLadderEntity.setDiscount(reductionTo.getDiscount());
        skuLadderEntity.setAddOther(reductionTo.getCountStatus());
        if(reductionTo.getFullCount() > 0) {
            skuLadderService.save(skuLadderEntity);
        }

        //sms_sku_full_reduction：满减表
        SkuFullReductionEntity reductionEntity = new SkuFullReductionEntity();
        BeanUtils.copyProperties(reductionTo,reductionEntity);
        if(reductionEntity.getFullPrice().compareTo(new BigDecimal("0")) == 1) {
            this.save(reductionEntity);
        }


        //sms_member_price：会员等级对应的价格
        List<MemberPrice> memberPrice = reductionTo.getMemberPrice();
        if(memberPrice != null && memberPrice.size() != 0) {
            List<MemberPriceEntity> memberPriceEntities = memberPrice.stream().map((item) -> {
                MemberPriceEntity memberPriceEntity = new MemberPriceEntity();
                memberPriceEntity.setSkuId(reductionTo.getSkuId());
                memberPriceEntity.setMemberLevelId(item.getId());
                memberPriceEntity.setMemberLevelName(item.getName());
                memberPriceEntity.setMemberPrice(item.getPrice());
                memberPriceEntity.setAddOther(1);
                return memberPriceEntity;
            }).filter((item)->{
                return item.getMemberPrice().compareTo(new BigDecimal("0")) == 1;
            }).collect(Collectors.toList());
            memberPriceService.saveBatch(memberPriceEntities);
        }



    }

}