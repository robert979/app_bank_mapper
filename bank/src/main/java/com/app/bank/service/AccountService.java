package com.app.bank.service;

import com.app.bank.domain.entity.AccountEntity;
import com.app.bank.domain.entity.UserEntity;
import com.app.bank.domain.model.AccountDTO;
import com.app.bank.domain.model.UserDTO;
import com.app.bank.exception.UserNotFindException;
import com.app.bank.mapper.AccountEntityToAccountMapper;
import com.app.bank.mapper.AccountToAccountEntityMapper;
import com.app.bank.repository.AccountRepository;
import com.app.bank.repository.BranchRepository;
import com.app.bank.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AccountService {

    private final AccountRepository repository;

    private final UserRepository userRepository;

    private final AccountEntityToAccountMapper accountEntityToAccountMapper;

    private final AccountToAccountEntityMapper accountToAccountEntityMapper;

    private final BranchRepository branchRepository;

    public List<AccountDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(accountEntityToAccountMapper::convert)
                .collect(Collectors.toList());
    }

    public AccountDTO createAccount(AccountDTO accountDTO, long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFindException("The user with the id " + userId + " does not exists"));
        AccountEntity accountEntity = accountToAccountEntityMapper.convert(accountDTO);
        accountEntity.setUserCnp(userEntity.getCnp());
        AccountEntity accountToBeSaved = repository.save(accountEntity);
        return accountEntityToAccountMapper.convert(accountToBeSaved);

    }

    public AccountDTO findAccountByIban(String iban) {
        AccountEntity accountEntity = repository.findById(branchRepository.findIdByIban(iban))
                .orElseThrow(() -> new RuntimeException("The iban " + iban + " is not in use;"));

        return accountEntityToAccountMapper.convert(accountEntity);
    }

    public List<AccountDTO> findAllAccountsByCnp(long cnp) {
        List<AccountDTO> listAccounts = new ArrayList<>();
        List<Long> listIdCnp = branchRepository.findAllAccountsByCnp(cnp);
        if (listIdCnp.size()!=0) {
            for (long l : listIdCnp) {
                listAccounts.add(accountEntityToAccountMapper.convert(repository.findById(l)
                        .orElseThrow(() -> new RuntimeException("There is no user with the cnp " + cnp))));
            }

        } else {
            System.out.println("Invalid CNP");
            return null;
        }
        return listAccounts;
    }


}
