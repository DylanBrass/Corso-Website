import React from 'react';
import {Container, Nav, Navbar} from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import './NavigationBar.css';
import Cookies from "js-cookie";
import {useAuth} from "../../security/Components/AuthProvider";
import {Link} from "react-router-dom";

function NavigationBar() {
    const auth = useAuth()
    return (
        <div id={"nav-bar-box"}>
            <Navbar expand="lg" className="navbar-custom">
                <Container fluid>
                    <Navbar.Brand href="/">
                        <img
                            id={"logo"}
                            src="https://res.cloudinary.com/dszhbawv7/image/upload/v1701844105/customcolor_logo_transparent_background_x5ensc.png"
                            alt="Logo"/>
                    </Navbar.Brand>
                    <Navbar.Toggle aria-controls="navbarNav"/>
                    <Navbar.Collapse id="navbarNav">
                        <div className={"ms-auto d-lg-flex flex-row"}>
                            {
                                // @ts-ignore
                                auth.userRoles().includes("Admin") &&
                                <Nav.Item className={"item"}>
                                    <Nav.Link href="/dashboard" className={"m-auto item-nav-text"}>DASHBOARD</Nav.Link>
                                </Nav.Item>
                            }
                            {
                                // @ts-ignore
                                (auth.userRoles().includes("Customer") || !auth.isAuthenticated) &&

                                <Nav.Item className={"item"}>
                                    <Nav.Link href="/faq" className={"m-auto item-nav-text"}>FAQ</Nav.Link>
                                </Nav.Item>
                            }
                            <Nav.Item className={"item"}>

                                <Nav.Link href="/" active className={"m-auto item-nav-text"}>
                                    HOME
                                </Nav.Link>
                            </Nav.Item>
                            <Nav.Item className={"item"}>
                                <Nav.Link href="#" className={"m-auto item-nav-text"}>FRANÇAIS</Nav.Link>
                            </Nav.Item>


                            {
                                //@ts-ignore
                                !auth.isAuthenticated &&
                                <Nav.Item className={"item"} >
                                    <Nav.Link
                                        href={process.env.REACT_APP_BE_HOST + `oauth2/authorization/okta`} className={"m-auto item-nav-text"}>LOGIN</Nav.Link>
                                </Nav.Item>
                            }
                            {
                                //@ts-ignore
                                auth.isAuthenticated &&
                                <Nav.Item className={"item"}>
                                    <form method={"post"}
                                          action={process.env.REACT_APP_BE_HOST + "api/v1/corso/logout"} className={"m-auto"}>
                                        <button id={"logout-btn"} type={"submit"}>
                                            <span className={"logout-btn-text"}>
                                                LOGOUT
                                            </span>
                                        </button>
                                    </form>
                                </Nav.Item>
                            }
                            <Nav.Item className={"item"}>
                                {
                                    Cookies.get("picture") != null &&
                                    // @ts-ignore
                                    auth.userRoles().includes("Customer") &&
                                    <Link id={"to-profile"} to={"/profile"} className={"m-auto"}>
                                        <img src={Cookies.get("picture")}
                                             alt="See Profile" id={"profile-image"} className={"d-none d-lg-block"}/>
                                        <Nav.Link href={"/profile"} id={"profile-text"} className={"d-block d-lg-none item item-nav-text"}>PROFILE</Nav.Link>
                                    </Link>
                                }
                            </Nav.Item>
                        </div>
                    </Navbar.Collapse>
                </Container>
            </Navbar>
        </div>
    );
}

export default NavigationBar;
