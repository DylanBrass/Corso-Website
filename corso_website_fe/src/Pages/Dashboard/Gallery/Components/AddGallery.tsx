import React, {useState} from "react";
import Cloudinary from "../../../../Services/Cloudinary";
import axios from "axios";
import {useAuth} from "../../../../security/Components/AuthProvider";
import './AddGallery.css';
import templateImg from "../../../../ressources/images/Upload_Image_Preview.png";
import {useTranslation} from "react-i18next";

function AddGallery({fetchGalleries}: { fetchGalleries: () => void }) {

    const [newImagePreview, setNewImagePreview] = useState('')


    const auth = useAuth();
    const {t} = useTranslation();

    const postNewGallery = (event: any) => {

        event.preventDefault();

        axios.post(process.env.REACT_APP_BE_HOST + 'api/v1/corso/galleries/carousel', {
            description: event.target.description.value,
            photo: newImagePreview
        }, {
            headers: {
                // @ts-ignore
                'X-XSRF-TOKEN': auth.getXsrfToken(),
            },
        })
            .then((r) => {
                if (r.status === 201) {
                    // @ts-ignore
                    swal(t("alerts.gallery.galleryCreatedTitle"), (t("alerts.gallery.galleryCreatedMessage")), 'success').then(() => {
                        fetchGalleries();
                    })

                }
            })
            .catch((e) => {
                // @ts-ignore
                swal((t("alerts.error")), e.response.data.message, 'error').then(() => {
                    // @ts-ignore
                    auth.authError(e.response.status);
                });
            });


    }

    return (
        <div className="add-gallery-page">
            <div className={"add-gallery-container"}>
                <div className={"add-gallery-form text-center"}>
                    <h1>{t("editGalleryPage.addGallerySection.addGallery")}</h1>
                    <form className={"row w-75 m-auto"} onSubmit={postNewGallery}>
                        <div className={"form-group col-12 col-md-6 add-image-box-left"}
                        >
                            <h3 className={"float-start"}>{t("editGalleryPage.addGallerySection.step")} 1:</h3>

                            <label htmlFor="description">Description</label>
                            <textarea className={"form-control"} id="description" rows={3}
                                      placeholder={t("enterDescription")}/>
                        </div>
                        <div className={"form-group col-12 col-md-6 add-image-box-right"}
                             style={{
                                 height: "50vh"
                             }}
                        >
                            <h3>{t("editGalleryPage.addGallerySection.step")} 2:</h3>
                            <label id={"button-for-file"} htmlFor="image">Image
                                <input type="file"
                                       style={{
                                           display: "none",
                                       }}
                                       accept=".jpg, .jpeg, .png, .svg"
                                       className={"form-control-file"}
                                       onChange={(event) => Cloudinary.handleImageChange(event, setNewImagePreview, 1920, 1080)}
                                       id="image"/>
                            </label>
                            <p>{t("editGalleryPage.tip")}</p>
                            {
                                newImagePreview ?
                                    <img src={newImagePreview} alt={"preview"} className={"img-fluid border m-3"} />
                                    :
                                    <img src={templateImg} alt={"preview"} className={"img-fluid border m-3"}/>
                            }

                        </div>
                        <button type="submit" className={"btn btn-primary w-25 m-auto mb-4 mt-4"}>{t("submitButton")}</button>
                    </form>
                </div>
            </div>
        </div>
    );
}

export default AddGallery;