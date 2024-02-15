import React, {useEffect} from "react";
import NavigationBar from "../../../Components/NavBar/NavigationBar";
import TimeByService from "./Components/TimeByService";
import OrderPerService from "./Components/OrdersPerService";
import Dropdown from "../../../Components/Dropdown/droptown";
import OrdersPerMonth from "./Components/OrdersPerMonth";
import {useTranslation} from "react-i18next";
import {useAuth} from "../../../security/Components/AuthProvider";
import BackButton from "../../../Components/BackButton";

export default function ReportsPage() {

    const auth = useAuth()
    ;
    const [selectedReport, setSelectedReport] = React.useState("select");
    const {t} = useTranslation();

    useEffect(() => {
        // @ts-ignore
        if (!auth.isAuthenticated || !auth.userRoles().includes("Admin")) {
            window.location.href = "/"
        }

        // eslint-disable-next-line
    }, []);

    const backButtonHref = "/dashboard";

    return (
        <div>
            <NavigationBar/>
            <BackButton
                link={backButtonHref}
                text={ t("backToDashboard")}
            />
            <h1 className={"text-center"}>{t("reports.reports")}</h1>
            <div>
                <center>
                    <button className="btn-style" onClick={() => {
                        window.location.href = "/reports/make-pdf";
                    }}>{t("reports.makePDF")}</button>
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
        </div>

    );
}