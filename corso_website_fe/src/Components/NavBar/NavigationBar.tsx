import React, {useCallback, useEffect, useState} from 'react';
import {Container, Nav, Navbar} from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import './NavigationBar.css';
import Cookies from "js-cookie";
import {useAuth} from "../../security/Components/AuthProvider";
import {Link} from "react-router-dom";
import logo from "../../ressources/images/customcolor_logo_transparent_background_revised.png";


import {useTranslation} from "react-i18next";

function NavigationBar() {

    const auth = useAuth()

    const { t, i18n } = useTranslation();
    const languages = ["fr", "en"];
    const [language, setLanguage] = useState(i18n.language); // Initialize language state with current language

  const changeLanguage = useCallback((lng: React.SetStateAction<string>) => {
        // @ts-ignore
        setLanguage(lng);
        // @ts-ignore
        i18n.changeLanguage(lng);
    }, [i18n]);

    useEffect(() => {
        changeLanguage(language);
    }, [language, changeLanguage]);

    return (
        <div id={"nav-bar-box"}>
            <Navbar expand="lg" className="navbar-custom">
                <Container fluid>
                    <Navbar.Brand href="/">
                        <img
                            id={"logo"}
                            src={logo}
                            alt="Logo"

                        />
                    </Navbar.Brand>
                    <Navbar.Toggle aria-controls="navbarNav"/>
                    <Navbar.Collapse id="navbarNav">
                        <div className={"ms-auto d-lg-flex flex-row"}>
                            {
                                // @ts-ignore
                                auth.userRoles().includes("Admin") &&
                                <Nav.Item className={"item"}>
                                    <Nav.Link href="/dashboard" className={"m-auto item-nav-text"}>{t("navigation.dashboard").toUpperCase()}</Nav.Link>
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
                                    {t("navigation.home")}
                                </Nav.Link>
                            </Nav.Item>
                            <Nav.Item className={"item"}>
                                <Nav.Link
                                    href="#"
                                    className={"m-auto item-nav-text"}
                                    onClick={() => {
                                        const nextLanguageIndex = (languages.indexOf(language) + 1) % languages.length;
                                        changeLanguage(languages[nextLanguageIndex]);
                                    }}
                                >
                                    {t("navigation.language")}
                                </Nav.Link>
                            </Nav.Item>


                            {
                                //@ts-ignore
                                !auth.isAuthenticated &&
                                <Nav.Item className={"item"}>
                                    <Nav.Link
                                        href={process.env.REACT_APP_BE_HOST + `oauth2/authorization/okta`}
                                        className={"m-auto item-nav-text"}>
                                        {t("navigation.login")}
                                    </Nav.Link>
                                </Nav.Item>
                            }
                            {
                                //@ts-ignore
                                auth.isAuthenticated &&
                                <Nav.Item className={"item"}>
                                    <form method={"post"}
                                          action={process.env.REACT_APP_BE_HOST + "api/v1/corso/logout"}
                                          className={"m-auto"}>
                                        <button id={"logout-btn"} type={"submit"} onClick={
                                            () => {
                                               // @ts-ignore
                                                auth.logout()
                                            }
                                        }>
                                            <span className={"logout-btn-text"}>
                                                {t("navigation.logout")}
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
                                    <div className={"m-auto"}>
                                        <Link id={"to-profile"} to={"/profile"} className={"m-auto"}>
                                            <img src={Cookies.get("picture")}
                                                 alt="See Profile" id={"profile-image"}
                                                 className={"d-none d-lg-block"}/>
                                        </Link>
                                        <Nav.Link href={"/profile"} id={"profile-text"}
                                                  className={"d-block d-lg-none item item-nav-text"}>{t("profilePage.profile")}</Nav.Link>
                                    </div>
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
