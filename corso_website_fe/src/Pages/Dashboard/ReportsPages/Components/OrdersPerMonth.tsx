import React, {useEffect} from "react";
import {Bar, BarChart, CartesianGrid, ResponsiveContainer, Tooltip, XAxis, YAxis} from "recharts";
import {getOrdersPerMonth} from "../../../../Services/GenerateReportsService";
import {useAuth} from "../../../../security/Components/AuthProvider";

const currentYear = new Date().getFullYear();

// @ts-ignore
const CustomTooltip = ({active, payload, label}) => {
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
                <p className="label">{`Total Orders: ${payload[0].value}`}</p>
            </div>
        );
    }

    return null;
}

const getMonthName = (month: string) => {
    return new Date(0, parseInt(month) - 1, 1).toLocaleString('default', {month: 'long'});
}

export default function OrdersPerMonth() {

    const auth = useAuth();

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
                <h1>Reports Page</h1>
                <input type="number" min="1900" max={currentYear} step="1" defaultValue={currentYear} id="year"/>
                <button onClick={fetchData}>Get Orders Per Month
                </button>
            </center>

            <ResponsiveContainer width="90%" height={300}>
                <BarChart data={ordersPerMonth}>
                    <Tooltip

                        content={
                            // @ts-ignore
                            <CustomTooltip/>}/>
                    <XAxis dataKey="month"
                           name={"Month"}
                           tickFormatter={(tickItem: number) => {
                               return new Date(0, tickItem - 1, 1).toLocaleString('default', {month: 'long'});
                           }}

                    />
                    <YAxis
                        name={"Total Orders"}
                        label={{
                            value: 'Total Orders',
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