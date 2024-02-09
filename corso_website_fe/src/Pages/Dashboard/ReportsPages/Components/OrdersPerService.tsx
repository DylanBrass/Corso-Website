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
                    <LineChart data={orderPerService} barGap={10} barCategoryGap={10}>
                        <XAxis dataKey="date" interval={0} angle={-70}
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
                    <BarChart data={orderPerService} barGap={10} barCategoryGap={10}
                    >
                        <XAxis dataKey="date" interval={0} angle={-70}
                               style={{
                                   overflow: "show",
                                   margin: "auto",

                               }}
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