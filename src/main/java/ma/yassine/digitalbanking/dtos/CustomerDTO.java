package ma.yassine.digitalbanking.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CustomerDTO {
    private Long id;
    private String name;
    private String email;
}
