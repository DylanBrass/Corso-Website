package com.corso.springboot.Service_Subdomain.datalayer;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Builder
@Table(name = "services")
@AllArgsConstructor
@Entity
public class ServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Embedded
    private ServiceIdentifier serviceIdentifier;

    private String serviceName;

    private String serviceDescription;

    private String serviceIcon;

    private String serviceImage;

    private boolean isActive;

    public ServiceEntity(){
        this.serviceIdentifier = new ServiceIdentifier();
    }

    public ServiceEntity(String serviceName, String serviceDescription, String serviceIcon, String serviceImage, boolean isActive){
        this.serviceIdentifier = new ServiceIdentifier();
        this.serviceName = serviceName;
        this.serviceDescription = serviceDescription;
        this.serviceIcon = serviceIcon;
        this.serviceImage = serviceImage;
        this.isActive = isActive;
    }
}
