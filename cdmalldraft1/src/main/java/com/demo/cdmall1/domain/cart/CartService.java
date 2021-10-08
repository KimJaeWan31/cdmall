package com.demo.cdmall1.domain.cart;

import javax.servlet.http.*;

import org.springframework.stereotype.*;

import com.demo.cdmall1.domain.product.entity.*;

import lombok.*;

@RequiredArgsConstructor
@Service
public class CartService {
	private final ProductRepository dao;

	// 임시 장바구니를 읽어오는 함수
	private Cart getTempCart(HttpSession session) {
		if(session.getAttribute("tempCart")!=null)
			return (Cart)session.getAttribute("tempCart");
		return null;
	}
	
	// 임시 장바구니를 저장하는 함수
	private Cart saveTempCart(Cart tempCart, HttpSession session) {
		session.setAttribute("tempCart", tempCart);
		return tempCart;
	}
	
	public Cart getCart(Integer pno, HttpSession session) {
		Cart tempCart = getTempCart(session);
		if(tempCart!=null && tempCart.getProductNo()!=pno) {
			session.removeAttribute("tempCart");
			return null;
		}
		return tempCart;
	}
	

	
	public Cart add(Integer pno, Integer optionNo, HttpSession session) {
		Cart tempCart = getTempCart(session);
		
		// 상품만 선택한 경우
		if(optionNo==null) {
			if(tempCart==null || tempCart.getProductNo()!=pno) {
				Product product = dao.findById(pno).get();
				return saveTempCart(new Cart(product), session);
			}
		}
		
		// 옵션을 선택한 경우
		else {
			Product product = dao.findById(pno).get();
			// 새 임시장바구니를 만들어야 하는 경우
			if(tempCart==null || tempCart.getProductNo()!=pno) {
				return saveTempCart(new Cart(product, product.getOptions(), optionNo), session);
			}
			
			// 기존 장바구니에 옵션을 추가하는 경우
			else if(tempCart!=null && tempCart.getProductNo()==pno) {
				// i5가 존재하는데 i5를 다시 추가하면 -> 예외, i7을 선택한다면 추가 => 옵션이 존재하는 지 검색하는 함수가 필요
				if(tempCart.contains(optionNo)==true)
					throw new CartFail.AlreadyExistException();
				return saveTempCart(tempCart.addOption(product.getOptions(), optionNo, product.getPrice()), session);
					
			}
		}
		return tempCart;
	}

	public Cart increase(Integer pno, HttpSession session) {
		Cart tempCart = getTempCart(session); 
		if(tempCart!=null  && tempCart.getProductNo()!=pno)
			throw new CartFail.IllegalJobException();
		return saveTempCart(tempCart.increase(), session);
	}
	
	public Cart decrease(Integer pno, HttpSession session) {
		Cart tempCart = getTempCart(session); 
		if(tempCart!=null  && tempCart.getProductNo()!=pno)
			throw new CartFail.IllegalJobException();
		return saveTempCart(tempCart.decrease(), session);
	}

	public Cart increaseOption(Integer pno, Integer optionNo, HttpSession session) {
		Cart tempCart = getTempCart(session); 
		if(tempCart!=null  && tempCart.getProductNo()!=pno)
			throw new CartFail.IllegalJobException();
		return saveTempCart(tempCart.increaseOption(optionNo), session);
	}

	public Cart decreaseOption(Integer pno, Integer optionNo, HttpSession session) {
		Cart tempCart = getTempCart(session); 
		if(tempCart!=null  && tempCart.getProductNo()!=pno)
			throw new CartFail.IllegalJobException();
		return saveTempCart(tempCart.decreaseOption(optionNo), session);
	}

	public Cart deleteOption(Integer pno, Integer optionNo, HttpSession session) {
		Cart tempCart = getTempCart(session); 
		if(tempCart!=null  && tempCart.getProductNo()!=pno)
			throw new CartFail.IllegalJobException();
		return saveTempCart(tempCart.deleteOption(optionNo), session);
	}	
}





