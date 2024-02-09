import React, {useEffect, useState} from "react";
import axios from "axios";
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
import {getOneYearAgo, stringToColour} from "../../../../Services/GenerateReportsService";
import {useAuth} from "../../../../security/Components/AuthProvider";

export default function OrderPerService() {

    const auth = useAuth();

    const [orderPerService, setOrderPerService] = useState([]);
    const diagramTypes = ["Line Chart", "Bar Chart"]
    const [diagramType, setDiagramType] = React.useState(0)


    const fetchData = async () => {
        let start = (document.getElementById("start") as HTMLInputElement).value;
        let end = (document.getElementById("end") as HTMLInputElement).value;
        let url = `${process.env.REACT_APP_BE_HOST}api/v1/corso/services/orders-per-service`;
        if (start && end) {
            url = `${url}?date_start=${start}&date_end=${end}`
        }
        axios.get(url)
            .then(res => {

                console.log(res.data);
                console.log(res.data[0].results[0].total);
                setOrderPerService(res.data);
            })
            .catch(err => {
                console.error(err);

                // @ts-ignore
                auth.authError(err.response.status);
            })
    }

    useEffect(() => {
        fetchData();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    const checkIfDatesAreMoreThanOneYear = (start: string, end: string) => {
        const startDate = new Date(start);
        const endDate = new Date(end);
        const difference = endDate.getTime() - startDate.getTime();
        return difference > 31556952000;
    }

    const customTickFormatter = (tickItem: string | number | Date) => {
        const date = new Date(tickItem);
        if (checkIfDatesAreMoreThanOneYear((document.getElementById("start") as HTMLInputElement).value, (document.getElementById("end") as HTMLInputElement).value)) {
            // If range is greater than 1, display only day and month
            return date.toLocaleDateString('en-US', { year: 'numeric', month: 'short' });
        } else {
            // Otherwise, display day, month, and year
            return date.toLocaleDateString('en-US', { day: 'numeric', month: 'short', year: 'numeric' });
        }
    };
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
                    <LineChart data={orderPerService}>
                        <XAxis dataKey="date" interval={0} angle={-70}
                               tickFormatter={customTickFormatter}
                               textAnchor="end" height={200}/>
                        <YAxis
                            domain={[0, (dataMax: number) => (Math.ceil(dataMax / 10) * 5)]}
                        />
                        <CartesianGrid strokeDasharray="3 3"/>
                        <Tooltip/>
                        <Legend
                            verticalAlign="top"
                        />

                        {
                            // @ts-ignore
                            orderPerService[0]?.results.map((result: any) => (
                                <Line
                                    key={result.serviceName}
                                    type="monotone"
                                    dataKey={obj => {
                                        return obj.results.find((r: any) => r.serviceName === result.serviceName).total

                                    }}
                                    name={result.serviceName}
                                    stroke={stringToColour(result.serviceName)}
                                />
                            ))}
                    </LineChart>
                </ResponsiveContainer>
            }
            {diagramType === 1 &&

                <ResponsiveContainer width="90%" height={600}>
                    <BarChart data={orderPerService} barGap={10}
                    >
                        <XAxis dataKey="date" interval={0} angle={-70}
                               style={{
                                   overflow: "show",
                                   margin: "auto",

                               }}
                               tickFormatter={customTickFormatter}
                               textAnchor="end" height={200}/>
                        <YAxis
                            domain={[0, (dataMax: number) => (Math.ceil(dataMax / 10) * 5)]}
                        />
                        <Tooltip/>
                        <CartesianGrid strokeDasharray="3 3"/>
                        <Legend
                            verticalAlign="top"
                        />

                        {
                            // @ts-ignore
                            orderPerService[0]?.results.map((result: any) => (
                                <Bar
                                    key={result.serviceName}
                                    type="monotone"
                                    dataKey={obj => {
                                        return obj.results.find((r: any) => r.serviceName === result.serviceName).total
                                    }}
                                    fill={stringToColour(result.serviceName)}
                                    stroke={stringToColour(result.serviceName)}
                                    name={result.serviceName}
                                />
                            ))}
                    </BarChart>
                </ResponsiveContainer>
            }

        </div>
    );
}