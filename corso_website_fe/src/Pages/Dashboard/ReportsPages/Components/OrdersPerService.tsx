import React, {useEffect, useState} from "react";
import {
    Bar,
    BarChart,
    CartesianGrid,
    Legend,
    Line,
    LineChart,
    ResponsiveContainer,
    Tooltip,
    XAxis,
    YAxis
} from "recharts";
import {getOneYearAgo, getOrdersPerService, stringToColour} from "../../../../Services/GenerateReportsService";
import {useAuth} from "../../../../security/Components/AuthProvider";

const checkIfDatesAreMoreOrEqualThanTwoMonths = (start: string, end: string) => {
    const startDate = new Date(start);
    const endDate = new Date(end);
    const differenceInTime = endDate.getTime() - startDate.getTime();
    const differenceInMonths = differenceInTime / (1000 * 3600 * 24 * 30);
    return differenceInMonths >= 2;

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
                <p className="label">{`${label}`}</p>
                {
                    payload.map((service: any, index: number) => (
                        <p key={index} className="label">{`${service.name}: ${service.value}`}</p>
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

    const [orderPerService, setOrderPerService] = useState([]);
    const [allServices, setAllServices] = useState<any[]>([])

    const diagramTypes = ["Stacked Bar Chart", "Line Chart", "Bar Chart"]
    const [diagramType, setDiagramType] = React.useState(0)


    async function fetchData() {
        let start = (document.getElementById("start") as HTMLInputElement).value;
        let end = (document.getElementById("end") as HTMLInputElement).value;
        await getOrdersPerService(start, end, allServices).then(
            res => {

                setAllServices(res.services);
                setOrderPerService(res.res);

            }
        ).catch((e: any) => {
            console.error(e);
            // @ts-ignore
            auth.authError(e.response.status);
        })

    }

    useEffect(() => {


        fetchData();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);





    return (
        <div>
            <center>
                <input type="date" id="start" defaultValue={getOneYearAgo()}/>
                <input type="date" id="end" defaultValue={new Date().toISOString().split('T')[0]}/>
                {diagramTypes.map((type, index) => (
                    <button key={index} onClick={() => setDiagramType(index)}>{type}</button>
                ))}
                <button onClick={fetchData}>Search</button>
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
                                 name={service}
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
                                    }}>{value}</span>
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
                                    }}>{value}</span>
                                }}
                        /> {allServices.map((service: any, index: number) => (
                        <Bar key={index} dataKey={service} fill={stringToColour(service)}/>
                    ))}
                    </BarChart>
                </ResponsiveContainer>

            }


        </div>
    )
        ;
}


export {customTickFormatter,checkIfDatesAreMoreOrEqualThanTwoMonths}