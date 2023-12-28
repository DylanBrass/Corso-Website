import React, { useEffect, useState } from "react";
import axios from "axios";
import './ServicesThroughDashboard.css';
import NavigationBar from "../../Components/NavBar/NavigationBar";
import ServiceTile from "../../Components/ServiceTile/ServiceTile";
import plus from "../../images/plus-services.svg";

interface Service {
    serviceId: string;
    serviceName: string;
    serviceDescription: string;
}

function ServicesThroughDashboard(){
    const [services, setServices] = useState<Service[]>([]);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchServices = async () => {
            try {
                const response = await axios.get('http://localhost:8080/api/v1/corso/services');
                setServices(response.data);
            } catch (error) {
                console.error('Error fetching services:', error);
                // @ts-ignore
                setError(error.message);
            }
        };
        fetchServices();
    }, []);

    const addService = () => {
        console.log("Add service clicked");
        window.location.href = "/add-service";
    }


    return(
        <>
            <NavigationBar/>
            {error && <div className="row"><div className="col-12"><p className="error">Error: {error}</p></div></div>}
            <div className="service-container">
                <h1>All Services</h1>
                <img src={plus} alt={"Add Service"} className={"add-service-icon"} onClick={addService}/>
                <div className="service-tiles-container">
                    {services.map(service => (
                        <ServiceTile
                            key={service.serviceId}
                            service = {{
                                serviceId: service.serviceId,
                                serviceName: service.serviceName,
                                serviceDescription: service.serviceDescription,
                                action: () => {
                                    console.log("Service clicked");
                                }
                            }}
                        />
                    ))}
                </div>
            </div>
        </>
    )
}

export default ServicesThroughDashboard;