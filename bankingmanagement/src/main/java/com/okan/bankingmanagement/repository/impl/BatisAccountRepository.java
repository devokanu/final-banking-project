package com.okan.bankingmanagement.repository.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.okan.bankingmanagement.domain.Account;
import com.okan.bankingmanagement.exception.InvalidAccountTypeException;
import com.okan.bankingmanagement.mybatis.AccountMapper;
import com.okan.bankingmanagement.repository.AccountRepository;

@Repository
public class BatisAccountRepository implements AccountRepository{
	
	private final AccountMapper accountMapper;
	
	@Autowired
	public BatisAccountRepository(AccountMapper accountMapper) {
		this.accountMapper = accountMapper;
	}

	@Override
	public Account createAccount(Account account) throws InvalidAccountTypeException {
		accountMapper.save(account);
		return account;
	}

	@Override
    public Account updateAccount(Account account) {
        if(accountMapper.update(account)) {
            return account;
        }
        return null;
    }

    @Override
    public Account getAccount(int id) {
        return accountMapper.findById(id);
    }
    
    @Override
    public Account getAccountByAccountNumber(int id) {
    	Account acc = accountMapper.findByAccountNumber(id);
        return acc;
    }
    
    

    @Override
    public boolean deleteAccount(Account account) {
    	Account acc = new Account();
    	acc.setAccount_number(account.getAccount_number());
    	acc.setIs_deleted(account.isIs_deleted());
    	acc.setLast_update_date(account.getLast_update_date());
       
        Account updatedAccount = updateAccount(account);

        return updatedAccount != null && updatedAccount.isIs_deleted();
    }

	@Override
	public List<Account> getAccounts(int user_id) {
		List<Account> accounts = accountMapper.getAccounts(user_id);
		return accounts;
	}
}
