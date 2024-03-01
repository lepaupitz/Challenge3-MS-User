package com.compassuol.sp.challenge.challenge3msuser.web.Client;

import com.compassuol.sp.challenge.challenge3msuser.web.dto.UserAddressDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "msaddress", url = "http://localhost:8081/v1/address")
public interface AddressClient  {

    @PostMapping(produces = "application/json", consumes = "application/json")
    UserAddressDto saveAddress(UserAddressDto userAddressDto);


}
