package com.demo.cdmall1.domain.order.service;

import java.util.*;
import java.util.stream.*;

import javax.servlet.http.*;

import org.springframework.stereotype.Service;

import com.demo.cdmall1.domain.order.entity.Cart;
import com.demo.cdmall1.domain.product.entity.ProductFail;
import com.demo.cdmall1.domain.product.entity.ProductRepository;
import com.demo.cdmall1.util.ZmallUtil;

import lombok.*;

@RequiredArgsConstructor
@Service
public class CartService {
	private final ProductRepository dao;
	
	@SuppressWarnings("unchecked")
	private List<Cart> getCarts() {
		HttpSession session = ZmallUtil.getSession();
		if(session.getAttribute("carts")!=null) 
			return (List<Cart>)session.getAttribute("carts");
		return new ArrayList<>();
	}
	
	private void saveCarts(List<Cart> carts) {
		HttpSession session = ZmallUtil.getSession();
		session.setAttribute("carts", carts);
	}

	public void add(Cart cart) {
		List<Cart> carts = getCarts();
		boolean isExist = false;
		for(Cart c:carts) {
			if(c.getPno()==cart.getPno()) {
				c.increase();
				isExist = true;
			}
		}
		if(isExist==false)
			carts.add(cart);
		
		saveCarts(carts);
	}

	public List<Cart> read() {
		return getCarts();
	}

	public List<Cart> increase(Integer pno) {
		List<Cart> carts = getCarts();
		for(Cart cart:carts) {
			if(cart.getPno()==pno) {
				Integer count = cart.getCount()+1;
				Integer stock = dao.readStock(pno);
				if(count>=stock)
					throw new ProductFail.OutOfStockException();
				cart.increase();
			}
		}
		saveCarts(carts);
		return carts;
	}

	public List<Cart> decrease(Integer pno) {
		List<Cart> carts = getCarts();
		for(Cart cart:carts) {
			if(cart.getPno()==pno)
				cart.decrease();
		}
		saveCarts(carts);
		return carts;
	}

	public List<Cart> delete(List<Integer> pnos) {
		List<Cart> carts = getCarts();
		List<Cart> newCarts = carts.stream().filter(cart->pnos.contains(cart.getPno())==false).collect(Collectors.toList());
		saveCarts(newCarts);
		return newCarts;
	}

	public String getUrl() {
		HttpSession session = ZmallUtil.getSession();
		
		return  ((String)session.getAttribute("saved_url"));
	}
}

