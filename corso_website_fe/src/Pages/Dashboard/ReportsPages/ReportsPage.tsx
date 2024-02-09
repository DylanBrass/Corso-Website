import React from "react";
import NavigationBar from "../../../Components/NavBar/NavigationBar";
import TimeByService from "./Components/TimeByService";
import OrderPerService from "./Components/OrdersPerService";
import Dropdown from "../../../Components/Dropdown/droptown";

export default function ReportsPage() {

    const [selectedReport, setSelectedReport] = React.useState("select");


    return (
        <div>
            <NavigationBar/>
            <center>
                <Dropdown
                    label={"Reports"}
                    options={["Time By Service", "Orders Per Service"]}
                    onSelect={(option: any) => {
                        if (option === "Time By Service") {
                            setSelectedReport("timeByService");
                        } else if (option === "Orders Per Service") {
                            setSelectedReport("ordersPerService");
                        }
                    }}
                />
            </center>
            {selectedReport === "timeByService" && <TimeByService/>}
            {selectedReport === "ordersPerService" && <OrderPerService/>}

            {selectedReport === "select" && <h1 className={"text-center"}>Select a report</h1>}

        </div>
    );
}