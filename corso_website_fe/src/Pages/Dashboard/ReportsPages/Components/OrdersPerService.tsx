import React, {useEffect, useState} from "react";
import {
    Bar,
    BarChart,
    CartesianGrid,
    Legend,
    Line,
    LineChart,
    Pie,
    PieChart,
    ResponsiveContainer,
    Tooltip,
    XAxis,
    YAxis
} from "recharts";
import {
    fetchTotalOrdersPerService,
    getOneYearAgo,
    getOrdersPerService,
    stringToColour
} from "../../../../Services/GenerateReportsService";
import {useAuth} from "../../../../security/Components/AuthProvider";
import {useTranslation} from "react-i18next";
import "./Reports.css";
import {getValuesFromJSON} from "../../../../Services/TranslationTools";
import i18n from "i18next";


const checkIfDatesAreMoreOrEqualThanTwoMonths = (start: string, end: string) => {
    const startDate = new Date(start)
    //remove time
    startDate.setHours(0, 0, 0, 0)
    const endDate = new Date(end);
    //remove time
    endDate.setHours(0, 0, 0, 0)


    //remove two months from the end date
    endDate.setMonth(endDate.getMonth() - 2)



    return endDate > startDate

}


const totalOrdersWithoutZero = (data: any) => {
    return data.filter((service: any) => service.totalOrderRequest !== 0)
}
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
                <p className="label">{`${checkIfDatesAreMoreOrEqualThanTwoMonths((document.getElementById("start") as HTMLInputElement).value, (document.getElementById("end") as HTMLInputElement).value) ? label.substring(0, 7) : label}`}</p>
                {
                    payload.map((service: any, index: number) => (

                        <p key={index}
                           className="label">{`${getValuesFromJSON(i18n.language, service.name)}: ${service.value}`}</p>

                    ))
                }
            </div>
        );
    }

    return null;
}

const customTickFormatter = (tickItem: string) => {

    if (checkIfDatesAreMoreOrEqualThanTwoMonths((document.getElementById("start") as HTMLInputElement).value, (document.getElementById("end") as HTMLInputElement).value)) {
        return tickItem.substring(0, 7)
    } else {
        return tickItem
    }
};

export default function OrderPerService() {

    const auth = useAuth();

    const {t} = useTranslation();

    const [orderPerService, setOrderPerService] = useState([]);
    const [allServices, setAllServices] = useState<any[]>([])

    const diagramTypes = [t("reports.stackedBarChart"), t("reports.lineChart"), t("reports.barChart"), t("reports.totalOrdersPerService"), t("reports.pieChart")]

    const [diagramType, setDiagramType] = React.useState(0)

    const [totalOrdersPerService, setTotalOrdersPerService] = useState<any[]>([])

    async function fetchData() {
        let start = (document.getElementById("start") as HTMLInputElement).value;
        let end = (document.getElementById("end") as HTMLInputElement).value;

        if(new Date(start) > new Date(end)){
            // @ts-ignore
            swal((t("error")), (t("invalidDateRange")) + " " + start + " " + (t("to")) + " " + end, "error");
            return;
        }

        await getOrdersPerService(start, end, allServices).then(
            res => {

                setAllServices(res.services);
                setOrderPerService(res.res);

            }
        ).catch((e: any) => {
            // @ts-ignore
            swal("Error", "Error fetching data: " + e.response.data.message, "error");
            // @ts-ignore
            auth.authError(e.response.status);
        })

        await fetchTotalOrdersPerService(start, end).then(
            res => {
                setTotalOrdersPerService(res);
            }
        ).catch((e: any) => {
            // @ts-ignore
            swal("Error", "Error fetching data: " + e.response.data.message, "error");
            // @ts-ignore
            auth.authError(e.response.status);
        })

    }

    useEffect(() => {


        fetchData();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);


    return (
        <div className={"text-center"}>
            <h1>{t("reports.ordersPerService")}</h1>
            <center>
                <div className={"text-center"}>
                    <label className="diagram-label" htmlFor="start">{t("reports.startDateLabel")}</label>
                    <input className="diagram-input" type="date" id="start" defaultValue={getOneYearAgo()}/>
                    <label className="diagram-label" htmlFor="end">{t("reports.endDateLabel")}</label>
                    <input className="diagram-input" type="date" id="end"
                           defaultValue={new Date().toISOString().split('T')[0]}/>
                </div>
                {diagramTypes.map((type, index) => (
                    <button className="diagram-btn" key={index} onClick={() => setDiagramType(index)}>{type}</button>
                ))}
                <button className="diagram-btn" onClick={fetchData}>
                    {t("submitButton")}
                </button>

                <h3>{totalOrdersPerService.reduce((a: any, b: any) => a + b.totalOrderRequest, 0)}
                    {" " + t("reports.totalOrdersFrom") + " "}
                    {(document.getElementById("start") as HTMLInputElement)?.value} {t("to")} {(document.getElementById("end") as HTMLInputElement)?.value}</h3>
            </center>

            {diagramType === 0 &&
                <ResponsiveContainer width="90%" height={600}>
                    <BarChart data={orderPerService}
                              barGap={10}
                              barCategoryGap={10}

                    >
                        <CartesianGrid strokeDasharray="3 3"/>
                        <XAxis dataKey="date" tickFormatter={customTickFormatter}
                               angle={-70}
                               textAnchor="end"
                               height={200}
                        />
                        <YAxis
                            domain={[0, (dataMax: number) => (Math.ceil(dataMax / 10) * 10)]}
                        />
                        <Tooltip
                            content={
                                // @ts-ignore
                                <CustomTooltip/>}
                        />
                        <Legend verticalAlign={"top"}
                                formatter={(value, entry) => {
                                    return <span style={{
                                        color: "black",
                                        fontWeight: "bold"
                                    }}>{value}</span>
                                }}
                        />
                        {allServices.map((service: any, index: number) => (
                            <Bar key={index} dataKey={service} stackId="a" fill={stringToColour(service)}
                                 name={getValuesFromJSON(i18n.language, service)}
                            />
                        ))}
                    </BarChart>
                </ResponsiveContainer>
            }

            {diagramType === 1 &&
                <ResponsiveContainer width="90%" height={600}>
                    <LineChart data={orderPerService}>
                        <CartesianGrid strokeDasharray="3 3"/>
                        <XAxis dataKey="date" tickFormatter={customTickFormatter}
                               angle={-70}
                               textAnchor="end"
                               height={200}
                        />
                        <YAxis
                            domain={[0, (dataMax: number) => (Math.ceil(dataMax / 10) * 10)]}
                        />
                        <Tooltip
                            content={
                                // @ts-ignore
                                <CustomTooltip/>}
                        />
                        <Legend verticalAlign={"top"}
                                formatter={(value, entry) => {
                                    return <span style={{
                                        color: "black",
                                        fontWeight: "bold"
                                    }}>{getValuesFromJSON(i18n.language, value)}</span>
                                }}
                        />
                        {allServices.map((service: any, index: number) => (
                            <Line key={index} type="monotone" dataKey={service}
                                  strokeWidth={3}
                                  stroke={stringToColour(service)}/>
                        ))}
                    </LineChart>
                </ResponsiveContainer>

            }
            {diagramType === 2 &&

                <ResponsiveContainer width="90%" height={600}>
                    <BarChart data={orderPerService} barSize={5}>
                        <CartesianGrid strokeDasharray="3 3"/>
                        <XAxis dataKey="date" tickFormatter={customTickFormatter}
                               angle={-70}
                               textAnchor="end"
                               height={200}
                        />
                        <YAxis
                            domain={[0, (dataMax: number) => (Math.ceil(dataMax / 10) * 10)]}
                        />
                        <Tooltip
                            content={
                                // @ts-ignore
                                <CustomTooltip/>}
                        />
                        <Legend verticalAlign={"top"}
                                formatter={(value, entry) => {
                                    return <span style={{
                                        color: "black",
                                        fontWeight: "bold"
                                    }}>{getValuesFromJSON(i18n.language, value)}</span>
                                }}
                        /> {allServices.map((service: any, index: number) => (
                        <Bar key={index} dataKey={service} fill={stringToColour(service)}/>
                    ))}
                    </BarChart>
                </ResponsiveContainer>

            }
            {diagramType === 3 &&
                <ResponsiveContainer width="90%" height={1000}>
                    <BarChart data={totalOrdersPerService} barSize={10}
                    >
                        <CartesianGrid strokeDasharray="3 3"/>
                        <XAxis dataKey="serviceName"
                               angle={-70}
                               textAnchor="end"
                               height={500}
                               tickFormatter={value => {
                                   return getValuesFromJSON(i18n.language, value)
                               }}
                        />
                        <YAxis
                            dataKey={"totalOrderRequest"}
                            name={t("reports.ordersTotal")}
                            domain={[0, (dataMax: number) => (Math.ceil(dataMax / 10) * 10)]}
                        />
                        <Tooltip
                            labelFormatter={(value) => getValuesFromJSON(i18n.language, value)}
                        />
                        <Bar dataKey={"totalOrderRequest"} fill={"#000000"}
                             name={t("reports.ordersTotal")}
                        />


                    </BarChart>
                </ResponsiveContainer>
            }

            {diagramType === 4 &&
                <ResponsiveContainer width="90%" height={600}>
                    <PieChart>
                        <Tooltip
                            formatter={(value, name) => [value + t("reports.totalOrders"), getValuesFromJSON(i18n.language, name)]}
                        />
                        <Pie dataKey={"totalOrderRequest"} data={totalOrdersWithoutZero(totalOrdersPerService)}
                             nameKey={"serviceName"} cx="50%" cy="50%" outerRadius={200}
                             fill="#8884d8"
                             label={(entry) => {
                                 return <text x={entry.x} y={entry.y} fill="black"
                                              textAnchor={entry.x > entry.cx ? "start" : "end"}
                                              dominantBaseline="central">
                                     {getValuesFromJSON(i18n.language, entry.name)}
                                 </text>
                             }}
                        />
                    </PieChart>


                </ResponsiveContainer>

            }

        </div>
    );
}


export {customTickFormatter, checkIfDatesAreMoreOrEqualThanTwoMonths, totalOrdersWithoutZero}
