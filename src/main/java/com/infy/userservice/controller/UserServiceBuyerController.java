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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.infy.userservice.dto.BuyerDTO;
import com.infy.userservice.dto.LoginDTO;
import com.infy.userservice.service.UserBuyerService;

@RestController
@CrossOrigin
public class UserServiceBuyerController {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserBuyerService userBuyerService;
	
	@Autowired
	private Environment environment;
	
	/*****************BUYER RELATED ***************************************/
	
	//Buyer login
	@PostMapping(value = "api/buyer/login",consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
		boolean islogin=userBuyerService.login(loginDTO);
		System.out.println(islogin);
		if(islogin) {
			logger.info("logging in for"+ loginDTO.toString());
			ResponseEntity<String> response= new ResponseEntity<>(environment.getProperty("API.BUYER_LOGIN"),HttpStatus.OK);
			logger.info(response.toString());
			return response;
		}
		ResponseEntity<String> response= new ResponseEntity<>(environment.getProperty("API.WRONG_CREDENTIALS"),HttpStatus.BAD_REQUEST);
		logger.info(response.toString());
		return response;
		
	}
	
	//registering a buyer
	@PostMapping(value = "api/buyer/register",consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> registerBuyer(@RequestBody BuyerDTO buyerDTO)throws Exception{
		try {
			logger.info("Registering buyer :"+ buyerDTO.toString());
			userBuyerService.buyerRegistration(buyerDTO);
			ResponseEntity<String> response= new ResponseEntity<>(environment.getProperty("API.BUYER_REGISTRATION"),HttpStatus.CREATED);
			logger.info(response.toString());
			return response;
		}catch (Exception e) {
			logger.warn("Exception occurs");
			ResponseEntity<String> response= new ResponseEntity<>(environment.getProperty(e.getMessage()),HttpStatus.BAD_REQUEST);
			logger.info(response.toString());
			return response;
		}
	}
	
	//get all the buyers
	@GetMapping(value= "api/buyer",  produces = MediaType.APPLICATION_JSON_VALUE)
	public List<BuyerDTO> showBuyers(){
		logger.info("fetching buyers list");
		List<BuyerDTO> buyers=userBuyerService.showBuyers();
		logger.info(buyers.toString());
		return buyers;
	}
	
	//removal of buyer
	@PutMapping(value="api/buyer/{buyerId}")
	public ResponseEntity<String> removeBuyer(@PathVariable Integer buyerId) throws Exception{
		try {
			logger.info("Removing buyer: "+buyerId);
			userBuyerService.removeBuyer(buyerId);
			ResponseEntity<String> response= new ResponseEntity<>(environment.getProperty("Service.BUYER_REMOVAL"),HttpStatus.OK);
			logger.info(response.toString());
			return response;	
		}catch (Exception e) {
			logger.info("Exception occurs");
			ResponseEntity<String> response= new ResponseEntity<>(environment.getProperty("Service.BUYER_REMOVAL"),HttpStatus.BAD_REQUEST);
			logger.info(response.toString());
			return response;
		}
		
	}
	
	//update reward point
	@PutMapping(value="api/buyer/{buyerId}/rewardpoints/{amount}")
	public ResponseEntity<String> updateRewardPoints(@PathVariable Integer buyerId, @PathVariable Double amount){
		if(userBuyerService.updateRewardPoints(buyerId, amount)) {
			logger.info("Updating rewards for buyer:"+buyerId);
			ResponseEntity<String> response= new ResponseEntity<>(environment.getProperty("Service.BUYER_REWARDS_UPDATE"),HttpStatus.OK);
			logger.info(response.toString());
		    return response;
		}
		ResponseEntity<String> response= new ResponseEntity<>(environment.getProperty("Service.BUYER_REWARDS_UPDATE_FAIL"),HttpStatus.OK);
		logger.info(response.toString());
	    return response;
	}
	
	//use reward points for discount
	@GetMapping(value="api/buyer/{buyerId}/discount/{amount}")
	public Double useRewardPoints(@PathVariable Integer buyerId,@PathVariable Double amount){
		return userBuyerService.useRewardPoints(buyerId,amount);
		
		
	}
	
	//set isPrivileged
	@PutMapping(value = "api/buyer/{buyerId}/privileged")
	public Boolean isPrivileged(@PathVariable Integer buyerId){
		logger.info("setting privilege for buyer: "+ buyerId);
		return userBuyerService.isPrivileged(buyerId);
		
	}
	
	//get isPriviledged
	@GetMapping(value="api/buyer/{buyerId}/privileged")
	public Boolean getisPrivileged(@PathVariable Integer buyerId) {
		logger.info("getting privilege for buyer: "+ buyerId);
		return userBuyerService.getisPrivileged(buyerId);
	}
	
}
