import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import './ViewServiceDetails.css';
import {useTranslation} from "react-i18next";


interface ServiceDetails {
    serviceId: string;
    serviceName: string;
    serviceDescription: string;
    serviceImage: string;
}


function ViewServiceDetails() {

    const { serviceId } = useParams<{ serviceId: string }>();
    const [serviceDetails, setServiceDetails] = useState<ServiceDetails | null>(null);
    const [error, setError] = useState<string | null>(null);
    const [t] = useTranslation();

    useEffect(() => {
        const fetchServiceDetails = async () => {
            try {
                const response = await axios.get(process.env.REACT_APP_BE_HOST + `api/v1/corso/services/${serviceId}`);
                setServiceDetails(response.data);
            } catch (error) {
                
                // @ts-ignore
                setError(error.message);
            }
        };
        fetchServiceDetails();
    }, [serviceId]);

    if (error) {
        return <div className="alert alert-danger" role="alert">Error: {error}</div>;
    }

    if (!serviceDetails) {
        return <div>{t("load.loading")}</div>;
    }

    return (
        <div className="service-details-container">
            <div className="container">
                <div className="row">
                    <h1 className="display-4 title">{serviceDetails.serviceName}</h1>
                </div>
                <div className="row">
                    <div className="col-lg-6 col-md-12">
                        <img src={serviceDetails.serviceImage} alt={`${serviceDetails.serviceName}`} className="img-fluid img" />
                    </div>
                    <div className="col-lg-6 col-md-12 service-details">
                        <p className="lead">{serviceDetails.serviceDescription}</p>
                    </div>
                    <div className="custom-row text-center">
                        <button
                            className="back-btn"
                            onClick={() => {
                                window.location.href = "/";
                            }}
                        >
                            {t("back")}
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default ViewServiceDetails;
