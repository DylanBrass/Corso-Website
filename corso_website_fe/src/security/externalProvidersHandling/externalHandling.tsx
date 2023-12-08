import React, { useEffect } from 'react';

function ExternalHandling() {

    useEffect(() => {
        // @ts-ignore
        console.log(document.cookie)
        // @ts-ignore
        if (document.cookie.includes("isAuthenticat=true")) {
            // @ts-ignore
            document.getElementById('submit').click();
        }else{
            window.location.href = "http://localhost:8080/oauth2/authorization/okta"
        }
    }, [])

    return (
        <div className={"logoutRedirect"}>

            <form method={"post"} action={"http://localhost:8080/api/v1/corso/logout?isLogoutExternal=true"} id="logoutForm">
                <button id={'submit'} type={"submit"} style={{
                    display: "none",
                    visibility: "hidden"
                }}>Logout</button>
            </form>

        </div>
    );
}

export default ExternalHandling;
