package com.infy.userservice.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import com.infy.userservice.dto.CartDTO;
import com.infy.userservice.dto.WishlistDTO;
import com.infy.userservice.service.UserService;

@RestController
@CrossOrigin
public class UserServiceController {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private Environment environment;
	
	
	/*****************ORDER RELATED ***************************************/
	
	//move from wishList to cart or adding product to cart
	@PostMapping(value = "api/cart",consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> addToCart(@RequestBody CartDTO cartDTO) {
		logger.info("Adding to cart :"+ cartDTO.toString());
		userService.addToCart(cartDTO);
		ResponseEntity<String> response= new ResponseEntity<>(environment.getProperty("API.ADDING_CART_SUCCESSFUL"),HttpStatus.OK);
		logger.info(response.toString());
		return response;
	}
	
	//move from cart to wishList or add product to wishList
	@PostMapping(value = "api/wishlist",consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> addToWishlist(@RequestBody WishlistDTO wishlistDTO) {
		logger.info("Adding to wishlist :"+wishlistDTO.toString());
		userService.addToWishlist(wishlistDTO);
		ResponseEntity<String> response= new ResponseEntity<>(environment.getProperty("API.ADDING_WISHLIST_SUCCESSFUL"),HttpStatus.OK);
		logger.info(response.toString());
		return response;
	}
	
	//remove particular product from cart
	@DeleteMapping(value = "api/cart")
	public ResponseEntity<String> removeFromCart(@RequestBody CartDTO cartDTO) {
		ResponseEntity<String> response=null;
			logger.info("removing from cart:"+ cartDTO.toString());
			userService.removeFromCart(cartDTO);
			response= new ResponseEntity<>(environment.getProperty("API.REMOVE_CART_SUCCESSFUL"),HttpStatus.OK);
			logger.info(response.toString());
		return response;
		
	}
	
	//checkout from cart for particular buyer
	@DeleteMapping(value = "api/cart/{buyerId}")
	public Boolean checkOutFromCart(@PathVariable Integer buyerId) {
		try {
			logger.info("Checking out from cart for buyer"+buyerId);
			userService.checkOutFromCart(buyerId);
			return true;
		}catch (Exception e) {
			logger.info("No item in cart for buyer: "+buyerId);
			return false;
		}
		
	}
	
	//show cart for particular buyer
	@GetMapping(value= "api/cart/{buyerId}",  produces = MediaType.APPLICATION_JSON_VALUE)
	public List<CartDTO> showCart(@PathVariable Integer buyerId){
		logger.info("list of cart items for buyer :"+buyerId);
		List<CartDTO> cartList=userService.showCart(buyerId);
		logger.info(cartList.toString());;
		return cartList;
		
	}
}
