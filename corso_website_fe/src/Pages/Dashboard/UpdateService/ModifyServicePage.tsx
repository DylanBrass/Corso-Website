import React, { useEffect, useState, useRef } from 'react';
import './ModifyServicePage.css';
import axios from 'axios';
import swal from 'sweetalert';
import { useAuth } from '../../../security/Components/AuthProvider';
import NavigationBar from '../../../Components/NavBar/NavigationBar';
import pen from '../../../ressources/images/pen.svg';
import { useParams } from 'react-router-dom';
import {SyncLoader} from "react-spinners";
import Cloudinary from "../../../Services/Cloudinary";
import {useTranslation} from "react-i18next";
import {createJSONForLanguages, getValuesFromJSON} from "../../../Services/TranslationTools";
import BackButton from "../../../Components/BackButton";
import '../../globalStyling/globalStyling.css';


function ModifyServicePage() {
  const { serviceId } = useParams();
  const [serviceName, setServiceName] = useState('');
  const [serviceDescription, setServiceDescription] = useState('');
  const [serviceIconPreview, setServiceIconPreview] = useState('');
  const [serviceImagePreview, setServiceImagePreview] = useState('');
  const iconFileInputRef = useRef<HTMLInputElement>(null);
  const imageFileInputRef = useRef<HTMLInputElement>(null);

  const auth = useAuth();
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const {t} = useTranslation();

  useEffect(() => {
    // @ts-ignore
    if (!auth.isAuthenticated || !auth.userRoles().includes('Admin')) {
      window.location.href = '/';
    } else {
      fetchServiceData();
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const fetchServiceData = async () => {
    try {
      const response = await axios.get(
          process.env.REACT_APP_BE_HOST + `api/v1/corso/services/${serviceId}`,
          {
            headers: {
              // @ts-ignore
              'X-XSRF-TOKEN': auth.getXsrfToken(),
            },
          }
      );

      const serviceData = response.data;
      setServiceName(serviceData.serviceName);
      setServiceDescription(serviceData.serviceDescription);
      setServiceIconPreview(serviceData.serviceIcon);
      setServiceImagePreview(serviceData.serviceImage);
    } catch (error) {
      
    }
  };



  const handlePenClick = (fileInputRef: React.RefObject<HTMLInputElement>) => {
    fileInputRef.current?.click();
  };

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    let serviceNameEnglish = (document.getElementById("serviceName_en") as HTMLInputElement)?.value
    let serviceNameFrench = (document.getElementById("serviceName_fr") as HTMLInputElement)?.value
    let serviceDescriptionEnglish = (document.getElementById("serviceDescription_en") as HTMLInputElement)?.value
    let serviceDescriptionFrench = (document.getElementById("serviceDescription_fr") as HTMLInputElement)?.value

    const data = {
      serviceName: createJSONForLanguages(["fr", "en"], [serviceNameFrench, serviceNameEnglish]),
      serviceDescription: createJSONForLanguages(["fr", "en"], [serviceDescriptionFrench, serviceDescriptionEnglish]),
      serviceIcon: serviceIconPreview,
      serviceImage: serviceImagePreview,
    };

    setIsLoading(true)

    try {
      const response = await axios.put(
          process.env.REACT_APP_BE_HOST + `api/v1/corso/services/${serviceId}`,
          data,
          {
            headers: {
              // @ts-ignore
              'X-XSRF-TOKEN': auth.getXsrfToken(),
            },
          }
      );

      setIsLoading(false)

      if (response.status === 200) {
        await swal((t("alerts.service.serviceModifiedTitle")), (t("alerts.service.serviceModifiedMessage")), 'success');
        window.location.href = "/services/";
      }
    } catch (error) {
      // @ts-ignore
      swal((t("alerts.error")), error.response.data.message, 'error').then(() => {
        // @ts-ignore
        auth.authError(error.response.status);
      });
    }
  };

    const backButtonHref = "/adminServices/" + serviceId;
  return (
      <>
        <NavigationBar />
        <BackButton
            link={backButtonHref}
            text={ t("back")}
        />
        <div className="modify-service-page">
          <h1 className="modify-service">{t("servicesAdminPage.editService")}</h1>
          <form onSubmit={handleSubmit} encType="multipart/form-data">
            <div className="container modify-service-container">
              <div className="grid-container">
                <div className="grid-item-full">
                  <div className="grid-item input-container">
                    <img src={pen} alt="Edit" className="pen-icon" />
                    <input
                        type="text"
                        id="serviceName_en"
                        name="serviceName"
                        placeholder={t("servicesAdminPage.serviceName")}
                        required
                        defaultValue={getValuesFromJSON("en", serviceName)}
                    />
                  </div>
                  <div className="grid-item input-container">
                    <img src={pen} alt="Edit" className="pen-icon" />
                    <textarea
                        id="serviceDescription_en"
                        name="serviceDescription"
                        placeholder="Description"
                        required
                        defaultValue={getValuesFromJSON("en", serviceDescription)}
                    />
                  </div>
                  <div className="grid-item input-container">
                    <img src={pen} alt="Edit" className="pen-icon" />
                    <input
                        type="text"
                        id="serviceName_fr"
                        name="serviceName"
                        placeholder={t("servicesAdminPage.serviceName")}
                        required
                        defaultValue={getValuesFromJSON("fr", serviceName)}
                    />
                  </div>
                  <div className="grid-item input-container">
                    <img src={pen} alt="Edit" className="pen-icon" />
                    <textarea
                        id="serviceDescription_fr"
                        name="serviceDescription"
                        placeholder="Description"
                        required
                        defaultValue={getValuesFromJSON("fr", serviceDescription)}
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
              <input type="submit" value={t("save")} className="btn-style mb-4"/>
            </div>}
          </form>
        </div>
      </>
  );
}

export default ModifyServicePage;
