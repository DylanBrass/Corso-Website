import React from 'react';
import './Footer.css';
import instagramIcon from "../../ressources/images/Insta-Logo.png";
import emailIcon from "../../ressources/images/Email-Logo.png";
import phoneIcon from "../../ressources/images/Phone-Logo.png";
import {useTranslation} from "react-i18next";

function Footer() {
    const {t} = useTranslation();

    return (
        <footer className="footer-container text-white">
            <div className="container-fluid container-fluid-footer">
                <div className="row row-footer">
                    {/* Mission Statement */}
                    <div className="col-md-4 col-md-4-footer">
                        <img src="https://res.cloudinary.com/dszhbawv7/image/upload/v1702592644/Logo-color_cdb8cu.png" alt="Company Logo" className="img-fluid logo-img"/>
                        <h5>{t("footer.ourMission")}</h5>
                        <p>{t("footer.ourMissionBlurb")}</p>
                        <img src="https://res.cloudinary.com/dszhbawv7/image/upload/v1702592644/Trophy_uztbrj.png" alt="Trophy Icon" className="t-img"/>
                    </div>

                    {/* Value Statement */}
                    <div className="col-md-5 col-md-5-footer">
                        <h5>{t("footer.valueStatement")}</h5>
                        <ul>
                            {(t("footer.valueStatements", { returnObjects: true }) as string[]).map((statement: string, index: number) => (
                                <li key={index}>{statement}</li>
                            ))}
                        </ul>
                        <h5>{t("footer.visionStatement")}</h5>
                        <p>{t("footer.visionStatementText")}</p>
                    </div>

                    {/* Contact Us */}
                    <div className="col-md-3 col-md-3-footer">
                        <h5>{t("footer.contactUs")}</h5>

                        <div className="row">
                            <div className="col-3">
                                <img src={phoneIcon} alt="Phone Icon" className="img-footer"/>
                            </div>
                            <div className="col-9">
                                <div className="centered-content">
                                    <p>(514) 703-8552</p>
                                </div>
                            </div>
                        </div>

                        <div className="row">
                            <div className="col-3">
                                <img src={emailIcon} alt="Email Icon" className="img-footer"/>
                            </div>
                            <div className="col-9">
                                <div className="centered-content">
                                    <p className={"text-break"}>corsoelectriqueinc@gmail.com</p>
                                </div>
                            </div>
                        </div>

                        <div className="row">
                            <div className="col-3">
                                <img src={instagramIcon} alt="Instagram Logo" className="img-footer"/>
                            </div>
                            <div className="col-9">
                                <div className="centered-content">
                                    <a href="https://www.instagram.com/corsoelectriqueinc/" target="_blank" rel="noopener noreferrer">{t("footer.takeMeThere")}</a>

                                </div>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </footer>
    );
}

export default Footer;
