import React, { useEffect, useState } from "react";
import './OurPreviousProjects.css';
import axios from "axios";
import { useTranslation } from "react-i18next";
import Carousel from 'react-multi-carousel';
import 'react-multi-carousel/lib/styles.css';
import {getValuesFromJSON} from "../../../../Services/TranslationTools";
import i18n from "i18next";

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
    const { t } = useTranslation();

    useEffect(() => {
        const fetchImages = async () => {
            try {
                const response = await axios.get<ImageData[]>(`${process.env.REACT_APP_BE_HOST}api/v1/corso/galleries/carousel`);
                const sortedImages = response.data.sort((a, b) => a.image_position - b.image_position);
                setImages(sortedImages);
            } catch (error) {
                console.error("API Error:", error);
            }
        };
        fetchImages();
    }, []);

    function truncateDescription(description: string, wordLimit: number): string {
        const wordsArray = description.split(' ');
        if (wordsArray.length > wordLimit) {
            return wordsArray.slice(0, wordLimit).join(' ') + '...';
        } else {
            return description;
        }
    }

    const responsive = {
        superLargeDesktop: {
            breakpoint: { max: 4000, min: 3000 },
            items: 5
        },
        desktop: {
            breakpoint: { max: 3000, min: 1024 },
            items: 3
        },
        tablet: {
            breakpoint: { max: 1024, min: 464 },
            items: 2
        },
        mobile: {
            breakpoint: { max: 464, min: 0 },
            items: 1
        }
    };

    return (
        <div className="Gallery-landing-page">
            <div className="row row-carousel">
                <div className="col-12 d-flex justify-content-center">
                    <h1>{t("mainPage.previousProjectsPart1")} <span className="blue-header">{t("mainPage.previousProjectsPart2")}</span></h1>
                </div>
            </div>
            <div className="row row-carousel">
                <div className="col-12 d-flex ">
                    <p className="subtitle-carousel">{t("mainPage.pastProjectsBlurb")}</p>
                </div>
            </div>
            <Carousel
                responsive={responsive}
                infinite={true}
                autoPlay={true}
                autoPlaySpeed={3000}
                keyBoardControl={true}
                customTransition="transform 300ms ease-in-out"
                transitionDuration={500}
                showDots={true}
                containerClass="carousel-container"
                itemClass="carousel-item-padding-40-px"
                dotListClass="custom-dot-list-style"
            >
                {images.map((image) => (
                    <div key={image.galleryId} className="wrapper" style={{
                        backgroundImage: `url(${image.photo})`,
                        backgroundSize: 'cover',
                        backgroundPosition: 'center',
                        width: '100%',
                        height: '20rem',
                        marginBottom: '1.5rem',
                        borderRadius: '3px',
                        transition: 'transform 0.5s ease-in-out'
                    }}>
                        <div className="overlay">
                            <h2 className="title-z-stack">{t("mainPage.project")}</h2>
                            <p className="description-z-stack">
                                {truncateDescription( getValuesFromJSON(i18n.language, image.description), 15)}
                            </p>
                        </div>
                    </div>
                ))}
            </Carousel>
        </div>
    );
}

export default OurPreviousProjects;
