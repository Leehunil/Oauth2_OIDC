package ohsoontaxi.login.domain.credential.presentation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RegisterRequest {

    private String name;
    private String gender;
    private String phone_number;
}
