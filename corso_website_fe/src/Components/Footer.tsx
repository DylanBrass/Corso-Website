import React from 'react';
import './Footer.css';

function Footer() {
    return (
        <footer className="footer-container text-white">
            <div className="container-fluid">
                <div className="row">
                    {/* Mission Statement */}
                    <div className="col-md-4">
                        <img src="https://res.cloudinary.com/dszhbawv7/image/upload/v1702592644/Logo-color_cdb8cu.png" alt="Company Logo" className="img-fluid logo-img"/>
                        <h5>Our Mission</h5>
                        <p>Elevating Spaces to New Heights of Luxury through Unparalleled Electrical Expertise and Craftsmanship.</p>
                        <img src="https://res.cloudinary.com/dszhbawv7/image/upload/v1702592644/Trophy_uztbrj.png" alt="Trophy Icon" className="t-img"/>
                    </div>

                    {/* Value Statement */}
                    <div className="col-md-5">
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
                    <div className="col-md-3">
                        <h5>Contact Us!</h5>

                        <div className="row">
                            <div className="col-3">
                                <img src="https://res.cloudinary.com/dszhbawv7/image/upload/v1702592644/Phone_fwm41y.png" alt="Phone Icon" className="img-fluid"/>
                            </div>
                            <div className="col-9">
                                <p>(514) 703-8552</p>
                            </div>
                        </div>

                        <div className="row">
                            <div className="col-3">
                                <img src="https://res.cloudinary.com/dszhbawv7/image/upload/v1702592644/Email_rv756e.png" alt="Email Icon" className="img-fluid"/>
                            </div>
                            <div className="col-9">
                                <p>corsoelectriqueinc@gmail.com</p>
                            </div>
                        </div>

                        <div className="row">
                            <div className="col-3">
                                <img src="https://res.cloudinary.com/dszhbawv7/image/upload/v1702592644/Instagram-logo_tinm9u.png" alt="Instagram Logo" className="img-fluid"/>
                            </div>
                            <div className="col-9">
                                <p>Future link to instagram</p>
                            </div>
                        </div>

                    </div>

                </div>
            </div>
        </footer>
    );
}

export default Footer;
