package com.infy.userservice.controller;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.infy.userservice.dto.LoginDTO;
import com.infy.userservice.dto.SellerDTO;
import com.infy.userservice.service.UserSellerService;

@RestController
@CrossOrigin
public class UserServiceSellerController {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Value("${Product.uri}")
	String productUri;
	
	@Autowired
	private UserSellerService UserSellerService;
	
	@Autowired
	private Environment environment;
	
	/*****************SELLER RELATED ***************************************/
	
	//Buyer login
	@PostMapping(value = "api/seller/login",consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
		logger.info("Login for seller :"+loginDTO.toString());
		boolean islogin=UserSellerService.login(loginDTO);
		if(islogin) {
			ResponseEntity<String> response= new ResponseEntity<>(environment.getProperty("API.SELLER_LOGIN"),HttpStatus.OK);
			logger.info(response.toString());
			return response;
		}
		ResponseEntity<String> response= new ResponseEntity<>(environment.getProperty("API.WRONG_CREDENTIALS"),HttpStatus.BAD_REQUEST);
		logger.info(response.toString());
		return response;
			
		}
	
	//registering a seller
	@PostMapping(value = "api/seller/register",consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> registerSeller(@RequestBody SellerDTO sellerDTO)throws Exception{
		logger.info("Registering for seller :"+ sellerDTO);
		try {
			UserSellerService.sellerRegistration(sellerDTO);
			ResponseEntity<String> response= new ResponseEntity<>(environment.getProperty("API.SELLER_REGISTRATION"),HttpStatus.CREATED);
			logger.info(response.toString());
			return response;
		}catch (Exception e) {
			ResponseEntity<String> response= new ResponseEntity<>(environment.getProperty(e.getMessage()),HttpStatus.BAD_REQUEST);
			logger.info(response.toString());
			return response;
			}
	}
	
	//get all the sellers
	@GetMapping(value= "api/seller",  produces = MediaType.APPLICATION_JSON_VALUE)
	public List<SellerDTO> showSellers(){
		logger.info("showing list of sellers");
		List<SellerDTO> sellers=UserSellerService.showSellers();
		logger.info(sellers.toString());
		return sellers;
	}
	
	//removal of seller
	@PutMapping(value="api/seller/{sellerId}")
	public ResponseEntity<String> removeSeller(@PathVariable Integer sellerId) throws Exception{
		logger.info("setting seller inactive for :"+sellerId);
		try {
			UserSellerService.removeSeller(sellerId);
			String url=productUri+sellerId;
			RestTemplate restTemplate=new RestTemplate();
			logger.info("hitting url :"+ url);
			restTemplate.delete(url,sellerId);
			System.out.println("deleted");
			ResponseEntity<String> response= new ResponseEntity<>(environment.getProperty("Service.SELLER_REMOVAL"),HttpStatus.OK);
			logger.info(response.toString());
			return response;	
			}catch (Exception e) {
				logger.info("Exception occurs");
				UserSellerService.removeSeller(sellerId);
				ResponseEntity<String> response= new ResponseEntity<>(environment.getProperty("NO_ITEMS"),HttpStatus.BAD_REQUEST);
				logger.info(response.toString());
				return response;
			}
			
		}

}
