import React from 'react';
import './ServiceTile.css';
import View_light from "../../ressources/images/View_light.png";
import CrossedOutEye from "../../ressources/images/Crossed-out_Eye.png";


export class Service {
    serviceId: string;
    serviceName: string;
    serviceDescription: string;
    active: boolean;
    action: () => void;

    constructor(
        serviceId: string,
        serviceName: string,
        serviceDescription: string,
        active: boolean,
        action: () => void
    ) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.serviceDescription = serviceDescription;
        this.active = active;
        this.action = action;
    }
}

interface ServiceTileProps {
    service: Service;
    updateServiceStatus: (serviceId: string) => void;
}

function ServiceTile({ service, updateServiceStatus }: ServiceTileProps) {
    const handleEyeClick = (event: React.MouseEvent<HTMLImageElement, MouseEvent>) => {
        event.stopPropagation();
        updateServiceStatus(service.serviceId);
    };

    return (
        <div className="service-tile w-100 text-center" onClick={service.action}>
            <div className="service-header">
                <p className="service-name">
                    {service.serviceName.toUpperCase()}
                </p>
                <img
                    src={service.active ? View_light : CrossedOutEye}
                    alt={service.active ? "Active" : "Inactive"}
                    className="eye-icon"
                    onClick={handleEyeClick}
                />
            </div>
            <p className="service-description text-center">{service.serviceDescription}</p>
        </div>
    );
}

export default ServiceTile;