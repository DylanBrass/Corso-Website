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

function AddServicePage() {
    const [serviceName, setServiceName] = useState('');
    const [serviceDescription, setServiceDescription] = useState('');
    const [serviceIconPreview, setServiceIconPreview] = useState('');
    const [serviceImagePreview, setServiceImagePreview] = useState('');
    const iconFileInputRef = useRef<HTMLInputElement>(null);
    const imageFileInputRef = useRef<HTMLInputElement>(null);

    const auth = useAuth();

    const [isLoading, setIsLoading] = useState<boolean>(false);
    const navigate = useNavigate()

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

        const data = {
            serviceName,
            serviceDescription,
            serviceIcon: serviceIconPreview,
            serviceImage: serviceImagePreview,
        };

        setIsLoading(true)
        await axios
            .post(process.env.REACT_APP_BE_HOST + 'api/v1/corso/services', data, {
                headers: {
                    // @ts-ignore
                    'X-XSRF-TOKEN': auth.getXsrfToken(),
                },
            })
            .then((r) => {
                if (r.status === 201) {
                    swal('Service created!', 'Service has been created successfully!', 'success');
                    navigate(-1)
                }
            })
            .catch((e) => {
                swal('Error!', e.response.data.message, 'error').then(() => {
                    // @ts-ignore
                    auth.authError(e.response.status);
                });
            });
        setIsLoading(false)
    };

    return (
        <>
            <NavigationBar />
            <div className="add-service-page">
            <h1 className="add-service">Add A Service</h1>
            <form onSubmit={handleSubmit} encType="multipart/form-data">
                <div className="container add-service-container">
                    <div className="grid-container">
                        <div className="grid-item-full">
                            <div className="grid-item input-container">
                                <img src={pen} alt="Edit" className="pen-icon" />
                                <input
                                    type="text"
                                    id="serviceName"
                                    name="serviceName"
                                    placeholder="Service Name"
                                    required
                                    value={serviceName}
                                    onChange={(e) => setServiceName(e.target.value)}
                                />
                            </div>
                            <div className="grid-item input-container">
                                <img src={pen} alt="Edit" className="pen-icon" />
                                <textarea
                                    id="serviceDescription"
                                    name="serviceDescription"
                                    placeholder="Description"
                                    required
                                    value={serviceDescription}
                                    onChange={(e) => setServiceDescription(e.target.value)}
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

                <div className="submit-button-container">
                    <input type="submit" value="Submit" className="submit-button"/>
                </div>
            </form>
            </div>
        </>
    );
}

export default AddServicePage;
