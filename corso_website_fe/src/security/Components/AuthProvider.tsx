import React, {createContext, useContext, useState, useEffect} from 'react';
import Cookies from "js-cookie";
import axios from "axios";

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
        console.log("XSRF-TOKEN: " + Cookies.get("XSRF-TOKEN"))
        setCsrfToken(document.cookie.replace(/(?:^|.*;\s*)XSRF-TOKEN\s*=\s*([^;]*).*$|^.*$/, '$1'))
    }, [csrfToken]);

    useEffect(() => {
        setIsAuthenticated(Boolean(Cookies.get("isAuthenticated")))
    }, [isAuthenticated])

    useEffect(() => {
        console.log("isAuthenticated: " + Cookies.get("isAuthenticated"))
        if (Cookies.get("isAuthenticated") === undefined) {
            setIsAuthenticated(false)
        } else {
            console.log("isAuthenticated: " + Boolean(Cookies.get("isAuthenticated")))
            setIsAuthenticated(Boolean(Cookies.get("isAuthenticated")))
        }
        console.log("isAuthenticated: " + isAuthenticated)
    }, [isAuthenticated])

    const login = () => {
        window.location.href = "http://localhost:8080/oauth2/authorization/okta"
    }


    const getUserInfo = async () => {
        if (isAuthenticated) {
           axios.get("http://localhost:8080/api/v1/corso/security/user-info", {
                headers: {
                    // @ts-ignore
                    'X-XSRF-TOKEN': csrfToken
                }
            })
                .then(r => {
                    if (r.status === 200) {
                        console.log(r.data)
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
            getUserInfo().then(r => console.log(r))
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [isAuthenticated]);





    const authError = (status: number) => {
        if (status === 401) {
            window.location.href = "http://localhost:8080/oauth2/authorization/okta"
        } else if (status === 403) {
            window.location.href = "/403"
        } else {
            console.log("Error: " + status)
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
        <AuthContext.Provider value={{isAuthenticated, login, authError, getXsrfToken, userRoles}}>
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