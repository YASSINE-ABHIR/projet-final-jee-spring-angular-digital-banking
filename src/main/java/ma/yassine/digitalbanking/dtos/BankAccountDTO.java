package ma.yassine.digitalbanking.dtos;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import ma.yassine.digitalbanking.enumirats.AccountStatus;

@Getter
@Setter
public class BankAccountDTO {
    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    private String currency;
    private CustomerDTO customerDTO;
}
