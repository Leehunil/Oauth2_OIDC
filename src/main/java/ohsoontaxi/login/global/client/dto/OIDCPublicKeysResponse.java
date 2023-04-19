package ohsoontaxi.login.global.client.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import ohsoontaxi.login.domain.credential.service.OIDCDecodePayload;

import java.util.List;

@Getter
@NoArgsConstructor
public class OIDCPublicKeysResponse {
    List<OIDCPublicKeyDto> keys;
}
