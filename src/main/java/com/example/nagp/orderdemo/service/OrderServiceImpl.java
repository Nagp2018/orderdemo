package com.example.nagp.orderdemo.service;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.example.nagp.orderdemo.model.Order;
import com.example.nagp.orderdemo.model.OrderStatus;


@Service
public class OrderServiceImpl implements OrderService {

	private  Map<String, Order> orders  = new ConcurrentHashMap<String, Order>();
	
	@Autowired 
	private JmsTemplate jmsTemplate;
	
	@Override
	public Order addOrder(Order order) {
		if(Objects.isNull(order.getOrderID())){
			order.setorderID(UUID.randomUUID().toString());
		}
		order.setCreationTimeStamp(Instant.now());
		order.setOrderStatus(OrderStatus.PROCESSING);	
		this.orders.put(order.getOrderID(), order);	
		System.out.print("Order"+ this.orders);
		
		//jmsTemplate.convertAndSend("OrderRequestReceivedEvent", order.getOrderID());
		
		return order;
	}

	@Override
	public Order getOrder(String orderId) {
		System.out.print("Order"+ this.orders.get(orderId));
		return this.orders.get(orderId);
	}

}
