import React from "react";
import {useParams} from "react-router-dom";
import axios from "axios";
import {useAuth} from "../Components/AuthProvider";


function VerifyEmail() {

    let {email, token} = useParams();

    const auth = useAuth();

    const verify = async (e:any) => {
        e.preventDefault();

        axios.post(process.env.REACT_APP_BE_HOST + `api/v1/corso/security/verify`,
            {
                email: email,
                token: token
            },
            {
                headers: {
                    // @ts-ignore
                    'X-XSRF-TOKEN': auth.getXsrfToken()
                }
            }
        )
            .then(res => {
                console.log(res.data);
            })
            .catch(err => {

            })

    }


    return (
        <div>
            <h1>Verify Email</h1>
            <form onSubmit={verify}
            >
                <input type={"hidden"} name={"token"} value={token}/>
                <input type={"hidden"} name={"email"} value={email}/>
                <button id={'submit'} type={"submit"}>
                    Verify
                </button>
            </form>
        </div>
    )
}

export default VerifyEmail;