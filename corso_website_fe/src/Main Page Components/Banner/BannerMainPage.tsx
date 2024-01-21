import "./BannerMain.css";
import {useEffect, useState} from "react";
import Cookies from "js-cookie";
import React from "react";


// @ts-ignore
function BannerMainPage({image}) {

    const [name, setName] = useState("")

    useEffect(() => {

        getUsername()
    }, [])

    const getUsername = async () => {
        if (Cookies.get("username") !== undefined) {
            // @ts-ignore
            setName(Cookies.get("username"))
        } else {
            let counter = 0;
            while (Cookies.get("username") === undefined) {

                await new Promise(r => setTimeout(r, 50));
                

                if (Cookies.get("username") !== undefined) {
                    // @ts-ignore
                    setName(Cookies.get("username"))
                    break;
                }
                counter++;
                if (counter === 100) {
                    break;
                }
            }
        }
    }

    return (
        <div className={"banner-box"} style={{
            backgroundImage: `url(${image})`
        }}>

            <h1 id={"greeting"} className={"mt-5"}>Hello {name}!</h1>
            <div className={"row row-banner"}>
                <div className={"row d-none d-lg-flex"}>
                    <div className={"col-12 col-lg-6 text-center"}>
                        <h3 className={"mt-5"}>View All past orders</h3>

                    </div>
                    <div className={"col-12 col-lg-6 text-center"}>
                        <h3 className={"mt-5"}>View current Orders</h3>

                    </div>
                </div>
                <div className={"row"}>
                    <div className={"col-12 col-lg-6 text-center"}>

                        <button className={"banner-btn mt-5 mb-5"}
                                onClick={() => {
                                    
                                    window.location.href = "/profile/orders"
                                }}>Past Orders
                        </button>
                    </div>
                    <div className={"col-12 col-lg-6 text-center"}>

                        <button className={"banner-btn mt-5 mb-5"}
                                onClick={() => {
                                    
                                }}>Request Now!
                        </button>
                    </div>

                </div>
            </div>
        </div>
    )
}

export default BannerMainPage;