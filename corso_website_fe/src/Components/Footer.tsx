import React from 'react';
import './Footer.css'; // Ensure you create a Footer.css file with the provided CSS

function Footer() {
    return (
        <footer className="footer-container text-white">
            <div className="container">
                <div className="row">
                    {/* Mission Statement */}
                    <div className="col-md-4">
                        <h5>Our Mission</h5>
                        <p>Elevating Spaces to New Heights of Luxury through Unparalleled Electrical Expertise and Craftsmanship.</p>
                    </div>

                    {/* Value Statement */}
                    <div className="col-md-4">
                        <h5>Value Statement</h5>
                        <ul>
                            <li>Excellence: We strive for nothing less than excellence in all we do.</li>
                            <li>Innovation: We embrace cutting-edge technology and ideas to lead in luxury electrical solutions.</li>
                            <li>Integrity: We operate with the utmost integrity, honesty, and transparency.</li>
                            <li>Quality: We are committed to delivering the highest quality work and services.</li>
                            <li>Customer-Centric: We prioritize our clients' needs and satisfaction in every project.</li>
                            <li>Sustainability: We consider the environmental impact and promote sustainable electrical solutions.</li>
                        </ul>
                    </div>

                    {/* Contact Us */}
                    <div className="col-md-4">
                        <h5>Contact Us!</h5>
                        <p><strong>Phone:</strong> (514) 703-8552</p>
                        <p><strong>Email:</strong> corsoelectriqueinc@gmail.com</p>
                        <p><strong>Instagram</strong></p>
                    </div>
                </div>
            </div>
        </footer>
    );
}

export default Footer;
