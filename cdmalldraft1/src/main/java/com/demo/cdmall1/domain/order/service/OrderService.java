package com.demo.cdmall1.domain.order.service;

import java.util.*;

import javax.servlet.http.*;

import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import com.demo.cdmall1.domain.order.entity.Cart;
import com.demo.cdmall1.domain.order.entity.DeliveryStatus;
import com.demo.cdmall1.domain.order.entity.Order;
import com.demo.cdmall1.domain.order.entity.OrderAddress;
import com.demo.cdmall1.domain.order.entity.OrderAddressId;
import com.demo.cdmall1.domain.order.entity.OrderAddressRepository;
import com.demo.cdmall1.domain.order.entity.OrderDslRepository;
import com.demo.cdmall1.domain.order.entity.OrderFail;
import com.demo.cdmall1.domain.order.entity.OrderItem;
import com.demo.cdmall1.domain.order.entity.OrderRepository;
import com.demo.cdmall1.domain.product.entity.Product;
import com.demo.cdmall1.domain.product.entity.ProductFail;
import com.demo.cdmall1.domain.product.entity.ProductRepository;
import com.demo.cdmall1.util.ZmallConstant;
import com.demo.cdmall1.util.ZmallUtil;
import com.demo.cdmall1.web.dto.OrderDto;

import lombok.*;

@RequiredArgsConstructor
@Service
public class OrderService {
	private final OrderAddressRepository addressDao;
	private final ProductRepository productDao;
	private final OrderRepository orderDao;
	private final OrderDslRepository orderDslDao;
	
	// 장바구니 주문 처리
	@SuppressWarnings("unchecked")
	public void orderCart(List<Integer> pnos) {
		HttpSession session = ZmallUtil.getSession();
		List<Cart> carts = (List<Cart>)session.getAttribute("carts");
		List<OrderItem> orders = new ArrayList<>();
		for(Cart cart:carts) {
			if(pnos.contains(cart.getPno())) {
				OrderItem orderItem = cart.toOrderItem();
				System.out.println(orderItem);
				orders.add(orderItem);
			}
		}
		session.setAttribute("cart", orders);
	}
	
	// 상품 선택하고 주문 처리
	public void orderProduct(OrderDto.OrderProduct dto) {
		HttpSession session = ZmallUtil.getSession();
		Product product = productDao.findById(dto.getPno()).orElseThrow(ProductFail.ProductNotFoundException::new);
		OrderItem orderItem = OrderItem.builder().pno(dto.getPno()).name(product.getName()).manufacturer(product.getManufacturer()).price(product.getPrice())
			.count(dto.getCount()).orderItemPrice(dto.getCount()*product.getPrice()).image(ZmallConstant.PRODUCT_URL+product.getImage()).build();
		session.setAttribute("product", Arrays.asList(orderItem));
	}

	// select 파라미터가 product면 상품을, cart면  주문 목록을 리턴
	@SuppressWarnings("unchecked")
	public List<OrderItem> getOrders(String select) {
		HttpSession session = ZmallUtil.getSession();
		if(select.equals("product")) {
			// 리턴 타입이 List<OrderItem>이므로 리스트로 변환해 리턴
			return (List<OrderItem>)session.getAttribute("product");
		}
		return (List<OrderItem>)session.getAttribute("cart");
	}
	
	@Transactional
	@SuppressWarnings("unchecked")
	public void payment(String select, Integer addressNo, String loginId) {
		select = select.equals("product") ? "product" : "cart";
		HttpSession session = ZmallUtil.getSession();
		
		List<OrderItem> orders = (List<OrderItem>)session.getAttribute(select);
		System.out.println(select);
		System.out.println(orders);
		
		OrderAddress address = addressDao.findById(new OrderAddressId(loginId, addressNo)).orElseThrow(OrderFail.AddressNotFoundException::new);
		Order order = Order.builder().address(address).deliveryStatus(DeliveryStatus.PAY).build();
		
		// Address.builder().addressNo(1).username("spring").build()와 같이 파라미터를 만들어 넘기면
		// JPA는 그 Address가 존재한다는 사실을 모르므로 새로운 주소라고 판단한다 -> 그런데 CascadeType이 없어서 아래 예외 발생
		// object references an unsaved transient instance - save the transient instance before flushing
		// Order order = Order.builder().address(Address.builder().addressNo(addressNo).username(loginId).build()).build();
		orders.forEach(orderItem->order.addOrderItem(orderItem));
		orderDao.save(order);
	}

	public List<Order> readAll(String loginId) {
		return orderDao.readAll(loginId);
	}

	public List<OrderDto.ReviewAvailable> reviewAvailableList(String loginId) {
		return orderDslDao.reviewAvailableList(loginId);
	}

	public OrderDto.ReviewAvailable getOrderItem(Integer orderNo, Integer orderItemNo, String loginId) {
		return orderDslDao.getOrderItem(orderNo, orderItemNo, loginId);
	}
}
