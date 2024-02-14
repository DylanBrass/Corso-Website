import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './AllServices.css';
import {useTranslation} from "react-i18next";
import serviceDetailIcon from '../../../../ressources/images/Sign_in_circle.svg';
import i18n from "i18next";
import {getValuesFromJSON} from "../../../../Services/TranslationTools";

interface Service {
    serviceId: string;
    serviceName: string;
    serviceIcon: string;
    serviceDescription: string;
}

function AllServices() {
    const [services, setServices] = useState<Service[]>([]);
    const [error, setError] = useState<string | null>(null);

    const {t} = useTranslation();

    useEffect(() => {
        const fetchServices = async () => {
            try {
                const response = await axios.get(process.env.REACT_APP_BE_HOST + 'api/v1/corso/services/visible');
                setServices(response.data);
                
            } catch (error) {
                
                // @ts-ignore
                setError(error.message);
            }
        };
        fetchServices();
    }, []);

    function truncateDescription(description: string, wordLimit: number): string {
        const wordsArray = description.split(' ');
        if (wordsArray.length > wordLimit) {
            return wordsArray.slice(0, wordLimit).join(' ') + '...';
        } else {
            return description;
        }
    }

    function truncateTitle(title: string, wordLimit: number): string {
        const wordsArray = title.split(' ');
        if (wordsArray.length > wordLimit) {
            return wordsArray.slice(0, wordLimit).join(' ') + '...';
        } else {
            return title;
        }
    }

    function handleImageClick(serviceId: string) {
        window.location.href = `/ViewServiceDetails/${serviceId}`;
    }


    return (
        <div className="container services-container">
            <div className={"border-service mb-5"}>
                <div className="row service-header">
                    <h1 className="title-services">{t("mainPage.services")}</h1>
                </div>
                <div className={"row"}>
                    <div className={"col-12 d-flex"}>
                        <p className={"subtitle-services"}>{t("mainPage.servicesBlurb")}</p>
                    </div>
                </div>
            </div>
            {error && (
                <div className="row">
                    <div className="col-12">
                        <p className="error">{t("alerts.error")}: {error}</p>
                    </div>
                </div>
            )}
            {services.length > 0 ? (
                <div className="row">
                    {services.map((service) => (
                        <div className="col-lg-4 col-md-6 col-sm-12 mb-4" key={service.serviceId}>
                            <div className="service-card ">
                                <div className="service-icon-container">
                                    <img src={service.serviceIcon} alt={`${service.serviceName} icon`} className="service-icon" />
                                </div>
                                <div className="service-info">
                                    <div className="service-info-text-area">
                                        <h2 className="service-name">{truncateTitle(`${getValuesFromJSON(i18n.language, service.serviceName)}`, 4)}</h2>
                                        <p className="service-description">
                                            {truncateDescription(`${getValuesFromJSON(i18n.language, service.serviceDescription)}`, 15)}
                                        </p>
                                    </div>
                                    <div className="row align-items-end justify-content-end justify-content-space-between flex-end mt-5">
                                        <div className="col-8 d-flex justify-content-start">
                                            <p className="learn-more-text">{t("mainPage.learnMore")}</p>
                                        </div>
                                        <div className="col-4 d-flex justify-content-end">
                                            <img
                                                src={serviceDetailIcon}
                                                alt={`${service.serviceName} icon`}
                                                className="service-detail-icon"
                                                onClick={() => handleImageClick(service.serviceId)}
                                            />
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            ) : (
                <div className="row">
                    <div className="col-12">
                        <p>{t("mainPage.noServicesFound")}</p>
                    </div>
                </div>
            )}
        </div>
    );
}

export default AllServices;
