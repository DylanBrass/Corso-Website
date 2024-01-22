import React from "react";
import "./DashboardTile.css"

class Body {
    title1: string;
    text1: string;
    title2: string;
    text2: string;
    icon: string;
    action: () => void;

    constructor(title1: string, text1: string, title2: string, text2: string, icon: string, action: () => void) {
        this.title1 = title1;
        this.text1 = text1;
        this.title2 = title2;
        this.text2 = text2;
        this.icon = icon;
        this.action = action;
    }


}

function AdminDashboardTile(body: Body) {
    return (
        <div className={"h-100 full-tile"} onClick={body.action}>
            <div className={"admin-dashboard-tile d-flex container h-100"}>
                <div className={"row h-100 text-center"} style={{
                }}>
                    <div className={"col-7 box-body"}>
                        {body.title1 !== ""  &&
                            <h3>{body.title1}</h3>
                        }
                        {body.text1 !== "" &&
                            <p>{body.text1}</p>
                        }
                        {body.title2 !== "" &&
                        <h3>{body.title2}</h3>
                        }
                        {body.text2 !== ""  &&
                            <p>{body.text2}</p>
                        }
                    </div>
                    <div className={"col-5 img-box"}>
                        <img src={body.icon} style={{
                            width: "100%",
                        }} className={"d-block"} alt={"Icon"}/>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default AdminDashboardTile;