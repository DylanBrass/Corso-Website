import React from 'react';
import backButton from '../ressources/images/BackButton.svg';

// @ts-ignore
const BackButton = ({ link, text }) => {
    return (
        <div className="container">
            <div className="row">
                <div className="col-12 m-1 d-flex align-items-center"
                     style={{ justifyContent: "flex-start", cursor: "pointer"}}
                     onClick={() => {
                         window.location.href = link;
                     }}>
                    <img
                        src={backButton}
                        alt="back button"
                    />
                    <span className="back-btn-title">
            {text}
          </span>
                </div>
            </div>
        </div>
    );
};

export default BackButton;
