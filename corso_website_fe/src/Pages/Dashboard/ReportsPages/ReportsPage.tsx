import React from "react";
import NavigationBar from "../../../Components/NavBar/NavigationBar";
import TimeByService from "./Components/TimeByService";
import OrderPerService from "./Components/OrdersPerService";
import Dropdown from "../../../Components/Dropdown/droptown";
import OrdersPerMonth from "./Components/OrdersPerMonth";
import {Link} from "react-router-dom";
import {useTranslation} from "react-i18next";

export default function ReportsPage() {

    const [selectedReport, setSelectedReport] = React.useState("select");
    const {t} = useTranslation();

    return (
        <div>
            <NavigationBar/>
            <h1 className={"text-center"}>{t("reports.reports")}</h1>
            <div>
                <center>
                    <Link to={"/reports/make-pdf"}>{t("reports.makePDF")}</Link>

                    <Dropdown
                        label={t("reports.reports")}
                        options={[
                            t("reports.reportTypes.timeByService"),
                            t("reports.reportTypes.ordersPerService"),
                            t("reports.reportTypes.ordersPerMonth")
                        ]}
                        onSelect={(option: any) => {
                            if (option === t("reports.reportTypes.timeByService")) {
                                setSelectedReport("timeByService");
                            } else if (option === t("reports.reportTypes.ordersPerService")) {
                                setSelectedReport("ordersPerService");
                            } else if (option === t("reports.reportTypes.ordersPerMonth")) {
                                setSelectedReport("ordersPerMonth");
                            }
                        }}
                    />
                </center>
                {selectedReport === "timeByService" && <TimeByService/>}

                {selectedReport === "ordersPerService" && <OrderPerService/>}

                {selectedReport === "ordersPerMonth" && <OrdersPerMonth/>}

                {selectedReport === "select" && <h1 className={"text-center"}>{t("reports.selectReport")}</h1>}

            </div>
            <div className="custom-row text-center">
                <button
                    className="back-btn"
                    onClick={() => {
                        window.location.href = "/dashboard";
                    }}
                >
                    {t("back")}
                </button>
            </div>
        </div>

    );
}