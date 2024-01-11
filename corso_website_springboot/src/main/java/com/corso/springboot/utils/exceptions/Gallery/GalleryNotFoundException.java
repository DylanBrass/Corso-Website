package com.corso.springboot.utils.exceptions.Gallery;

public class GalleryNotFoundException extends RuntimeException {

        public GalleryNotFoundException(String message) {
            super(message);
        }
}
