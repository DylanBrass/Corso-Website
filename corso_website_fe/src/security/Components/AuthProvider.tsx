import React, {createContext, useContext, useEffect, useState} from 'react';
import Cookies from "js-cookie";
import axios from "axios";
import {Bounce, toast, ToastContainer} from "react-toastify";
import {useTranslation} from "react-i18next";

// @ts-ignore
const AuthContext = createContext();

axios.defaults.withCredentials = true;

const userRoles = () => {
    if (Cookies.get("accessPermission") === undefined) {
        return ""
    }
    return Cookies.get("accessPermission")
}
// @ts-ignore
const AuthProvider = ({children}) => {
    const { t } = useTranslation();

    const [isAuthenticated, setIsAuthenticated] =
        useState(() => {
            return Boolean(Cookies.get("isAuthenticated")) || false
        })

    const [csrfToken, setCsrfToken] = useState(() => {
        return document.cookie.replace(/(?:^|.*;\s*)XSRF-TOKEN\s*=\s*([^;]*).*$|^.*$/, '$1')
            || "invalid"
    })

    useEffect(() => {
        setCsrfToken(document.cookie.replace(/(?:^|.*;\s*)XSRF-TOKEN\s*=\s*([^;]*).*$|^.*$/, '$1'))
    }, [csrfToken]);

    useEffect(() => {
        setIsAuthenticated(Boolean(Cookies.get("isAuthenticated")))
    }, [isAuthenticated])

    useEffect(() => {
        if (Cookies.get("isAuthenticated") === undefined) {
            setIsAuthenticated(false)
        } else {
            setIsAuthenticated(Boolean(Cookies.get("isAuthenticated")))
        }
    }, [isAuthenticated])

    const login = () => {
        window.location.href = process.env.REACT_APP_BE_HOST + "oauth2/authorization/okta"
    }


    const getUserInfo = async () => {
        // @ts-ignore
        if (isAuthenticated && !userRoles().toString().includes("Admin")) {
            axios.get(process.env.REACT_APP_BE_HOST + "api/v1/corso/customers", {
                headers: {
                    // @ts-ignore
                    'X-XSRF-TOKEN': csrfToken
                }
            })
                .then(r => {
                    Cookies.set("isVerified", r.data.verified)

                    if (r.status === 200 && r.data.verified === true) {
                        console.log("User is verified")
                        Cookies.set("username", r.data.name)
                        Cookies.set("email", r.data.email)
                    } else {
                        getInfo()
                    }
                })
                .catch(e => {
                    authError(e.response.status)

                    getInfo()
                })

        }
    }

    const getInfo = async () => {
        // @ts-ignore
        if (isAuthenticated && !userRoles().toString().includes("Admin")) {
            axios.get(process.env.REACT_APP_BE_HOST + "api/v1/corso/security/user-info", {
                headers: {
                    // @ts-ignore
                    'X-XSRF-TOKEN': csrfToken
                }
            })
                .then(r => {
                    if (r.status === 200) {
                        Cookies.set("username", r.data.username)
                        Cookies.set("email", r.data.email)
                    }
                })
                .catch(e => {

                    authError(e.response.status)

                })
        }
    }


    useEffect(() => {
        // @ts-ignore
        if (isAuthenticated && !userRoles().toString().includes("Admin")) {
            getUserInfo()
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [isAuthenticated]);


    const authError = (status: number) => {
        if (status === 401) {
            if (document.getElementById("auth-error") === null) {
                toast.info((t("session.sessionExpired")), {
                    position: "top-center",
                    autoClose: 5000,
                    hideProgressBar: false,
                    closeOnClick: true,
                    pauseOnHover: true,
                    draggable: true,
                    progress: undefined,
                    theme: "colored",
                    transition: Bounce,
                    toastId: "auth-error"
                })
                window.location.href = process.env.REACT_APP_BE_HOST + "oauth2/authorization/okta"
            }
        } else if (status === 403) {
            window.location.href = "/403"
        }
    }


    const getXsrfToken = () => {
        return csrfToken
    }


    const logout = () => {

        Cookies.remove("username")
        Cookies.remove("email")


    }


    return (
        <AuthContext.Provider value={{isAuthenticated, login, authError, getXsrfToken, userRoles, getUserInfo, logout}}>
            {children}
            <ToastContainer
                position="top-center"
                autoClose={5000}
                hideProgressBar={false}
                newestOnTop={false}
                closeOnClick
                rtl={false}
                pauseOnFocusLoss
                draggable
                pauseOnHover
                theme="colored"
            />
        </AuthContext.Provider>
    )
}

const useAuth = () => {
    const { t } = useTranslation();
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error(t("session.useAuthMessage"));
    }
    return context
};

export {AuthProvider, useAuth};
