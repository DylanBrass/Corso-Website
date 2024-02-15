import React, {useEffect, useState} from "react";
import axios from "axios";
import {DragDropContext, Draggable, Droppable, DropResult} from 'react-beautiful-dnd';
import {useAuth} from "../../../security/Components/AuthProvider";
import NavigationBar from "../../../Components/NavBar/NavigationBar";
import './EditGallery.css';
import swal from "sweetalert";
import AddGallery from "./Components/AddGallery";
import {useTranslation} from "react-i18next";
import {getValuesFromJSON} from "../../../Services/TranslationTools";
import i18n from "i18next";
import BackButton from "../../../Components/BackButton";
import '../../globalStyling/globalStyling.css';


interface ImageData {
    galleryId: string;
    description: string;
    photo: string;
    image_position: number;
    width: number;
    height: number;
}

const reorder = (list: ImageData[], startIndex: number, endIndex: number): ImageData[] => {
    const result = Array.from(list);
    const [removed] = result.splice(startIndex, 1);
    result.splice(endIndex, 0, removed);
    return result;
};

interface DraggableImageProps {
    image: ImageData;
    index: number;
    moveImage: (fromIndex: number, toIndex: number) => void;
}

function DraggableImage({image, index, moveImage}: DraggableImageProps) {
    const {t} = useTranslation();
    function truncateDescription(description: string, wordLimit: number): string {
        const wordsArray = description.split(' ');
        if (wordsArray.length > wordLimit) {
            return wordsArray.slice(0, wordLimit).join(' ') + '...';
        } else {
            return description;
        }
    }

    return (
        <Draggable key={image.galleryId} draggableId={image.galleryId} index={index}>
            {(provided, snapshot) => (
                <div
                    className={"gallery-item col-12 col-lg-2 m-1 mx-3"}
                    ref={provided.innerRef}
                    {...provided.draggableProps}
                    {...provided.dragHandleProps}
                    style={getItemStyle(
                        snapshot.isDragging,
                        provided.draggableProps.style,
                    )
                    }
                >
                    <div className={"row mb-auto"}
                         style={{
                             position: 'relative',
                             zIndex: 100,
                             width: '100%',
                             top: 0,
                         }}
                    >
                        {index !== 0 ?
                            <button
                                style={{
                                    backgroundColor: 'transparent',
                                    border: 'none',
                                    outline: 'none',
                                    cursor: 'pointer',
                                    fontSize: '20px',
                                }}
                                className={"col-2"}
                                onClick={() => {
                                    moveImage(index, index - 1);
                                }
                                }>
                                &#11014;

                            </button>
                            :
                            <div className={"col-2"}/>
                        }

                        <span className={"col-8 text-center"}><b>Position: {image.image_position}</b></span>


                        <button
                            className={"col-2"}
                            style={{
                                backgroundColor: 'transparent',
                                border: 'none',
                                outline: 'none',
                                cursor: 'pointer',
                                fontSize: '20px',
                            }}
                            onClick={() => {
                                moveImage(index, index + 1);
                            }
                            }>
                            &#11015;
                        </button>


                    </div>
                    <img
                        className={"mt-4"
                        }
                        src={image.photo} alt={image.description} style={{
                        width: '200px',
                        backgroundColor: 'black',
                        opacity: snapshot.isDragging ? 0.4 : 1,
                    }}/>
                    <p className={"mt-4 w-100 overflow-scroll"}
                       style={{
                           maxWidth: '150px',
                       }}
                    >
                        {truncateDescription( getValuesFromJSON(i18n.language, image.description), 10)}
                    </p>
                    <button className={"btn-style"} onClick={() => {
                        moveImage(index, 0);
                    }}>{t("editGalleryPage.moveToTop")}
                    </button>

                    <button
                        className={"btn-style mt-2"}
                        onClick={() => {
                            window.location.href = `/manage/gallery/${image.galleryId}`;
                        }}>{t("editGalleryPage.edit")}
                    </button>
                </div>
            )}
        </Draggable>
    );
}

function EditGallery() {
    const [images, setImages] = useState<ImageData[]>([]);
    const [isSubmitClicked, setIsSubmitClicked] = useState(false);
    const [isSmallScreen, setIsSmallScreen] = useState(window.innerWidth <= 992);
    const auth = useAuth();
    const {t} = useTranslation();

    useEffect(() => {
        // @ts-ignore
        if (!auth.isAuthenticated || !auth.userRoles().includes('Admin')) {
            window.location.href = '/';
        }
    }, [auth]);

    useEffect(() => {
        const handleResize = () => {
            setIsSmallScreen(window.innerWidth <= 992);
        };

        window.addEventListener('resize', handleResize);

        return () => {
            window.removeEventListener('resize', handleResize);
        };
    }, []);

    const fetchImages = async () => {
        try {
            const response = await axios.get(process.env.REACT_APP_BE_HOST + 'api/v1/corso/galleries/carousel');
            if (response.status !== 200) {
                // @ts-ignore
                auth.authError(response.status);
            }
            const sortedImages = response.data.sort((a: ImageData, b: ImageData) => a.image_position - b.image_position);
            setImages(sortedImages);
        } catch (error) {

        }
    };

    useEffect(() => {

        fetchImages();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    const moveImage = (fromIndex: number, toIndex: number) => {
        setImages((prevImages) => {
            const updatedImages = reorder(prevImages, fromIndex, toIndex);
            updateImagePositions(updatedImages);
            return updatedImages;
        });
    };

    const updateImagePositions = (updatedImages: ImageData[]) => {
        updatedImages.forEach((image, index) => {
            image.image_position = index + 1;
        });
    };

    const handleSubmit = async () => {
        try {
            await axios.patch(
                process.env.REACT_APP_BE_HOST + 'api/v1/corso/galleries/carousel/order',
                images.map((image) => ({
                    galleryId: image.galleryId,
                    image_position: image.image_position,
                })),
                {
                    headers: {
                        // @ts-ignore
                        'X-XSRF-TOKEN': auth.getXsrfToken(),
                    },
                }
            ).then(r => {
                if (r.status === 200) {

                    swal(t("alerts.gallery.carouselUpdatedTitle"), t("alerts.gallery.carouselUpdatedMessage"), "success")

                }
            })
                .catch(e => {
                    swal(t("alerts.error"), e.response.data.message, "error")
                        .then(() => {
                            // @ts-ignore
                            auth.authError(e.response.status)
                        })
                })

            const getResponse = await axios.get(process.env.REACT_APP_BE_HOST + 'api/v1/corso/galleries/carousel ');
            const sortedImages = getResponse.data.sort((a: ImageData, b: ImageData) => a.image_position - b.image_position);
            setImages(sortedImages);
            setIsSubmitClicked(true);
        } catch (error) {

        }
    };

    const onDragEnd = (result: DropResult) => {
        if (!result.destination) {
            return;
        }

        moveImage(result.source.index, result.destination.index);
    };

    useEffect(() => {
        if (isSubmitClicked) {

            setIsSubmitClicked(false);
        }
    }, [isSubmitClicked, images]);

    const backButtonHref = "/dashboard";

    return (
        <div>
            <NavigationBar/>
            <BackButton
                link={backButtonHref}
                text={ t("backToDashboard")}
            />
            <div className="gallery-container">
                <h1>{t("editGalleryPage.editGallery")}</h1>
                <div className={"overflow-scroll w-100"}>
                    <DragDropContext onDragEnd={onDragEnd}>
                        <Droppable droppableId="gallery" direction={isSmallScreen ? 'vertical' : 'horizontal'}>
                            {(provided) => (
                                <div
                                    className={"gallery-wrapper"}
                                    ref={provided.innerRef}
                                    style={isSmallScreen ? getListStyleVertical() : getListStyleHorizontal()}
                                    {...provided.droppableProps}
                                >
                                    {images.map((image, index) => (
                                        <DraggableImage
                                            key={index}
                                            image={image}
                                            index={index}
                                            moveImage={moveImage}
                                        />
                                    ))}
                                    {provided.placeholder}

                                </div>
                            )}

                        </Droppable>
                    </DragDropContext>
                </div>
                <button onClick={handleSubmit} className="btn-style mt-2">
                    {t("editGalleryPage.saveOrder")}
                </button>
            </div>
            <hr/>

            <AddGallery
                fetchGalleries={fetchImages}
            />
        </div>
    );
}

const getListStyleVertical = () => ({
    background: 'white',
    padding: 20,
    height: "100%",
    maxHeight: "100vh",
    overflow: 'auto',
    width: "100%",

});

const getListStyleHorizontal = () => ({
    background: 'white',
    display: 'flex',
    padding: 20,
    height: "100%",
    width: "100% !important",


});

const getItemStyle = (isDragging: boolean, draggableStyle: any) => ({
        userSelect: 'none',
        padding: 20,
        minWidth: '200px',
        background: isDragging ? 'rgba(115,115,115,0.4)' : 'white',
        borderRadius: '10px',
        border: isDragging ? '2px dashed white' : '',
        boxShadow: isDragging ? 'none' : '0 0 5px rgba(0,0,0,0.4)',

        ...draggableStyle,
    })
;

export default EditGallery;
