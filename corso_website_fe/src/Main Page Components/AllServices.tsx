import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './AllServices.css'; // Importing CSS file

interface Service {
    serviceId: string;
    serviceName: string;
    serviceIcon: string;
}

function AllServices() {
    const [services, setServices] = useState<Service[]>([]);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchServices = async () => {
            try {
                const response = await axios.get('http://localhost:8080/api/v1/corso/services/allServices');
                setServices(response.data);
            } catch (error) {
                console.error('Error fetching services:', error);
                // @ts-ignore
                setError(error.message);
            }
        };
        fetchServices();
    }, []);

    return (
        <div className="container services-container">
            <div className="row service-header">
                <h1 className="service-title">All Services</h1>
            </div>
            {error && <div className="row"><div className="col-12"><p className="error">Error: {error}</p></div></div>}
            <div className="row">
                {services.map(service => (
                    <div className="col-lg-4 col-md-6 col-sm-12 mb-4" key={service.serviceId}>
                        <div className="service-card">
                            <div className="service-icon-container">
                                <img src={service.serviceIcon} alt={`${service.serviceName} icon`} className="service-icon" />
                            </div>
                            <div className="service-info">
                                <h2 className="service-name">{service.serviceName}</h2>
                                {/* <a className="details-hyperlink" href="#">View Details</a> */}
                            </div>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default AllServices;
