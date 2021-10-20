package com.demo.cdmall1.domain.order.entity;


import lombok.*;

@Data
@AllArgsConstructor
@Builder
public class Cart {
	private Integer pno;
	private String name;
	private String manufacturer;
	private Integer price;
	private Integer count;
	private String image;
	private Integer cartPrice;
	
	public void increase() {
		this.count++;
		this.cartPrice = this.count * this.price;
	}
	
	public void decrease() {
		if(this.count>1)
			this.count--;
		this.cartPrice = this.count * this.price;
	}

	public OrderItem toOrderItem() {
		return OrderItem.builder().pno(pno).name(name).manufacturer(manufacturer).price(price).count(count).orderItemPrice(cartPrice).image(image).build();
	}
	
	
	
	
	/*
	 * private Integer productNo; private String manufacturer; private String name;
	 * private Integer price; private Integer count; private Integer cartPrice;
	 * private List<CartItem> cartItemList;
	 * 
	 * // 상품을 선택한 경우 상품 정보를 Cart로 복사 public Cart(Product product) { this.productNo =
	 * product.getPno(); this.manufacturer = product.getManufacturer(); this.name =
	 * product.getName(); this.price = product.getPrice(); this.count = 1;
	 * this.cartPrice = price; }
	 * 
	 * // 옵션 선택 -> 상품과 옵션 정보를 읽어와서 Cart에는 상품 정보+옵션들의 합계, CartItem에는 옵션 정보를 복사.
	 * public Cart(Product product, Set<Option> options, Integer optionNo) {
	 * this.productNo = product.getPno(); this.manufacturer =
	 * product.getManufacturer(); this.name = product.getName(); this.price =
	 * product.getPrice(); this.count = 1; this.cartItemList=new ArrayList<>();
	 * for(Option option:options) { if(option.getOptionNo()==optionNo) { CartItem
	 * item = new CartItem(optionNo, option.getName(), option.getInfo(),
	 * product.getPrice()+option.getPrice(), 1,
	 * product.getPrice()+option.getPrice()); this.cartItemList.add(item);
	 * calcCartPrice(); } } }
	 * 
	 * // 옵션들의 가격 합계를 cartPrice에 누적하는 함수 private void calcCartPrice() {
	 * this.cartPrice = 0; for(CartItem item:this.cartItemList) {
	 * System.out.println(item); this.cartPrice += item.getItemPrice(); } }
	 * 
	 * private void calcCartCount() { this.count = 0; for(CartItem
	 * item:this.cartItemList) { this.count += item.getCount(); } }
	 * 
	 * 
	 * public Cart increase() { this.count++; this.cartPrice = this.count *
	 * this.price; return this; }
	 * 
	 * public Cart decrease() { if(this.count>1) this.count--; this.cartPrice =
	 * this.count * this.price; return this; }
	 * 
	 * // 이미 장바구니에 담긴 옵션인지 확인 public boolean contains(Integer optionNo) {
	 * for(CartItem item:this.cartItemList) { if(item.getOptionNo()==optionNo)
	 * return true; } return false; }
	 * 
	 * public Cart addOption(Set<Option> options, Integer optionNo, Integer
	 * basePrice) { for(Option option:options) { if(option.getOptionNo()==optionNo)
	 * { CartItem item = new CartItem(optionNo, option.getName(), option.getInfo(),
	 * basePrice+option.getPrice(), 1, basePrice+option.getPrice());
	 * this.cartItemList.add(item); calcCartCount(); calcCartPrice(); } } return
	 * this; }
	 * 
	 * public Cart increaseOption(Integer optionNo) { for(int i=0;
	 * i<this.cartItemList.size(); i++) {
	 * if(this.cartItemList.get(i).getOptionNo()==optionNo) {
	 * this.cartItemList.get(i).increase(); calcCartCount(); calcCartPrice(); } }
	 * return this; }
	 * 
	 * public Cart decreaseOption(Integer optionNo) { for(int i=0;
	 * i<this.cartItemList.size(); i++) {
	 * if(this.cartItemList.get(i).getOptionNo()==optionNo) {
	 * this.cartItemList.get(i).decrease(); calcCartCount(); calcCartPrice(); } }
	 * return this; }
	 * 
	 * public Cart deleteOption(Integer optionNo) { // 리스트에서 원소를 삭제하면 삭제한 원소 뒤에 있는
	 * 원소들의 인덱스가 모두 하나씩 땡겨진다. 따라서 조건을 만족하는데 삭제안되는 값들이 있을 수 있다 for(int i=0;
	 * i<this.cartItemList.size(); i++ ) {
	 * if(this.cartItemList.get(i).getOptionNo()==optionNo) {
	 * this.cartItemList.remove(i); break; } }
	 * System.out.println(cartItemList.size()); if(this.cartItemList.size()>=0) {
	 * calcCartCount(); calcCartPrice(); } return this; }
	 */
}






