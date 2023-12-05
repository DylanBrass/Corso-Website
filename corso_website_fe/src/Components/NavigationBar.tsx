import React from 'react';
import { Navbar, Container, Nav} from 'react-bootstrap';
import logoImage from "../images/logo.png";
import 'bootstrap/dist/css/bootstrap.min.css';
import './NavigationBar.css';

function NavigationBar() {
    return (
        <Navbar expand="lg" className="navbar-custom">
            <Container fluid>
                <Navbar.Brand href="#">
                    <img src={logoImage} alt="Logo"/>
                </Navbar.Brand>
                <Navbar.Toggle aria-controls="navbarNav" />
                <Navbar.Collapse id="navbarNav" className="navbar-collapse">
                    <Nav className="ms-auto">
                        <Nav.Item>
                            <Nav.Link href="#" active>
                                HOME
                            </Nav.Link>
                        </Nav.Item>
                        <Nav.Item>
                            <Nav.Link href="#">FAQ</Nav.Link>
                        </Nav.Item>
                        <Nav.Item>
                            <Nav.Link href="#">LOGIN</Nav.Link>
                        </Nav.Item>
                        <Nav.Item>
                            <Nav.Link href="#">REGISTER</Nav.Link>
                        </Nav.Item>
                        <Nav.Item>
                            <Nav.Link href="#">FRANÇAIS</Nav.Link>
                        </Nav.Item>
                        <Nav.Item>
                            <a href="http://localhost:8080/oauth2/authorization/okta">Login</a>
                        </Nav.Item>
                        <Nav.Item>
                            <form method={"post"} action={"http://localhost:8080/api/v1/corso/logout"}>
                                <button type={"submit"}>Logout</button>
                            </form>
                        </Nav.Item>
                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
}

export default NavigationBar;
