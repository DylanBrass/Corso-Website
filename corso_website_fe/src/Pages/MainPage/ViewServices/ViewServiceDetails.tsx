import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import './ViewServiceDetails.css';
import {useTranslation} from "react-i18next";
import {getValuesFromJSON} from "../../../Services/TranslationTools";
import i18n from "i18next";
import BackButton from "../../../Components/BackButton";


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

    const backButtonHref = "/";

    return (
        <div className="service-details-container">
            <BackButton
                link={backButtonHref}
                text={ t("backToMainPage")}
            />
            <div className="container">
                <div className="row">
                    <h1 className="display-4 service-details-title-mainPage">{
                        getValuesFromJSON(i18n.language,serviceDetails.serviceName)
                    }</h1>
                </div>
                <div className="row">
                    <div className="col-lg-6 col-md-12">
                        <img src={serviceDetails.serviceImage} alt={`${serviceDetails.serviceName}`} className="img-fluid img" />
                    </div>
                    <div className="col-lg-6 col-md-12 service-details">
                        <p className="lead">{getValuesFromJSON(i18n.language,serviceDetails.serviceDescription)}</p>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default ViewServiceDetails;
