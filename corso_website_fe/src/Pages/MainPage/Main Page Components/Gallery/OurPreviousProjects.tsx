import React, { useEffect, useState } from "react";
import './OurPreviousProjects.css';
import axios from "axios";
import { useTranslation } from "react-i18next";
import { Swiper, SwiperSlide } from 'swiper/react';

import 'swiper/css';
import 'swiper/css/navigation';
import 'swiper/css/pagination';
import 'swiper/css/effect-coverflow';
import 'swiper/css/autoplay';


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
    const {t} = useTranslation();

    useEffect(() => {
        const fetchImages = async () => {
            try {
                const response = await axios.get<ImageData[]>(process.env.REACT_APP_BE_HOST + 'api/v1/corso/galleries/carousel');

                const sortedImages = response.data.sort((a, b) => a.image_position - b.image_position);
                setImages(sortedImages);
            } catch (error) {

            }
        };
        fetchImages()
    }, []);

    return (
        <div className={"Gallery-landing-page"}>
            <div className={"row"}>
                <div className={"col-12 d-flex justify-content-center"}>
                    <h1>{t("previousProjectsPart1")} <span className={"blue-header"}>{t("previousProjectsPart2")}</span></h1>
                </div>
            </div>

            <Swiper
                autoplay={{
                    delay: 2000,
                    disableOnInteraction: false,
                }}
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
                pagination={{el: '.swiper-pagination', clickable: true}}
                navigation={{
                    nextEl: '.swiper-button-next',
                    prevEl: '.swiper-button-prev',
                    clickable: true,
                }}
                className="swiper_container"
            >
                {images.map((image) => (
                    <SwiperSlide key={image.galleryId}>

                        <div className={"wrapper"} style={{
                            backgroundImage: `url(${image.photo})`,
                            backgroundSize: 'cover',
                            backgroundPosition: 'center',
                            width: '80%',
                            height: '20rem',
                            margin: 'auto',
                            borderRadius: '3px'

                        }}>
                            <div className={"overlay"}>
                                <h2 className={"title-z-stack"}>Project</h2>
                                <p className={"description-z-stack"}>{image.description}</p>
                            </div>

                        </div>

                    </SwiperSlide>
                ))}
            </Swiper>
        </div>
    );
}

export default OurPreviousProjects;
