package ma.yassine.digitalbanking.mappers;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import ma.yassine.digitalbanking.dtos.BankAccountDTO;
import ma.yassine.digitalbanking.dtos.CurrentBankAccountDTO;
import ma.yassine.digitalbanking.dtos.CustomerDTO;
import ma.yassine.digitalbanking.dtos.OperationDTO;
import ma.yassine.digitalbanking.dtos.SavingBankAccountDTO;
import ma.yassine.digitalbanking.entities.BankAccount;
import ma.yassine.digitalbanking.entities.CurrentAccount;
import ma.yassine.digitalbanking.entities.Customer;
import ma.yassine.digitalbanking.entities.Operation;
import ma.yassine.digitalbanking.entities.SavingAccount;

@Service
public class AppMapper implements IAppMapper {

	@Override
	public Customer toCustomer(CustomerDTO customerDTO) {
		Customer customer = new Customer();
		BeanUtils.copyProperties(customerDTO, customer);
		return customer;
	}

	@Override
	public CustomerDTO toCustomerDTO(Customer customer) {
		CustomerDTO customerDTO = new CustomerDTO();
		BeanUtils.copyProperties(customer, customerDTO);
		return customerDTO;
	}

	@Override
	public CurrentBankAccountDTO toCurrentBankAccountDTO(CurrentAccount currentBankAccount) {
		CurrentBankAccountDTO currentBankAccountDTO = new CurrentBankAccountDTO();
		BeanUtils.copyProperties(currentBankAccount, currentBankAccountDTO);
		currentBankAccountDTO.setCustomerDTO(toCustomerDTO(currentBankAccount.getCustomer()));
		return currentBankAccountDTO;
	}

	@Override
	public CurrentAccount toCurrentBankAccount(CurrentBankAccountDTO currentBankAccountDTO) {
		CurrentAccount currentBankAccount = new CurrentAccount();
		BeanUtils.copyProperties(currentBankAccountDTO, currentBankAccount);
		currentBankAccount.setCustomer(toCustomer(currentBankAccountDTO.getCustomerDTO()));
		return currentBankAccount;
	}

	@Override
	public SavingBankAccountDTO toSavingBankAccountDTO(SavingAccount savingBankAccount) {
		SavingBankAccountDTO savingBankAccountDTO = new SavingBankAccountDTO();
		BeanUtils.copyProperties(savingBankAccount, savingBankAccountDTO);
		savingBankAccountDTO.setCustomerDTO(toCustomerDTO(savingBankAccount.getCustomer()));
		return savingBankAccountDTO;
	}

	@Override
	public SavingAccount toSavingBankAccount(SavingBankAccountDTO savingBankAccountDTO) {
		SavingAccount savingBankAccount = new SavingAccount();
		BeanUtils.copyProperties(savingBankAccountDTO, savingBankAccount);
		savingBankAccount.setCustomer(toCustomer(savingBankAccountDTO.getCustomerDTO()));
		return savingBankAccount;
	}

	@Override
	public BankAccountDTO toBankAccountDTO(BankAccount bankAccount) {
		BankAccountDTO bankAccountDTO = new BankAccountDTO();
		BeanUtils.copyProperties(bankAccount, bankAccountDTO);
		bankAccountDTO.setCustomerDTO(toCustomerDTO(bankAccount.getCustomer()));
		return bankAccountDTO;
	}

	@Override
	public BankAccount toBankAccount(BankAccountDTO bankAccountDTO) {
		BankAccount bankAccount = new BankAccount();
		BeanUtils.copyProperties(bankAccountDTO, bankAccount);
		bankAccount.setCustomer(toCustomer(bankAccountDTO.getCustomerDTO()));
		return bankAccount;
	}

	@Override
	public OperationDTO toOperationDTO(Operation operation) {
		OperationDTO operationDTO = new OperationDTO();
		BeanUtils.copyProperties(operation, operationDTO);
		return operationDTO;
	}

	@Override
	public Operation toOperation(OperationDTO operationDTO) {
		Operation operation = new Operation();
		BeanUtils.copyProperties(operationDTO, operation);
		return operation;
	}
}
