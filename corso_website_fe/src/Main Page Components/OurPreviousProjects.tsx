import React, { useEffect, useState } from "react";
import './OurPreviousProjects.css';
import { CCarousel, CCarouselItem, CCarouselCaption, CImage } from "@coreui/react";
import 'bootstrap/dist/css/bootstrap.min.css';
import '@coreui/coreui/dist/css/coreui.min.css';
import axios from "axios";
import {useAuth0} from "@auth0/auth0-react";

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
    const { user, isAuthenticated, getAccessTokenSilently } = useAuth0();
    const [userMetadata, setUserMetadata] = useState(null);
    let accessToken = "";
    const getUserMetadata = async () => {
        const domain = "dev-7k6npylc7qks07rv.us.auth0.com";

        try {
            accessToken = await getAccessTokenSilently({
                authorizationParams: {
                    audience: `https://${domain}/api/v2/`,
                    scope: "read:current_user",
                },
            });

            // @ts-ignore
            const userDetailsByIdUrl = `https://${domain}/api/v2/users/${user.sub}`;

            const metadataResponse = await fetch(userDetailsByIdUrl, {
                headers: {
                    Authorization: `Bearer ${accessToken}`,
                },
            });

            const { user_metadata } = await metadataResponse.json();

            setUserMetadata(user_metadata);
        } catch (e:any) {
            console.log(e.message);
        }
    };


    useEffect(() => {
        const fetchImages = async () => {
            try {
                await getUserMetadata();
                const response = await axios.get('http://localhost:8080/api/v1/corso/galleries/carousel');
                console.log('Response:', response.data)
                setImages(response.data);
            } catch (error) {
                console.error('Error fetching images:', error);
            }
        };
        fetchImages().then(r => console.log('Images fetched'));
    }, []);

    return (
        <div>
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
