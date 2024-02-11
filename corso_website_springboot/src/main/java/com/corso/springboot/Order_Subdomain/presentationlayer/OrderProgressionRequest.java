package com.corso.springboot.Order_Subdomain.presentationlayer;


import com.corso.springboot.Service_Subdomain.presentationlayer.ServiceResponse;
import com.corso.springboot.utils.annotations.ValidCanadianPostalCode;
import com.corso.springboot.utils.annotations.ValidPhoneNumber;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=false)
@Data
@AllArgsConstructor
@Builder
public class OrderProgressionRequest {



    private String progressInformation;

    private int hoursWorked;

    private int estimatedDuration;

    @NotBlank(message = "Service Id is required.")
    private String serviceId;

    public String getProgression() {
        return progressInformation;
    }
}

