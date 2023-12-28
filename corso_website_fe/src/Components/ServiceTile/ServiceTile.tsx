import React from 'react';
import './ServiceTile.css';

export {};

export class Service {
    serviceId: string;
    serviceName: string;
    serviceDescription: string;
    action: () => void;

    constructor(
        serviceId: string,
        serviceName: string,
        serviceDescription: string,
        action: () => void
    ) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.serviceDescription = serviceDescription;
        this.action = action;
    }
}

function ServiceTile({ service }: { service: Service }) {
    return (
        <div className="service-tile" onClick={service.action}>
            <p className={"service-name"}>{service.serviceName.toUpperCase()}</p>
            <p className={"service-description"}>{service.serviceDescription}</p>
        </div>
    );
}

export default ServiceTile;