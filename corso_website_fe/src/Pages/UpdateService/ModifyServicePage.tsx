import React, { useEffect, useState, useRef } from 'react';
import './ModifyServicePage.css';
import axios from 'axios';
import swal from 'sweetalert';
import { useAuth } from '../../security/Components/AuthProvider';
import NavigationBar from '../../Components/NavBar/NavigationBar';
import pen from '../../images/pen.svg';
import { useParams } from 'react-router-dom';

function ModifyServicePage() {
  const { serviceId } = useParams();
  const [serviceName, setServiceName] = useState('');
  const [serviceDescription, setServiceDescription] = useState('');
  const [serviceIconPreview, setServiceIconPreview] = useState('');
  const [serviceImagePreview, setServiceImagePreview] = useState('');
  const iconFileInputRef = useRef<HTMLInputElement>(null);
  const imageFileInputRef = useRef<HTMLInputElement>(null);

  const auth = useAuth();

  useEffect(() => {
    // @ts-ignore
    if (!auth.isAuthenticated || !auth.userRoles().includes('Admin')) {
      window.location.href = 'http://localhost:3000/';
    } else {
      // Fetch existing service data for modification
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
      console.error('Error fetching service data:', error);
    }
  };

  const convertToBase64 = (file: File, callback: (result: string) => void) => {
    const reader = new FileReader();
    reader.onloadend = () => {
      callback(reader.result as string);
    };
    reader.readAsDataURL(file);
  };

  const handleServiceImageChange = (
      event: React.ChangeEvent<HTMLInputElement>,
      setImagePreview: (value: string) => void
  ) => {
    const file = event.target.files ? event.target.files[0] : null;

    if (file) {
      // Check image dimensions
      const image = new Image();
      image.src = URL.createObjectURL(file);

      image.onload = () => {
        const maxWidth = 1920;
        const maxHeight = 1080;

        if (image.width > maxWidth || image.height > maxHeight) {
          swal('Error', 'Service image dimensions should be 1920x1080 or smaller.', 'error');
          return;
        }

        convertToBase64(file, (base64Result) => {
          setImagePreview(base64Result);
        });
      };
    } else {
      setImagePreview('');
    }
  };

  const handleServiceIconChange = (
      event: React.ChangeEvent<HTMLInputElement>,
      setImagePreview: (value: string) => void
  ) => {
    const file = event.target.files ? event.target.files[0] : null;

    if (file) {
      // Check icon dimensions
      const image = new Image();
      image.src = URL.createObjectURL(file);

      image.onload = () => {
        const iconWidth = 106;
        const iconHeight = 106;

        if (image.width !== iconWidth || image.height !== iconHeight) {
          swal('Error', 'Service icon dimensions should be 106x106.', 'error');
          return;
        }

        convertToBase64(file, (base64Result) => {
          setImagePreview(base64Result);
        });
      };
    } else {
      setImagePreview('');
    }
  };

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

    console.log(data.serviceImage);

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

      if (response.status === 200) {
        await swal('Service modified!', 'Service has been modified successfully!', 'success');
      }
    } catch (error) {
      // @ts-ignore
      swal('Error!', error.response.data.message, 'error').then(() => {
        // @ts-ignore
        auth.authError(error.response.status);
      });
    }
  };

  return (
      <>
        <NavigationBar />
        <div className="modify-service-page">
          <h1 className="modify-service">Edit Service</h1>
          <form onSubmit={handleSubmit} encType="multipart/form-data">
            <div className="container modify-service-container">
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
                        onChange={(e) => handleServiceImageChange(e, setServiceImagePreview)}
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
                        onChange={(e) => handleServiceIconChange(e, setServiceIconPreview)}
                    />
                  </div>
                </div>
              </div>
            </div>
            <div className="submit-button-container">
              <input type="submit" value="Save Changes" className="submit-button"/>
            </div>
          </form>
        </div>
      </>
  );
}

export default ModifyServicePage;
