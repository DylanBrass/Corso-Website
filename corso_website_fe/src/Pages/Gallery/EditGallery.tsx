import React, { useState, useEffect } from "react";
import axios from "axios";
import { DragDropContext, Droppable, Draggable, DropResult } from 'react-beautiful-dnd';
import { useAuth } from "../../security/Components/AuthProvider";
import NavigationBar from "../../Components/NavBar/NavigationBar";
import './EditGallery.css';
import swal from "sweetalert";

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

function DraggableImage({ image, index, moveImage }: DraggableImageProps) {
    const fixedWidth = 250; // Specify your desired fixed width

    return (
        <Draggable key={image.galleryId} draggableId={image.galleryId} index={index}>
            {(provided, snapshot) => (
                <div
                    ref={provided.innerRef}
                    {...provided.draggableProps}
                    {...provided.dragHandleProps}
                    style={getItemStyle(
                        snapshot.isDragging,
                        provided.draggableProps.style
                    )}
                    className="gallery-item"
                >
                    <p><b>Position: {image.image_position}</b></p>
                    <img src={image.photo} alt={image.description} style={{ width: `${fixedWidth}px` }} />
                    <p>{image.description}</p>
                </div>
            )}
        </Draggable>
    );
}

function EditGallery() {
    const [images, setImages] = useState<ImageData[]>([]);
    const [isSubmitClicked, setIsSubmitClicked] = useState(false);
    const [isSmallScreen, setIsSmallScreen] = useState(window.innerWidth <= 800);
    const auth = useAuth();

    useEffect(() => {
        // @ts-ignore
        if (!auth.isAuthenticated || !auth.userRoles().includes('Admin')) {
            window.location.href = '/';
        }
    }, [auth]);

    useEffect(() => {
        const handleResize = () => {
            setIsSmallScreen(window.innerWidth <= 800);
        };

        window.addEventListener('resize', handleResize);

        return () => {
            window.removeEventListener('resize', handleResize);
        };
    }, []);

    useEffect(() => {
        const fetchImages = async () => {
            try {
                const response = await axios.get(process.env.REACT_APP_BE_HOST+'api/v1/corso/galleries/carousel');
                const sortedImages = response.data.sort((a: ImageData, b: ImageData) => a.image_position - b.image_position);
                setImages(sortedImages);
            } catch (error) {
                
            }
        };
        fetchImages();
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
                process.env.REACT_APP_BE_HOST+'api/v1/corso/galleries/carousel/order',
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

                    swal("Carousel updated!", "The carousel has been updated successfully!", "success")

                }
            })
                .catch(e => {
                    swal("Error!", e.response.data.message, "error")
                        .then(() => {
                            // @ts-ignore
                            auth.authError(e.response.status)
                        })
                })

            const getResponse = await axios.get(process.env.REACT_APP_BE_HOST+'api/v1/corso/galleries/carousel ');
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

    return (
        <div>
            <NavigationBar />
            <div className="gallery-container">
                <h1>Edit Gallery</h1>
                <DragDropContext onDragEnd={onDragEnd}>
                    <Droppable droppableId="gallery" direction={isSmallScreen ? 'vertical' : 'horizontal'}>
                        {(provided, snapshot) => (
                            <div
                                ref={provided.innerRef}
                                style={isSmallScreen ? getListStyleVertical(snapshot.isDraggingOver) : getListStyleHorizontal(snapshot.isDraggingOver)}
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
                <button onClick={handleSubmit} className="save-order-button">
                    Save Order
                </button>
            </div>
        </div>
    );
}

const getListStyleVertical = (isDraggingOver: boolean) => ({
    background: isDraggingOver ? 'lightblue' : 'lightgrey',
    padding: 20,
    height: "100%", // Adjusted to take up the full height
    overflow: 'auto',
});

const getListStyleHorizontal = (isDraggingOver: boolean) => ({
    background: isDraggingOver ? 'lightblue' : 'lightgrey',
    display: 'flex',
    padding: 20,
    overflow: 'auto',
});

const getItemStyle = (isDragging: boolean, draggableStyle: any) => ({
    userSelect: 'none',
    padding: 20,
    background: isDragging ? 'black' : 'grey',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    color: 'white',
    ...draggableStyle,
});

export default EditGallery;
