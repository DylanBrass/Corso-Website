import React from 'react';
import './Footer.css';
import instagramIcon from "../../ressources/images/Insta-Logo.png";
import emailIcon from "../../ressources/images/Email-Logo.png";
import phoneIcon from "../../ressources/images/Phone-Logo.png";

function Footer() {
    return (
        <footer className="footer-container text-white">
            <div className="container-fluid container-fluid-footer">
                <div className="row row-footer">
                    {/* Mission Statement */}
                    <div className="col-md-4 col-md-4-footer">
                        <img src="https://res.cloudinary.com/dszhbawv7/image/upload/v1702592644/Logo-color_cdb8cu.png" alt="Company Logo" className="img-fluid logo-img"/>
                        <h5>Our Mission</h5>
                        <p>Elevating Spaces to New Heights of Luxury through Unparalleled Electrical Expertise and Craftsmanship.</p>
                        <img src="https://res.cloudinary.com/dszhbawv7/image/upload/v1702592644/Trophy_uztbrj.png" alt="Trophy Icon" className="t-img"/>
                    </div>

                    {/* Value Statement */}
                    <div className="col-md-5 col-md-5-footer">
                        <h5>Value Statement</h5>
                        <ul>
                            <li>Excellence: We strive for nothing less than excellence in all we do.</li>
                            <li>Innovation: We embrace cutting-edge technology and ideas to lead in luxury electrical solutions.</li>
                            <li>Integrity: We operate with the utmost integrity, honesty, and transparency.</li>
                            <li>Quality: We are committed to delivering the highest quality work and services.</li>
                            <li>Customer-Centric: We prioritize our clients' needs and satisfaction in every project.</li>
                            <li>Sustainability: We consider the environmental impact and promote sustainable electrical solutions.</li>
                        </ul>
                        <h5>Vision Statement</h5>
                        <p>To become the foremost authority in high-end electrical craftsmanship, setting the standard for luxury, innovation, and excellence in every project we undertake.</p>
                    </div>

                    {/* Contact Us */}
                    <div className="col-md-3 col-md-3-footer">
                        <h5>Contact Us!</h5>

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
                                    <a href="https://www.instagram.com/corsoelectriqueinc/" target="_blank" rel="noopener noreferrer">Take Me There!</a>

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
