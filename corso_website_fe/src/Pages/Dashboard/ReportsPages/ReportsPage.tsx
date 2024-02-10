import React from "react";
import NavigationBar from "../../../Components/NavBar/NavigationBar";
import TimeByService from "./Components/TimeByService";
import OrderPerService from "./Components/OrdersPerService";
import Dropdown from "../../../Components/Dropdown/droptown";
import OrdersPerMonth from "./Components/OrdersPerMonth";
import {Link} from "react-router-dom";

export default function ReportsPage() {

    const [selectedReport, setSelectedReport] = React.useState("select");


    return (
        <div>
            <NavigationBar/>
            <h1 className={"text-center"}>Reports</h1>
            <div>
                <center>
                    <Link to={"/reports/make-pdf"}>Make PDF</Link>

                    <Dropdown
                        label={"Reports"}
                        options={["Time By Service", "Orders Per Service", "Orders Per Month"]}
                        onSelect={(option: any) => {
                            if (option === "Time By Service") {
                                setSelectedReport("timeByService");
                            } else if (option === "Orders Per Service") {
                                setSelectedReport("ordersPerService");
                            } else if (option === "Orders Per Month") {
                                setSelectedReport("ordersPerMonth");
                            }
                        }}
                    />
                </center>
                {selectedReport === "timeByService" && <TimeByService/>}

                {selectedReport === "ordersPerService" && <OrderPerService/>}

                {selectedReport === "ordersPerMonth" && <OrdersPerMonth/>}

                {selectedReport === "select" && <h1 className={"text-center"}>Select a report</h1>}

            </div>
        </div>

    );
}