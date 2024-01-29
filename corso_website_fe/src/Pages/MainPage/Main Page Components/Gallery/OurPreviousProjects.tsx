import React, { useEffect, useState } from "react";
import './OurPreviousProjects.css';
import axios from "axios";
import { useTranslation } from "react-i18next";
import { Swiper, SwiperSlide } from 'swiper/react';
import 'swiper/css';
import 'swiper/css/navigation';
import 'swiper/css/pagination';
import 'swiper/css/effect-coverflow';


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
                const response = await axios.get(process.env.REACT_APP_BE_HOST + 'api/v1/corso/galleries/carousel');
                
                setImages(response.data);
            } catch (error) {
                
            }
        };
        fetchImages()
    }, []);

    return (
        <div className={"Gallery-landing-page"}>
            <div className={"row"}>
                <div className={"col-md-6 col-12 d-flex justify-content-md-end justify-content-center"}>
                    <h1>{t("previousProjectsPart1")}</h1>
                </div>
                <div className={"col-md-6 col-12 d-flex justify-content-md-start justify-content-center"}>
                    <h1 className={"blue-header"}>{t("previousProjectsPart2")}</h1>
                </div>
            </div>

            <Swiper
                effect={'coverflow'}
                grabCursor={true}
                centeredSlides={true}
                loop={true}
                slidesPerView={'auto'}
                coverflowEffect={{
                    rotate: 0,
                    stretch: 0,
                    depth: 100,
                    modifier: 2.5,
                }}
                pagination={{ el: '.swiper-pagination', clickable: true }}
                navigation={{
                    nextEl: '.swiper-button-next',
                    prevEl: '.swiper-button-prev',
                    clickable: true,
                }}
                className="swiper_container"
            >
                {images.map((image) => (
                    <SwiperSlide key={image.galleryId}>
                        <img src={image.photo} alt={image.description} />
                        <h3>{image.description}</h3>
                    </SwiperSlide>
                ))}

            </Swiper>
        </div>
    );
}

export default OurPreviousProjects;
