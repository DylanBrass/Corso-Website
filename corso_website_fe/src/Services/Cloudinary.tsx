import React from "react";
import swal from "sweetalert";
import i18next from "i18next";

class Cloudinary {


    convertToBase64(file: File, callback: (base64Result: any) => void) {
        const reader = new FileReader();
        reader.onloadend = () => {
            callback(reader.result as string);
        };
        reader.readAsDataURL(file);
    }



    handleImageChange(
        event: React.ChangeEvent<HTMLInputElement>,
        setImagePreview: (value: string) => void,
        maxWidthCheck: number,
        maxHeightCheck: number
    ) {
        const file = event.target.files ? event.target.files[0] : null;

        if (file) {
            // Check image dimensions
            const image = new Image();
            image.src = URL.createObjectURL(file);

            image.onload = () => {
                const maxWidth = maxWidthCheck;
                const maxHeight = maxHeightCheck;

                if (image.width > maxWidth || image.height > maxHeight) {
                    swal((i18next.t("alerts.error")), i18next.t("alerts.service.imageDimensionsError", { maxWidth, maxHeight }), 'error');
                    return;
                }

                this.convertToBase64(file, (base64Result: any) => {
                    setImagePreview(base64Result);
                });
            };
        } else {
            setImagePreview('');
        }
    };
}


// eslint-disable-next-line import/no-anonymous-default-export
export default new Cloudinary();