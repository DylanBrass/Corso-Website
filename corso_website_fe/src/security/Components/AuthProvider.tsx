import React, {createContext, useContext, useState, useEffect} from 'react';
import Cookies from "js-cookie";
import axios from "axios";
import {Bounce, toast} from "react-toastify";

// @ts-ignore
const AuthContext = createContext();


// @ts-ignore
const AuthProvider = ({children}) => {
    const [isAuthenticated, setIsAuthenticated] =
        useState(() => {
            return Boolean(Cookies.get("isAuthenticated")) || false
        })

    const [csrfToken, setCsrfToken] = useState(() => {
        return document.cookie.replace(/(?:(?:^|.*;\s*)XSRF-TOKEN\s*=\s*([^;]*).*$)|^.*$/, '$1')
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
        window.location.href = process.env.REACT_APP_BE_HOST+"oauth2/authorization/okta"
    }


    const getUserInfo = async () => {
        if (isAuthenticated) {
            axios.get(process.env.REACT_APP_BE_HOST+"api/v1/corso/customers", {
                headers: {
                    // @ts-ignore
                    'X-XSRF-TOKEN': csrfToken
                }
            })
                .then(r => {
                    if (r.status === 200) {
                        Cookies.set("username", r.data.name)
                        Cookies.set("email", r.data.email)
                    }else{
                        getInfo()
                    }
                })
                .catch(e => {
                    getInfo()
                })

        }

        const getInfo = async () => {
            axios.get(process.env.REACT_APP_BE_HOST+"api/v1/corso/security/user-info", {
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


                })
        }
    }

    useEffect(() => {
        if (isAuthenticated) {
            getUserInfo()
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [isAuthenticated]);





    const authError = (status: number) => {
        if (status === 401) {
            toast.info('Your session expired, we are login you back in!', {
                position: "top-center",
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "colored",
                transition: Bounce
            });
            window.location.href = process.env.REACT_APP_BE_HOST+"oauth2/authorization/okta"
        } else if (status === 403) {
            window.location.href = "/403"
        }
    }

    const userRoles = () => {
        if (Cookies.get("accessPermission") === undefined) {
            return []
        }
        return Cookies.get("accessPermission")
    }

    const getXsrfToken = () => {
        return csrfToken
    }


    return (
        <AuthContext.Provider value={{isAuthenticated, login, authError, getXsrfToken, userRoles,getUserInfo}}>
            {children}
        </AuthContext.Provider>
    )
}

const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context
};

export {AuthProvider, useAuth};