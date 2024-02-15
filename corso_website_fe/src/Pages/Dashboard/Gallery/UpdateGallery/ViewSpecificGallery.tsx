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
import {SyncLoader} from "react-spinners";
import {useTranslation} from "react-i18next";
import {createJSONForLanguages, getValuesFromJSON} from "../../../../Services/TranslationTools";
import TrashDelete from "../../../../ressources/images/Trash_light.svg";
import BackButton from "../../../../Components/BackButton";
import '../../../globalStyling/globalStyling.css';

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


        let englishDesc = (document.getElementById("description_en") as HTMLInputElement)?.value
        let frenchDesc = (document.getElementById("description_fr") as HTMLInputElement)?.value

        const data = {
            description: createJSONForLanguages(["fr","en"],[frenchDesc,englishDesc]),
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

            if (response.status === 200) {
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



    const deleteGallery = async () => {
        const confirmDelete = await swal({
            title: (t("editGalleryPage.editImagePage.galleryDeletedTitle")) + "?",
            text: (t("editGalleryPage.editImagePage.galleryDeletedMessage")),
            icon: "warning",
            buttons: [(t("cancel")), (t("delete"))],
            dangerMode: true,
        });

        if (!confirmDelete) {
            return;
        }

        try {
            const response = await axios.delete(
                `${process.env.REACT_APP_BE_HOST}api/v1/corso/galleries/carousel/${galleryId}`,
                {
                    headers: {
                        // @ts-ignore
                        'X-XSRF-TOKEN': auth.getXsrfToken(),
                    },
                }
            );

            if (response.status === 204) {
                await swal({
                    title: (t("editGalleryPage.editImagePage.galleryDeletedSuccessTitle")),
                    icon: "success",
                });

                window.location.href = "/carousel/order";
            } else {
                console.error("Unexpected response status:", response.status);
                swal(t("alerts.error"), 'error');
            }
        } catch (error) {
            console.error("Error deleting gallery:", error);

            // @ts-ignore
            if (error.response && error.response.data && error.response.data.message) {
                // @ts-ignore
                swal(t("alerts.error"), error.response.data.message, 'error');
            } else {
                swal(t("alerts.error"), 'error');
            }
        }
    }

    const backButtonHref = "/carousel/order";

    return (
        <div>
            <NavigationBar/>
            <BackButton
                link={backButtonHref}
                text={ t("back")}
            />
            <div className={"manage-gallery-wrapper p-5"}>

                <div className={"d-flex justify-content-center"}>
                <button className="btn-style-red" onClick={deleteGallery}>
                    <img src={TrashDelete} alt="DeleteTrash"/>
                </button>
                </div>

                <h1 className={"text-center mb-4"}>{t("editGalleryPage.editGallery")}</h1>
                <form onSubmit={updateGallery} className={"w-75 m-auto"}>
                    <div className={"d-flex row"}>
                        <img src={pen} alt="Edit" className="pen-icon col-md-2 d-none d-md-block"/>
                        <textarea
                            id="description_en"
                            name="description"
                            placeholder="Description in English"
                            required
                            className={"col-12 col-md-10"}
                            defaultValue={getValuesFromJSON("en",gallery.description)}
                        />
                    </div>
                    <div className={"d-flex row"}>
                        <img src={pen} alt="Edit" className="pen-icon col-md-2 d-none d-md-block"/>
                        <textarea
                            id="description_fr"
                            name="description"
                            placeholder="Description en français"
                            required
                            className={"col-12 col-md-10"}
                            defaultValue={getValuesFromJSON("fr",gallery.description)}
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
                        <label id={"button-for-file"} className={" m-auto mt-4"} htmlFor="image">Image
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
                        {isLoading ? "" :
                        <button className={"btn btn-style m-auto w-50"} type={"submit"} value={"Update"}>
                            {t("update")}
                        </button>}
                    </div>
                </form>
            </div>
        </div>
    );
}

export default ViewSpecificGallery;