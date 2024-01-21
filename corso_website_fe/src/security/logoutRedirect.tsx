// This page should never be accessed directly. It is used to redirect the user to the logout page from the backend.
import React, { useEffect} from 'react';

function LogoutRedirect() {


    useEffect(() => {

        
        // @ts-ignore
        document.getElementById('submit').click();


    }, [])




        return (
            <div className={"logoutRedirect"}>


                <div className="loader">
                    <p>
                        Creating account...
                    </p>

                </div>

                <form method={"post"} action={process.env.REACT_APP_BE_HOST+"api/v1/corso/logout?isLogoutSignUp=true"} id="logoutForm">
                    <button id={'submit'} type={"submit"} style={
                        {
                            display: "none",
                            visibility: "hidden"
                        }
                    }>
                        Logout
                    </button>

                </form>

            </div>
        );

}


export default LogoutRedirect;