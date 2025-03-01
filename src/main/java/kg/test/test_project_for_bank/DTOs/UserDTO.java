package kg.test.test_project_for_bank.DTOs;


import lombok.Builder;
import lombok.Data;

import java.util.Map;
@Builder
@Data
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private Map<String, AccountDTO> accounts;
}
