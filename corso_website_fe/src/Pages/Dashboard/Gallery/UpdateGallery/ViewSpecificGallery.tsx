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

function ViewSpecificGallery() {

    const auth = useAuth();

    const [gallery, setGallery] = useState<Gallery>(new Gallery("", "", "", 0));

    const {galleryId} = useParams();

    const [imagePreview, setImagePreview] = useState<string>('');


    const getGallery = async () => {
        axios.get(`${process.env.REACT_APP_BE_HOST}api/v1/corso/galleries/carousel/${galleryId}`, {
            headers: {
                // @ts-ignore
                'X-XSRF-TOKEN': auth.getXsrfToken(),
            },
        }).then((response) => {
            setGallery(response.data);
            setImagePreview(response.data.photo);
        }).catch((error) => {
            // @ts-ignore
            auth.authError(error.response.status);
        });
    }

    useEffect(() => {
        getGallery();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);


    const updateGallery = async () => {
    }

    return (
        <div>
            <NavigationBar/>
            <div className={"manage-gallery-wrapper p-5"}>
                <h1 className={"text-center mb-4"}>Edit Gallery</h1>
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
                        <p className={"col-12"}>Tip!: compatible image types include PNG, SVG</p>
                    </div>
                    <div className={"row"}>
                        <button className={"btn btn-primary m-auto w-50"} type={"submit"}>
                            Update
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}

export default ViewSpecificGallery;