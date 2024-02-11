import React, {useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import {useAuth} from "../../../../security/Components/AuthProvider";
import Gallery from "../../../../ressources/Models/Gallery";
import axios from "axios";
import NavigationBar from "../../../../Components/NavBar/NavigationBar";
import "./ViewSpecificGallery.css";
import pen from "../../../../ressources/images/pen.svg";
import templateImg from "../../../../ressources/images/Upload_Image_Preview.png";
import Cloudinary from "../../../../Services/Cloudinary";
import swal from "sweetalert";
import {useNavigate} from "react-router";
import {SyncLoader} from "react-spinners";
import {useTranslation} from "react-i18next";

function ViewSpecificGallery() {

    const auth = useAuth();

    const {t} = useTranslation();

    const [gallery, setGallery] = useState<Gallery>(new Gallery("", "", "", 0));

    const {galleryId} = useParams();

    const [imagePreview, setImagePreview] = useState<string>('');

    const [isLoading, setIsLoading] = useState<boolean>(false);
    const getGallery = async () => {
        axios.get(`${process.env.REACT_APP_BE_HOST}api/v1/corso/galleries/carousel/${galleryId}`, {
            headers: {
                // @ts-ignore
                'X-XSRF-TOKEN': auth.getXsrfToken(),
            },
        }).then((response) => {
            setGallery(response.data);
            setImagePreview(response.data.photo);
            console.log(response.data)
        }).catch((error) => {
            // @ts-ignore
            auth.authError(error.response.status);
        });
    }

    useEffect(() => {
        getGallery();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);


    const updateGallery = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();

        const data = {
            description: gallery.description,
            photo: imagePreview,
            image_position: gallery.image_position
        };

        setIsLoading(true)

        try {
            const response = await axios.put(
                `${process.env.REACT_APP_BE_HOST}api/v1/corso/galleries/carousel/${galleryId}`,
                data,
                {
                    headers: {
                        // @ts-ignore
                        'X-XSRF-TOKEN': auth.getXsrfToken(),
                    },
                }
            );

            setIsLoading(false)

            if(response.status === 200) {
                await swal(t("alerts.gallery.galleryUpdatedTitle"), t("alerts.gallery.galleryUpdatedMessage"), "success");
                window.location.href = "/carousel/order";
            }
        } catch (error) {
            // @ts-ignore
            swal(t("alerts.error"), error.response.data.message, 'error')
            // @ts-ignore
            auth.authError(error.response.status);
        }

    }

    return (
        <div>
            <NavigationBar/>
            <div className={"manage-gallery-wrapper p-5"}>
                <h1 className={"text-center mb-4"}>{t("editGalleryPage.editGallery")}</h1>
                <form onSubmit={updateGallery} className={"w-75 m-auto"}>
                    <div className={"d-flex row"}>
                        <img src={pen} alt="Edit" className="pen-icon col-md-2 d-none d-md-block"/>
                        <textarea
                            id="description"
                            name="description"
                            placeholder="Description"
                            required
                            className={"col-12 col-md-10"}
                            value={gallery.description}
                            onChange={(event) => setGallery({...gallery, description: event.target.value})}
                        />
                    </div>
                    <div className={"row mt-4"}>
                        <img src={pen} alt="Edit" className="pen-icon col-md-2 d-none d-md-block"/>
                        {
                            imagePreview ?
                                <img src={imagePreview} alt={"preview"} className={"border col-12 col-md-10"}/>
                                :
                                <img src={templateImg} alt={"preview"} className={"border col-12 col-md-10"}/>
                        }


                    </div>
                    <div className={"row"}>
                        <label id={"button-for-file"} className={"m-auto mt-4"} htmlFor="image">Image
                            <input type="file"
                                   style={{
                                       display: "none",
                                   }}
                                   accept=".jpg, .jpeg, .png, .svg"
                                   className={"form-control-file col-12"}
                                   onChange={(event) => Cloudinary.handleImageChange(event, setImagePreview, 1920, 1080)}
                                   id="image"/>

                        </label>
                    </div>
                    <div className={"row text-center"}>
                        <p className={"col-12"}>{t("editGalleryPage.tip")}</p>
                    </div>
                    <div className={isLoading ? "d-block spinner-visible text-center" : "d-none"}>
                        <SyncLoader className={"spinner"} color="#054AEB"/>
                    </div>
                    <div className={"row"}>
                        <button className={"btn btn-primary m-auto w-50"} type={"submit"} value={"Update"}>
                            {t("update")}
                        </button>
                    </div>
                </form>
            </div>
            <div className="custom-row text-center">
                <button
                    className="back-btn"
                    onClick={() => {
                        window.location.href = "/carousel/order";
                    }}
                >
                    {t("back")}
                </button>
            </div>
        </div>
    );
}

export default ViewSpecificGallery;