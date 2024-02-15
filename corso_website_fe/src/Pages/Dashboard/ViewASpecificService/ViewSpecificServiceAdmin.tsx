import React, { useEffect, useState } from 'react';
import './ViewSpecificServiceAdmin.css';
import axios from 'axios';
import { useAuth } from '../../../security/Components/AuthProvider';
import NavigationBar from '../../../Components/NavBar/NavigationBar';
import {useParams} from "react-router-dom";
import Service from "../../../ressources/Models/Service";
import engine from '../../../ressources/images/engine.png';
import {useTranslation} from "react-i18next";
import {getValuesFromJSON} from "../../../Services/TranslationTools";
import i18n from "i18next";
import BackButton from "../../../Components/BackButton";

function ViewSpecificServiceAdmin() {

    let {serviceId} = useParams();
    const { t } = useTranslation();

    const auth = useAuth();

    useEffect(() => {
        // @ts-ignore
        if (!auth.isAuthenticated || !auth.userRoles().includes('Admin')) {
            window.location.href = '/';
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    const getService = async () => {
        axios.get(process.env.REACT_APP_BE_HOST + `api/v1/corso/services/${serviceId}`,
            {
                headers: {
                    // @ts-ignore
                    'X-XSRF-TOKEN': auth.getXsrfToken()
                }
            }
        )
            .then(res => {
                const service = res.data;
                setService(service);
            })
            .catch(err => {
                

                if (err.response.status === 401 || err.response.status === 403) {
                    // @ts-ignore
                    auth.authError(err.response.status);
                }

            })

    }

    useEffect(() => {
        getService()
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);
    const [service, setService] = useState(new Service("", "", "", "", ""));

    const handlePageSizeToModifyService = (serviceId: string) => {
        window.location.href = `/modify-service/${serviceId}`;
    }

    const backButtonHref = "/services";

    return (
        <>
            <NavigationBar />
            <BackButton
                link={backButtonHref}
                text={ t("back")}
            />
            <div className="container">
                <div className="row justify-content-center">
                    <div className="engine-container">
                        <img src={engine} alt="engine" className="engine"  onClick={() => handlePageSizeToModifyService(service.serviceId)}/>
                    </div>
                    <div className="col-md-8">
                        <div className="view-service-admin-container">
                            <div className="view-service-admin-container-content">
                                <div className="view-service-admin-container-content-description">
                                    <h1>{getValuesFromJSON(i18n.language,service.serviceName)}</h1>
                                    <hr></hr>
                                    <p>{getValuesFromJSON(i18n.language,service.serviceDescription)}</p>
                                </div>
                                <div className="view-service-admin-container-content-image">
                                    <img src={service.serviceImage} alt={service.serviceName} className="img-fluid" />
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
}

export default ViewSpecificServiceAdmin;
