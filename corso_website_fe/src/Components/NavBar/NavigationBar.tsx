import React from 'react';
import { Navbar, Container, Nav} from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import './NavigationBar.css';
import Cookies from "js-cookie";
import {useAuth} from "../../security/Components/AuthProvider";
import {Link} from "react-router-dom";

function NavigationBar() {
    const auth = useAuth()
    return (
        <Navbar expand="lg" className="navbar-custom">
            <Container fluid>
                <Navbar.Brand href="/">
                    <img src="https://res.cloudinary.com/dszhbawv7/image/upload/v1701844105/customcolor_logo_transparent_background_x5ensc.png" alt="Logo"/>
                </Navbar.Brand>
                <Navbar.Toggle aria-controls="navbarNav" />
                <Navbar.Collapse id="navbarNav" className="navbar-collapse">
                    <Nav className="ms-auto">
                        {
                            // @ts-ignore
                            auth.userRoles().includes("Admin") &&
                            <Nav.Item className={"item"}>
                                <Nav.Link href="/dashboard">DASHBOARD</Nav.Link>
                            </Nav.Item>
                        }
                        {
                            // @ts-ignore
                            (auth.userRoles().includes("Customer") || !auth.isAuthenticated) &&

                            <Nav.Item className={"item"}>
                                <Nav.Link href="/faq">FAQ</Nav.Link>
                            </Nav.Item>
                        }
                        <Nav.Item className={"item"}>

                            <Nav.Link href="/" active>
                                HOME
                            </Nav.Link>
                        </Nav.Item>
                        <Nav.Item className={"item"}>
                            <Nav.Link href="#">FRANÇAIS</Nav.Link>
                        </Nav.Item>


                        {
                            //@ts-ignore
                            !auth.isAuthenticated &&
                        <Nav.Item className={"item"}>
                            <Nav.Link href={process.env.REACT_APP_BE_HOST + `oauth2/authorization/okta`}>LOGIN</Nav.Link>
                        </Nav.Item>
                        }
                        {
                            //@ts-ignore
                            auth.isAuthenticated &&
                            <Nav.Item className={"item"}>
                                <form method={"post"} action={ process.env.REACT_APP_BE_HOST +"api/v1/corso/logout"}>
                                    <button id={"logout-btn"} type={"submit"}>LOGOUT</button>
                                </form>
                            </Nav.Item>
                        }
                        <Nav.Item className={"item"}>
                            {
                                Cookies.get("picture") != null &&
                                       // @ts-ignore
                                auth.userRoles().includes("Customer") &&
                                <Link id={"to-profile"} to={"/profile"}>
                         <img src={Cookies.get("picture")}
                                        alt="See Profile" id={"profile-image"} className={"d-none d-lg-block"} />
                                </Link>
                            }
                    </Nav.Item>
                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
}

export default NavigationBar;
