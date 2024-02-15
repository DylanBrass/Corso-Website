import React, { useEffect, useState } from "react";
import axios from "axios";
import './ServicesThroughDashboard.css';
import NavigationBar from "../../../Components/NavBar/NavigationBar";
import ServiceTile from "../../../Components/ServiceTile/ServiceTile";
import plus from "../../../ressources/images/plus-services.svg";
import {useAuth} from "../../../security/Components/AuthProvider";
import swal from "sweetalert";
import {useTranslation} from "react-i18next";
import {getValuesFromJSON} from "../../../Services/TranslationTools";
import i18n from "i18next";
import BackButton from "../../../Components/BackButton";

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
    const {t} = useTranslation();

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
                    swal(t("alerts.service.serviceVisibilityUpdatedTitle"), (t("alerts.service.serviceVisibilityUpdatedMessage")), "success")
                }
            })
                .catch(e => {
                    swal(t("alerts.error"), e.response.data.message, "error")
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

    const backButtonHref = "/dashboard";

    return(
        <>
            <NavigationBar/>
            <BackButton
                link={backButtonHref}
                text={ t("backToDashboard")}
            />
            {error && <div className="row"><div className="col-12"><p className="error">{t("alerts.error")}: {error}</p></div></div>}
            <div className="service-container">
                <h1>{t("servicesAdminPage.allServices")}</h1>
                <img src={plus} alt={"Add Service"} className={"add-service-icon"} onClick={addService}/>
                <div className="service-tiles-container">
                    {services.map(service => (
                        <ServiceTile
                            key={service.serviceId}
                            service = {{
                                serviceId: service.serviceId,
                                serviceName: getValuesFromJSON(i18n.language,service.serviceName),
                                serviceDescription: getValuesFromJSON(i18n.language,service.serviceDescription),
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