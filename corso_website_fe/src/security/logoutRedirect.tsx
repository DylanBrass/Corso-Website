// This page should never be accessed directly. It is used to redirect the user to the logout page from the backend.
import React, { useEffect} from 'react';
import {useTranslation} from "react-i18next";

function LogoutRedirect() {

    const {t} = useTranslation();

    useEffect(() => {

        
        // @ts-ignore
        document.getElementById('submit').click();


    }, [])




        return (
            <div className={"logoutRedirect"}>


                <div className="loader">
                    <p>
                        {t("load.createAccount")}
                    </p>

                </div>

                <form method={"post"} action={process.env.REACT_APP_BE_HOST+"api/v1/corso/logout?isLogoutSignUp=true"} id="logoutForm">
                    <button id={'submit'} type={"submit"} style={
                        {
                            display: "none",
                            visibility: "hidden"
                        }
                    }>
                        {t("navigation.logout")}
                    </button>

                </form>

            </div>
        );

}


export default LogoutRedirect;