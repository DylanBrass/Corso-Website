class Gallery{

    public galleryId: string;

    public description: string;

    public photo: string;

    public image_position: number;


    constructor(galleryId: string, description: string, photo: string, image_position: number) {
        this.galleryId = galleryId;
        this.description = description;
        this.photo = photo;
        this.image_position = image_position;
    }
}


export default Gallery;