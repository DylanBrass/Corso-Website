import React, {useEffect} from "react";
import {Bar, BarChart, CartesianGrid, ResponsiveContainer, Tooltip, XAxis, YAxis} from "recharts";
import {getOrdersPerMonth} from "../../../../Services/GenerateReportsService";
import {useAuth} from "../../../../security/Components/AuthProvider";
import "./Reports.css";
import {useTranslation} from "react-i18next";
import i18n from "i18next";

const currentYear = new Date().getFullYear();

// @ts-ignore
const CustomTooltip = ({active, payload, label}) => {
    const {t} = useTranslation();
    if (active && payload && payload.length) {
        return (
            <div
                style={{
                    backgroundColor: "white",
                    border: "1px solid black",
                    padding: "10px",
                    borderRadius: "5px",
                    boxShadow: "0 0 10px rgba(0,0,0,0.5)"
                }}
            >
                <p className="label">{`${getMonthName(label)}`}</p>
                <p className="label">{`${t("reports.ordersTotal")}: ${payload[0].value}`}</p>
            </div>
        );
    }

    return null;
}

const getMonthName = (month: string) => {
    const monthIndex = parseInt(month);
    const monthName = new Date(Date.UTC(2000, monthIndex, 1)).toLocaleString(i18n.language, { month: 'long' });
    return monthName.charAt(0).toUpperCase() + monthName.slice(1);
};

export default function OrdersPerMonth() {

    const auth = useAuth();

    const {t} = useTranslation();

    const [ordersPerMonth, setOrdersPerMonth] = React.useState([])

    async function fetchData() {
        setOrdersPerMonth(await getOrdersPerMonth((document.getElementById("year") as HTMLInputElement).value)
            .catch((e: any) => {
                console.error(e);
                // @ts-ignore
                auth.authError(e.response.status);
            }))


    }

    useEffect(() => {

        fetchData();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    return (
        <div>
            <center>
                <h1>{t("reports.ordersPerMonth")}</h1>
                <div className={"text-center"}>
                    <label className="diagram-label" htmlFor="start">{t("reports.year")}</label>
                    <input type="number" min="1900" max={currentYear} step="1" defaultValue={currentYear} id="year"/>
                </div>
                <button className="diagram-btn" onClick={fetchData}>{t("reports.getOrdersPerMonth")}
                </button>
                <h3>{t("reports.totalOrdersIn")} {
                    (document.getElementById("year") as HTMLInputElement)?.value
                
                }: {ordersPerMonth.reduce((acc: number, month: any) => acc + month.totalOrders, 0)}</h3>
            </center>

            <ResponsiveContainer width="90%" height={300}>
                <BarChart data={ordersPerMonth}>
                    <Tooltip

                        content={
                            // @ts-ignore
                            <CustomTooltip/>}/>
                    <XAxis
                        dataKey="month"
                        name={"Month"}
                        tickFormatter={(tickItem: string, index: number) => getMonthName(tickItem)}
                    />
                    <YAxis
                        name={"Total Orders"}
                        label={{
                            value: t("reports.orders"),
                            angle: -90,
                            position: 'insideLeft'
                        }}
                        domain={[0, (dataMax: number) => (Math.ceil(dataMax / 10) * 10)]}
                    />
                    <Bar dataKey="totalOrders"
                         fill="#064bec"
                         name={"Total Orders"}

                    />
                    <CartesianGrid strokeDasharray="5 5"/>
                </BarChart>
            </ResponsiveContainer>

        </div>
    );
}

export {CustomTooltip, getMonthName}