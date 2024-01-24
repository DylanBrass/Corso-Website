import React, { useState } from "react";
import Cloudinary from "../../../../Services/Cloudinary";




function AddGallery() {

    const [newImagePreview, setNewImagePreview] = useState('')




    const postNewGallery = () =>{



    }

    return (
        <div className="add-gallery-page">
            <div className={"add-gallery-container"}>
                <div className={"add-gallery-form text-center"}>
                    <h1>Add Gallery</h1>
                    <form className={"row w-75 m-auto"} onSubmit={postNewGallery}>
                        <div className={"form-group col-6"}>
                            <label htmlFor="description">Description</label>
                            <textarea className={"form-control"} id="description" rows={3}
                                      placeholder="Enter description"/>
                        </div>
                        <div className={"form-group col-6"}>
                            {
                                newImagePreview ?
                                    <img src={newImagePreview} alt={"preview"} className={"img-fluid"}/>
                                    :
                                    <div className={"placeholder-rectangle"}/>
                            }
                            <label htmlFor="image">Image</label>
                            <input type="file"
                                   accept=".jpg, .jpeg, .png, .svg"
                                   className={"form-control-file"}
                                      onChange={(event) => Cloudinary.handleImageChange(event, setNewImagePreview, 1920, 1080)}
                                   id="image"/>
                        </div>
                        <button type="submit" className={"btn btn-primary w-25 m-auto"}>Submit</button>
                    </form>
                </div>
            </div>
        </div>
    );
}

export default AddGallery;