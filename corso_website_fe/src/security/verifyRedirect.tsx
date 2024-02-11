import React, { useEffect } from 'react';
import './VerifyRedirect.css';
import {useTranslation} from "react-i18next";  // Import a CSS file for styling

function VerifyRedirect() {
    const {t} = useTranslation();

    useEffect(() => {
        // @ts-ignore
        
        // @ts-ignore
        if (document.cookie.includes("isAuthenticating=true")) {
            // @ts-ignore
            document.getElementById('submit').click();
        }
    }, [])

    return (
        <div className={"logoutRedirect"}>

            <div className="verification-message-container">
                <div className="verification-message">
                    <p>{t("alerts.email.unverifiedEmailMessage")}</p>
                    <a href={process.env.REACT_APP_BE_HOST+"oauth2/authorization/okta"}>{t("navigation.login")}</a>
                </div>
            </div>

            <form method={"post"} action={process.env.REACT_APP_BE_HOST+"api/v1/corso/logout?isLogoutVerify=true"} id="logoutForm">
                <button id={'submit'} type={"submit"} style={{
                    display: "none",
                    visibility: "hidden"
                }}>{t("navigation.logout")}</button>
            </form>

        </div>
    );
}

export default VerifyRedirect;
