import React, { useEffect, useState } from "react";
import axios from "axios";
import './ServicesThroughDashboard.css';
import NavigationBar from "../../../Components/NavBar/NavigationBar";
import ServiceTile from "../../../Components/ServiceTile/ServiceTile";
import plus from "../../../ressources/images/plus-services.svg";
import {useAuth} from "../../../security/Components/AuthProvider";
import swal from "sweetalert";

interface Service {
    serviceId: string;
    serviceName: string;
    serviceDescription: string;
    active: boolean;
}

function ServicesThroughDashboard(){
    const [services, setServices] = useState<Service[]>([]);
    const [error, setError] = useState<string | null>(null);
    const auth = useAuth();

    useEffect(() => {
        const fetchServices = async () => {
            try {
                const response = await axios.get(process.env.REACT_APP_BE_HOST+ 'api/v1/corso/services');
                setServices(response.data);
                console.log(response.data);
            } catch (error) {
                
                // @ts-ignore
                setError(error.message);
            }
        };
        fetchServices();
    }, []);

    const addService = () => {
        
        window.location.href = "/add-service";
    }

    const updateServiceStatus = async (serviceId: string) => {
        try {
            await axios.patch(
                process.env.REACT_APP_BE_HOST + `api/v1/corso/services/${serviceId}/visibility`,
                {},
                {
                    headers: {
                        // @ts-ignore
                        'X-XSRF-TOKEN': auth.getXsrfToken(),
                    },
                }
            ).then(r => {
                if (r.status === 200) {
                    swal("Service visibility is updated!", "The change will be reflected on the main page!", "success")
                }
            })
                .catch(e => {
                    swal("Error!", e.response.data.message, "error")
                        .then(() => {
                            // @ts-ignore
                            auth.authError(e.response.status)
                        })
                })

            const updatedServices = services.map((service) => {
                if (service.serviceId === serviceId) {
                    return {
                        ...service,
                        active: !service.active,
                    };
                }
                return service;
            });
            setServices(updatedServices);
            updatedServices.forEach((service) => {
                console.log(service.serviceId, service.active);
            }

            );
        } catch (error) {
            console.error('Error updating service status:', error);
        }
    };

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
                                active: service.active,
                                action: () => {
                                    window.location.href = `/adminServices/${service.serviceId}`;
                                },
                            }}
                            updateServiceStatus={updateServiceStatus}
                        />
                    ))}
                </div>
            </div>
        </>
    )
}

export default ServicesThroughDashboard;