import React, {useEffect} from "react";
import {
    Bar,
    BarChart,
    CartesianGrid,
    Legend,
    Pie,
    PieChart,
    ResponsiveContainer,
    Tooltip,
    XAxis,
    YAxis
} from "recharts";
import {useAuth} from "../../../../security/Components/AuthProvider";
import {fetchTimeByServiceReport, getOneYearAgo} from "../../../../Services/GenerateReportsService";

const dataWithoutZero = (data:any) => {
    return data.filter((service: any) => service.hours_worked !== 0)
}


export default function TimeByService() {

    const auth = useAuth();

    const [data, setData] = React.useState([])

    const diagramTypes = ["Bar Chart", "Pie Chart"]

    const [diagramType, setDiagramType] = React.useState(0)


    async function fetchData() {
        const start = (document.getElementById("start") as HTMLInputElement).value;
        const end = (document.getElementById("end") as HTMLInputElement).value;
        fetchTimeByServiceReport(start, end).then(
            res => {
                setData(res)


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
        <div className={"text-center"}>
            <h1>Time By Service</h1>
            <input type="date" id="start" defaultValue={getOneYearAgo()}/>
            <input type="date" id="end" defaultValue={new Date().toISOString().split('T')[0]}/>
            {diagramTypes.map((type, index) => (
                <button key={index} onClick={() => setDiagramType(index)}>{type}</button>
            ))}
            <button onClick={fetchData}>Submit</button>

            {diagramType === 0 &&
                <ResponsiveContainer width="90%" height={600}>
                    <BarChart barGap={10}
                              barSize={20}
                              data={data} style={
                        {
                            overflow: "show",
                            margin: "auto",
                        }}

                    >
                        <Bar dataKey="hours_worked" name={"Hours Worked"}/>
                        <CartesianGrid stroke="#ccc"/>

                        <XAxis dataKey="serviceName" name={"Service"}
                               interval={0} angle={-45}
                               textAnchor="end" height={200}/>
                        <YAxis name={"Hours Worked"}
                               dataKey={"hours_worked"}
                               domain={[0, (dataMax: number) => (Math.ceil(dataMax / 10) * 10)]}
                        />
                        <Tooltip cursor={{fill: 'transparent'}}/>
                    </BarChart>
                </ResponsiveContainer>
            }

            {diagramType === 1 &&
                <ResponsiveContainer width="95%" height={600}>
                    <PieChart>
                        <Tooltip
                            formatter={(value, name) => [value + " total hours", name]}
                        />
                        <Pie
                            data={dataWithoutZero(data)}
                            dataKey="hours_worked"
                            nameKey="serviceName"
                            cx="50%" cy="50%" outerRadius={200}
                            fill="#8884d8"
                            label={(entry) => {
                                return <text x={entry.x} y={entry.y} fill="black"
                                             textAnchor={entry.x > entry.cx ? "start" : "end"}
                                             dominantBaseline="central">
                                    {entry.name}
                                </text>
                            }}
                        >
                        </Pie>
                    </PieChart>
                </ResponsiveContainer>
            }
        </div>
    )
}

export {dataWithoutZero}