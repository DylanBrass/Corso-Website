import React, {useEffect} from "react";
import {useParams} from "react-router-dom";
import axios from "axios";
import {useAuth} from "../Components/AuthProvider";
import "./VerifyEmail.css";
import {useTranslation} from "react-i18next"; // Import your CSS file

function VerifyEmail() {
    let {email, token} = useParams();
    const auth = useAuth();
    const [success, setSuccess] = React.useState(false);
    const [error, setError] = React.useState("");
    const {t} = useTranslation();

    const verify = async () => {
        axios
            .post(
                process.env.REACT_APP_BE_HOST + `api/v1/corso/security/verify`,
                {
                    email: email,
                    token: token,
                },
                {
                    headers: {
                        // @ts-ignore
                        'X-XSRF-TOKEN': auth.getXsrfToken(),
                    },
                }
            )
            .then((res) => {
                console.log(res.data);

                if (res.status === 200) {
                    setSuccess(true);
                }
            })
            .catch((err) => {
                console.log(err)
                setSuccess(false);
                setError(err.response.data.message);
            });
    };

    useEffect(() => {
        verify();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    return (
        <div className={"verify-wrapper"}>
            <div className="box-container bg-black">
                <div className="verification-container">
                    {success ? (
                        <div className="container">
                            <span className="success-icon"/>
                            <p className="success-message">
                                {t("alerts.email.verifiedEmailMessage")}
                            </p>
                        </div>
                    ) : (
                        <div className="container">
                            <span className="error-icon"/>
                            <p className="error-message">{t("alerts.error")} {error}</p>
                        </div>
                    )}
                </div>

            </div>
        </div>
    );
}

export default VerifyEmail;
