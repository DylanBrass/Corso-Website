import React, {useEffect} from 'react';
import {useTranslation} from "react-i18next";

function ExternalHandling() {
    const {t} = useTranslation();

    useEffect(() => {

        // @ts-ignore
        
        // @ts-ignore
        if (document.cookie.includes("isAuthenticated=true")) {
            // @ts-ignore
            document.getElementById('submit').click()
        } else {
            window.location.href = process.env.REACT_APP_BE_HOST + "oauth2/authorization/okta"
        }

    }, [])

    return (
        <div className={"logoutRedirect"}>

            <form method={"post"} action={process.env.REACT_APP_BE_HOST + "api/v1/corso/logout?isLogoutExternal=true"}
                  id="logoutForm">
                <button id={'submit'} type={"submit"} style={{
                    display: "none",
                    visibility: "hidden"
                }}>{t("navigation.logout")}
                </button>
            </form>

        </div>
    );
}

export default ExternalHandling;
