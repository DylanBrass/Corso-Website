import React, {useState} from "react";
import Cloudinary from "../../../../Services/Cloudinary";
import axios from "axios";
import {useAuth} from "../../../../security/Components/AuthProvider";
import './AddGallery.css';
import templateImg from "../../../../ressources/images/Upload_Image_Preview.png";
import {useTranslation} from "react-i18next";
import {createJSONForLanguages} from "../../../../Services/TranslationTools";
import {SyncLoader} from "react-spinners";
import '../../../globalStyling/globalStyling.css';

function AddGallery({fetchGalleries}: { fetchGalleries: () => void }) {

    const [newImagePreview, setNewImagePreview] = useState('')


    const auth = useAuth();
    const {t} = useTranslation();

    const [isLoading, setIsLoading] = useState<boolean>(false);

    const postNewGallery = (event: any) => {

        event.preventDefault();

        setIsLoading(true)

        let englishDesc = (document.getElementById("description_en") as HTMLInputElement)?.value
        let frenchDesc = (document.getElementById("description_fr") as HTMLInputElement)?.value

        axios.post(process.env.REACT_APP_BE_HOST + 'api/v1/corso/galleries/carousel', {
            description: createJSONForLanguages(["fr","en"],[frenchDesc,englishDesc]),
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
                        (document.getElementById("description_en") as HTMLInputElement).value = "";
                        (document.getElementById("description_fr") as HTMLInputElement).value = "";
                        (document.getElementById("image") as HTMLInputElement).value = '';
                        setNewImagePreview(templateImg);

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
            }).finally(() =>
            setIsLoading(false)

        )

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
                            <br />
                            <br />
                            <label htmlFor="description">Description in English</label>
                            <textarea className={"form-control"} id="description_en" rows={3}
                                      maxLength={120}
                                      placeholder={"Enter a description in English"}
                                      required/>

                            <br />
                            <label htmlFor="description">Description en français</label>
                            <textarea className={"form-control"} id="description_fr" rows={3}
                                      maxLength={120}
                                      placeholder={t("Entrez une description en français")}
                                      required/>
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
                                       required={true}
                                       accept=".jpg, .jpeg, .png, .svg"
                                       className={"form-control-file"}
                                       onChange={(event) =>
                                           Cloudinary.handleImageChange(event, setNewImagePreview, 1920, 1080)}
                                       id="image"/>
                            </label>
                            <p>{t("editGalleryPage.tip")}</p>
                            {
                                newImagePreview ?
                                    <img src={newImagePreview} alt={"preview"} className={"img-fluid border"} />
                                    :
                                    <img src={templateImg} alt={"preview"} className={"img-fluid border"}/>
                            }

                        </div>
                        {isLoading ? "" :
                            <button type="submit" className={"btn btn-style w-50 m-auto mb-4 mt-4"}>{t("submitButton")}</button>
                        }

                        <div className={isLoading ? "d-block spinner-visible text-center" : "d-none"}>
                            <SyncLoader className={"spinner"} color="#054AEB"/>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
}

export default AddGallery;