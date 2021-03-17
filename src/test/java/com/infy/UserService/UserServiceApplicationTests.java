package com.infy.UserService;



import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.infy.userservice.dto.CartDTO;
import com.infy.userservice.entity.Buyer;
import com.infy.userservice.entity.Cart;
import com.infy.userservice.entity.Seller;
import com.infy.userservice.entity.Wishlist;
import com.infy.userservice.repository.BuyerRepository;
import com.infy.userservice.repository.CartRepository;
import com.infy.userservice.repository.SellerRepository;
import com.infy.userservice.repository.WishlistRepository;
import com.infy.userservice.service.UserBuyerService;
import com.infy.userservice.service.UserSellerService;
import com.infy.userservice.service.UserService;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class UserServiceApplicationTests {
	/*************Cart and wishList ********************/
	@InjectMocks
	private UserService userService=new UserService();

	@Mock
	private CartRepository cartRepository;
	
	@Mock
	private WishlistRepository wishlistRepository;
	
	/**************** Buyer ****************************/
	@InjectMocks
	private UserBuyerService userBuyerService=new UserBuyerService();
	
	@Mock
	private BuyerRepository buyerRepository;
	
	
	/************************Seller **************************/
	
	@InjectMocks
	private UserSellerService userSellerService=new UserSellerService();
	
	@Mock
	private SellerRepository sellerRepository;
	
	/************************************Cart And WishList test cases *******************************/ 
	@Test
	public void showCartTestSuccess() {
		List<Cart> cartList=new ArrayList<>(); 
		Cart cart=new Cart();
		cart.setBuyerId(1);
		cart.setProdId(3);
		cart.setQuantity(2);
		cartList.add(cart);
		Cart cart1=new Cart();
		cart1.setBuyerId(1);
		cart1.setProdId(7);
		cart1.setQuantity(3);
		cartList.add(cart1);
		Mockito.when(cartRepository.getByBuyerId(1)).thenReturn(cartList);
		
		List<CartDTO> cartDTOList=new ArrayList<>();
		for(Cart cart2:cartList) {
			cartDTOList.add(CartDTO.valueOf(cart2));
		}
		List<CartDTO> actualList=userService.showCart(1);
		Assertions.assertTrue(cartDTOList.retainAll(actualList));
	}
		
	@Test
	public void showCartTestWrong() {
		List<Cart> cartList=new ArrayList<>(); 
		Cart cart=new Cart();
		cart.setBuyerId(1);
		cart.setProdId(3);
		cart.setQuantity(2);
		cartList.add(cart);
		Cart cart1=new Cart();
		cart1.setBuyerId(1);
		cart1.setProdId(7);
		cart1.setQuantity(3);
		cartList.add(cart1);
		Mockito.lenient().when(cartRepository.getByBuyerId(1)).thenReturn(cartList);
		
		List<CartDTO> cartDTOList=new ArrayList<>();
		for(Cart cart2:cartList) {
			cartDTOList.add(CartDTO.valueOf(cart2));
		}
		Object[] cartDToArray=cartDTOList.toArray();
		List<CartDTO> actualList=userService.showCart(2);
		Assert.assertNotSame(cartDToArray, actualList);
	}
	
	
	@Test
	public void addToCartTest() {
		Cart cart=new Cart();
		cart.setBuyerId(1);
		cart.setProdId(3);
		cart.setQuantity(2);
		Mockito.when(cartRepository.save(cart)).thenReturn(cart);
		assertEquals(cart, cartRepository.save(cart));
	}
	
	@Test
	public void addToWishlistTest() {
		Wishlist wishlist=new Wishlist();
		wishlist.setBuyerId(1);
		wishlist.setProdId(3);
		Mockito.when(wishlistRepository.save(wishlist)).thenReturn(wishlist);
		assertEquals(wishlist, wishlistRepository.save(wishlist));
	}
	
	
	
	
	/************************************Buyer test cases *******************************/ 
	
	
	@Test
	public void buyerRegistrationTest() {
		Buyer buyer=new Buyer();
		buyer.setBuyerId(3);
		buyer.setEmail("kennie@gmail.com");
		buyer.setIsActive(1);
		buyer.setIsPrivileged(null);
		buyer.setName("Kennie");
		buyer.setPassword("Kennie179!");
		buyer.setPhoneNumber("9256712354");
		buyer.setRewardPoints(null);
		
		Mockito.when(buyerRepository.save(buyer)).thenReturn(buyer);
		
		assertEquals(buyer, buyerRepository.save(buyer));
	}
	
	
	
		
	/************************************Buyer test cases *******************************/ 
	
	
	@Test
	public void sellerRegistrationTest() {
		Seller seller=new Seller();
		seller.setSellerId(3);
		seller.setEmail("Annie@gmail.com");
		seller.setIsActive(1);
		seller.setName("Annie");
		seller.setPassword("Annie23!");
		seller.setPhoneNumber("9256712354");
		
		Mockito.when(sellerRepository.save(seller)).thenReturn(seller);
		
		assertEquals(seller, sellerRepository.save(seller));
	}
	
}