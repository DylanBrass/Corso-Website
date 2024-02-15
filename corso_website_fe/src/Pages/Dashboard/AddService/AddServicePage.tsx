import React, { useEffect, useRef, useState } from 'react';
import './AddServicePage.css';
import axios from 'axios';
import swal from 'sweetalert';
import { useAuth } from '../../../security/Components/AuthProvider';
import NavigationBar from '../../../Components/NavBar/NavigationBar';
import pen from '../../../ressources/images/pen.svg';
import {SyncLoader} from "react-spinners";
import {useNavigate} from "react-router";
import Cloudinary from "../../../Services/Cloudinary";
import {useTranslation} from "react-i18next";
import {createJSONForLanguages} from "../../../Services/TranslationTools";
import BackButton from "../../../Components/BackButton";
import '../../globalStyling/globalStyling.css';

function AddServicePage() {
    const [serviceIconPreview, setServiceIconPreview] = useState('');
    const [serviceImagePreview, setServiceImagePreview] = useState('');
    const iconFileInputRef = useRef<HTMLInputElement>(null);
    const imageFileInputRef = useRef<HTMLInputElement>(null);

    const auth = useAuth();

    const [isLoading, setIsLoading] = useState<boolean>(false);
    const navigate = useNavigate()

    const {t} = useTranslation();

    useEffect(() => {
        // @ts-ignore
        if (!auth.isAuthenticated || !auth.userRoles().includes('Admin')) {
            window.location.href = '/';
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);



    const handlePenClick = (fileInputRef: React.RefObject<HTMLInputElement>) => {
        fileInputRef.current?.click();
    };

    const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();

        const englishServiceName = (document.getElementById("serviceName_en") as HTMLInputElement)?.value
        const englishServiceDescription = (document.getElementById("serviceDescription_en") as HTMLInputElement)?.value

        const frenchServiceName = (document.getElementById("serviceName_fr") as HTMLInputElement)?.value
        const frenchServiceDescription = (document.getElementById("serviceDescription_fr") as HTMLInputElement)?.value

        setIsLoading(true)

        const data = {
            serviceName: createJSONForLanguages(["fr","en"], [frenchServiceName, englishServiceName]),
            serviceDescription: createJSONForLanguages(["fr","en"], [frenchServiceDescription, englishServiceDescription]),
            serviceIcon: serviceIconPreview,
            serviceImage: serviceImagePreview,
        };

        await axios
            .post(process.env.REACT_APP_BE_HOST + 'api/v1/corso/services', data, {
                headers: {
                    // @ts-ignore
                    'X-XSRF-TOKEN': auth.getXsrfToken(),
                },
            })
            .then((r) => {
                if (r.status === 201) {
                    swal(t("alerts.service.serviceCreatedTitle"), (t("alerts.service.serviceCreatedMessage")), 'success');
                    navigate(-1)
                }
            })
            .catch((e) => {
                swal(t("alerts.error"), e.response.data.message, 'error').then(() => {
                    // @ts-ignore
                    auth.authError(e.response.status);
                });
            });
        setIsLoading(false)
    };

    const backButtonHref = "/services";

    return (
        <>
            <NavigationBar />
            <BackButton
                link={backButtonHref}
                text={ t("back")}
            />
            <div className="add-service-page">
            <h1 className="add-service">{t("servicesAdminPage.addService")}</h1>
            <form onSubmit={handleSubmit} encType="multipart/form-data">
                <div className="container add-service-container">
                    <div className="grid-container">
                        <div className="grid-item-full">
                            <div className="grid-item input-container">
                                <img src={pen} alt="Edit" className="pen-icon" />
                                <input
                                    type="text"
                                    id="serviceName_en"
                                    name="serviceName"
                                    placeholder={"Service Name in English"}
                                    required
                                />
                            </div>
                            <div className="grid-item input-container">
                                <img src={pen} alt="Edit" className="pen-icon" />
                                <textarea
                                    id="serviceDescription_en"
                                    name="serviceDescription"
                                    placeholder="Description in English"
                                    required
                                />
                            </div>
                        </div>

                        <div className="grid-item-full">
                            <div className="grid-item input-container">
                                <img src={pen} alt="Edit" className="pen-icon" />
                                <input
                                    type="text"
                                    id="serviceName_fr"
                                    name="serviceName"
                                    placeholder={"Nom du Service en français"}
                                    required
                                />
                            </div>
                            <div className="grid-item input-container">
                                <img src={pen} alt="Edit" className="pen-icon" />
                                <textarea
                                    id="serviceDescription_fr"
                                    name="serviceDescription"
                                    placeholder="Description en français"
                                    required
                                />
                            </div>
                        </div>

                        <div className="grid-item-bottom">
                            <div className="image-preview-container">
                                <img
                                    src={pen}
                                    alt="Pen Icon"
                                    className="pen-icon"
                                    onClick={() => handlePenClick(imageFileInputRef)}
                                />
                                {serviceImagePreview ? (
                                    <img
                                        src={serviceImagePreview}
                                        alt="Service Preview"
                                        className="service-image"
                                    />
                                ) : (
                                    <div className="placeholder-rectangle"></div>
                                )}
                                <input
                                    type="file"
                                    accept=".jpg, .jpeg, .png, .svg"
                                    id="serviceImage"
                                    name="serviceImage"
                                    style={{ display: 'none' }}
                                    ref={imageFileInputRef}
                                    onChange={(e) => Cloudinary.handleImageChange(e, setServiceImagePreview, 1920, 1080)}
                                />
                            </div>
                        </div>

                        <div className="grid-item-bottom">
                            <div className="image-preview-container">
                                <img
                                    src={pen}
                                    alt="Pen Icon"
                                    className="pen-icon"
                                    onClick={() => handlePenClick(iconFileInputRef)}
                                />
                                {serviceIconPreview ? (
                                    <img
                                        src={serviceIconPreview}
                                        alt="Service Icon Preview"
                                        className="service-icon"/>
                                ) : (
                                    <div className="placeholder-circle"></div>
                                )}
                                <input
                                    type="file"
                                    accept=".jpg, .jpeg, .png, .svg"
                                    id="serviceIcon"
                                    name="serviceIcon"
                                    style={{ display: 'none' }}
                                    ref={iconFileInputRef}
                                    onChange={(e) => Cloudinary.handleImageChange(e, setServiceIconPreview,106,106)}
                                />
                            </div>
                        </div>
                    </div>
                </div>

                <div className={isLoading ? "d-block spinner-visible" : "d-none"}>
                    <SyncLoader className={"spinner"} color="#054AEB"/>
                </div>

                {isLoading ? "" :
                <div className="submit-button-container">
                    <input type="submit" value={t("submitButton")} className="btn-style"/>
                </div>}
            </form>
            </div>
        </>
    );
}

export default AddServicePage;
