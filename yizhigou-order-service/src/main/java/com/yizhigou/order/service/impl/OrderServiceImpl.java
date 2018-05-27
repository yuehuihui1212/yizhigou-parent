package com.yizhigou.order.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yizhigou.mapper.TbOrderItemMapper;
import com.yizhigou.mapper.TbOrderMapper;
import com.yizhigou.mapper.TbPayLogMapper;
import com.yizhigou.order.service.OrderService;
import com.yizhigou.pojo.TbOrder;
import com.yizhigou.pojo.TbOrderExample;
import com.yizhigou.pojo.TbOrderItem;
import com.yizhigou.pojo.TbPayLog;
import com.yizhigou.pojogroup.Cart;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import utils.IdWorker;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	private TbOrderItemMapper orderItemMapper;
	@Autowired
	private TbOrderMapper orderMapper;

	@Autowired
	private RedisTemplate redisTemplate;
	@Autowired
	private IdWorker idWorker;

	@Autowired
	private TbPayLogMapper logMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbOrder> findAll() {
		return orderMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbOrder> page=   (Page<TbOrder>) orderMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbOrder order) {
		List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("cartList").get(order.getUserId());
		List<String> payList=new ArrayList<>();
		double total_money=0;
		for (Cart cart : cartList) {
			Long id=idWorker.nextId();
			payList.add(id + "");
			TbOrder tbOrder = new TbOrder();
			tbOrder.setOrderId(id);
			tbOrder.setPaymentType(order.getPaymentType());
			tbOrder.setSellerId(cart.getSellerId());
			tbOrder.setUserId(order.getUserId());
			tbOrder.setReceiverAreaName(order.getReceiverAreaName());
			tbOrder.setReceiver(order.getReceiver());
			tbOrder.setReceiverMobile(order.getReceiverMobile());
			tbOrder.setStatus("1");
			tbOrder.setCreateTime(new Date());
			tbOrder.setUpdateTime(new Date());
			long money=0;
			for (TbOrderItem orderItem : cart.getOrderItemList()) {
				orderItem.setId(idWorker.nextId());
				orderItem.setOrderId(id);
				orderItem.setSellerId(cart.getSellerId());
				money+=orderItem.getTotalFee().longValue();
				orderItemMapper.insert(orderItem);
			}
			total_money+=money;
			tbOrder.setPayment(new BigDecimal(money));
			orderMapper.insert(tbOrder);
		}
		if("1".equals(order.getPaymentType())){
			TbPayLog tbPayLog = new TbPayLog();
			tbPayLog.setOutTradeNo(idWorker.nextId()+"");
			tbPayLog.setCreateTime(new Date());
			tbPayLog.setPayType("1");
			tbPayLog.setTotalFee((long) total_money);
			tbPayLog.setUserId(order.getUserId());
			tbPayLog.setTradeState("0");
			String  orderList= payList.toString();
			orderList = orderList.replace("[", "").replace("]", "").replace(" ", "");
			tbPayLog.setOrderList(orderList);
			logMapper.insert(tbPayLog);
			redisTemplate.boundHashOps("payLog").put(order.getUserId(),tbPayLog);
		}
		redisTemplate.boundHashOps("cartList").delete(order.getUserId());

	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbOrder order){
		orderMapper.updateByPrimaryKey(order);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbOrder findOne(Long id){
		return orderMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			orderMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbOrder order, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbOrderExample example=new TbOrderExample();
		TbOrderExample.Criteria criteria = example.createCriteria();
		
		if(order!=null){			
						if(order.getPaymentType()!=null && order.getPaymentType().length()>0){
				criteria.andPaymentTypeLike("%"+order.getPaymentType()+"%");
			}
			if(order.getPostFee()!=null && order.getPostFee().length()>0){
				criteria.andPostFeeLike("%"+order.getPostFee()+"%");
			}
			if(order.getStatus()!=null && order.getStatus().length()>0){
				criteria.andStatusLike("%"+order.getStatus()+"%");
			}
			if(order.getShippingName()!=null && order.getShippingName().length()>0){
				criteria.andShippingNameLike("%"+order.getShippingName()+"%");
			}
			if(order.getShippingCode()!=null && order.getShippingCode().length()>0){
				criteria.andShippingCodeLike("%"+order.getShippingCode()+"%");
			}
			if(order.getUserId()!=null && order.getUserId().length()>0){
				criteria.andUserIdLike("%"+order.getUserId()+"%");
			}
			if(order.getBuyerMessage()!=null && order.getBuyerMessage().length()>0){
				criteria.andBuyerMessageLike("%"+order.getBuyerMessage()+"%");
			}
			if(order.getBuyerNick()!=null && order.getBuyerNick().length()>0){
				criteria.andBuyerNickLike("%"+order.getBuyerNick()+"%");
			}
			if(order.getBuyerRate()!=null && order.getBuyerRate().length()>0){
				criteria.andBuyerRateLike("%"+order.getBuyerRate()+"%");
			}
			if(order.getReceiverAreaName()!=null && order.getReceiverAreaName().length()>0){
				criteria.andReceiverAreaNameLike("%"+order.getReceiverAreaName()+"%");
			}
			if(order.getReceiverMobile()!=null && order.getReceiverMobile().length()>0){
				criteria.andReceiverMobileLike("%"+order.getReceiverMobile()+"%");
			}
			if(order.getReceiverZipCode()!=null && order.getReceiverZipCode().length()>0){
				criteria.andReceiverZipCodeLike("%"+order.getReceiverZipCode()+"%");
			}
			if(order.getReceiver()!=null && order.getReceiver().length()>0){
				criteria.andReceiverLike("%"+order.getReceiver()+"%");
			}
			if(order.getInvoiceType()!=null && order.getInvoiceType().length()>0){
				criteria.andInvoiceTypeLike("%"+order.getInvoiceType()+"%");
			}
			if(order.getSourceType()!=null && order.getSourceType().length()>0){
				criteria.andSourceTypeLike("%"+order.getSourceType()+"%");
			}
			if(order.getSellerId()!=null && order.getSellerId().length()>0){
				criteria.andSellerIdLike("%"+order.getSellerId()+"%");
			}
	
		}
		
		Page<TbOrder> page= (Page<TbOrder>)orderMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Override
	public TbPayLog searchPayLogFromRedis(String userId) {
		return (TbPayLog) redisTemplate.boundHashOps("payLog").get(userId);

	}

	@Override
	public void updateOrderStatus(String out_trade_no, String transaction_id) {
		TbPayLog payLog = logMapper.selectByPrimaryKey(out_trade_no);
		payLog.setPayTime(new Date());
		payLog.setTransactionId(transaction_id);
		payLog.setTradeState("1");
		logMapper.updateByPrimaryKey(payLog);
		String ids = payLog.getOrderList();
		String[] idss = ids.split(",");
		for (String id : idss) {
			TbOrder order = orderMapper.selectByPrimaryKey(Long.parseLong(id));
			if (order != null) {
				order.setStatus("2");
				order.setUpdateTime(new Date());
				orderMapper.updateByPrimaryKey(order);
			}
		}
		redisTemplate.boundHashOps("payLog").delete(payLog.getUserId());
	}

}
