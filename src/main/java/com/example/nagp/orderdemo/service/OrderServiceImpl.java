package com.example.nagp.orderdemo.service;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
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
		
		jmsTemplate.convertAndSend("OrderRequestReceivedEvent", order.getOrderID());
		
		return order;
	}

	@Override
	public Order getOrder(String orderId) {
		System.out.print("Order"+ this.orders.get(orderId));
		return this.orders.get(orderId);
	}
	
	
	@Override
	@JmsListener(destination="InventoryAvaliableEvent")
	public void confirmOrder(String id) {
		System.out.println("InventoryAvaliableEvent");
		Order order = this.orders.get(id);
		order.setOrderStatus(OrderStatus.CONFIRMED);
		this.orders.put(order.getOrderID(), order);		
		//jmsTemplate.convertAndSend("CreateOrderShippingEvent", order.getOrderID());
		
	}
	
	@Override
	@JmsListener(destination="InventoryNotAvaliableEvent")
	public void UnConfirmOrder(String id) {
		System.out.println("InventoryNotAvaliableEvent");
		Order order = this.orders.get(id);
		order.setOrderStatus(OrderStatus.UNCONFIRMED);
		order.setRemarks("Product is out of stock");
		this.orders.put(order.getOrderID(), order);
		//jmsTemplate.convertAndSend("ProductNotAvaialbleEvent", order.getOrderID());
		
	}


}
