package com.okan.bankingmanagement.utility;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.okan.bankingmanagement.domain.Account;
import com.okan.bankingmanagement.dto.response.AccountDetailResponse;
import com.okan.bankingmanagement.repository.BankRepository;

@Component
public class UtilMapper {
	
	private final BankRepository bankRepo;
	
	@Autowired
	public UtilMapper(BankRepository bankRepo) {
		this.bankRepo = bankRepo;
	}



	public List<AccountDetailResponse> detailAccount(List<Account> acc) {
		List<AccountDetailResponse> list = new ArrayList<AccountDetailResponse>();
		
		for (Account ac : acc) {
			AccountDetailResponse temp = new AccountDetailResponse();
			temp.setAccount_number(ac.getAccount_number());
			temp.setBalance(ac.getBalance());
			temp.setCreation_date(ac.getCreation_date());
			temp.setLast_update_date(ac.getLast_update_date());
			temp.setType(ac.getType());
			temp.setBank(bankRepo.getBank(ac.getBank().getId()));
			temp.setUser(ac.getUser());
			if(!ac.isIs_deleted()) {
				list.add(temp);
			}
			
		}
		return list;	
	}

}
