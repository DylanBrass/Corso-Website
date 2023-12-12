package com.corso.springboot.Services_Subdomain.datalayer;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@Builder
@Table(name = "services")
@AllArgsConstructor
@Entity
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Embedded
    private ServiceIdentifier serviceId;

    private String serviceName;
    private String serviceDescription;
    private String serviceIcon;
    private String serviceImage;

    public Service(String serviceName, String serviceDescription, String serviceIcon, String serviceImage) {
        this.serviceId = new ServiceIdentifier();
        this.serviceName = serviceName;
        this.serviceDescription = serviceDescription;
        this.serviceIcon = serviceIcon;
        this.serviceImage = serviceImage;
    }


}
