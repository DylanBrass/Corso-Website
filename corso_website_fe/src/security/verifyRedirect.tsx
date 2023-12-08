import React, { useEffect } from 'react';
import './VerifyRedirect.css';  // Import a CSS file for styling

function VerifyRedirect() {

    useEffect(() => {
        // @ts-ignore
        console.log(document.cookie)
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
                    <p>Your email is not verified yet. Please check your email and click on the link to login into your account.</p>
                    <a href="http://localhost:8080/oauth2/authorization/okta">Login</a>
                </div>
            </div>

            <form method={"post"} action={"http://localhost:8080/api/v1/corso/logout?isLogoutVerify=true"} id="logoutForm">
                <button id={'submit'} type={"submit"} style={{
                    display: "none",
                    visibility: "hidden"
                }}>Logout</button>
            </form>

        </div>
    );
}

export default VerifyRedirect;
