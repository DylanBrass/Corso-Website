import React, { useEffect, useState } from "react";
import './OurPreviousProjects.css';
import { CCarousel, CCarouselItem, CCarouselCaption, CImage } from "@coreui/react";
import 'bootstrap/dist/css/bootstrap.min.css';
import '@coreui/coreui/dist/css/coreui.min.css';
import axios from "axios";
//axios.defaults.withCredentials = true;

interface ImageData {
    galleryId: string;
    description: string;
    photo: string;
    image_position: number;
    width: number;
    height: number;
}

function OurPreviousProjects() {
    const [images, setImages] = useState<ImageData[]>([]);

    useEffect(() => {
        const fetchImages = async () => {
            try {
                const response = await axios.get(process.env.REACT_APP_BE_HOST + 'api/v1/corso/galleries/carousel');
                
                setImages(response.data);
            } catch (error) {
                
            }
        };
        fetchImages()
    }, []);

    return (
        <div className={"carousel-box"}>
            <h1>Our Previous Projects</h1>
            {images.length > 0 && (
                <CCarousel controls indicators>
                    {images
                        .sort((a, b) => a.image_position - b.image_position)
                        .map((image, index) => {
                            const aspectRatio = image.width / image.height;
                            return (
                                <CCarouselItem key={index} style={{ height: "700px" }}>
                                    <CImage
                                        src={image.photo}
                                        className="d-block w-75 h-auto mx-auto"
                                        style={{ objectFit: "cover", aspectRatio: `${aspectRatio}` }}
                                        alt={`Image ${index + 1}`}
                                    />
                                    <CCarouselCaption>
                                        <h3>{image.description}</h3>
                                    </CCarouselCaption>
                                </CCarouselItem>
                            );
                        })}
                </CCarousel>
            )}
        </div>
    );
}

export default OurPreviousProjects;
