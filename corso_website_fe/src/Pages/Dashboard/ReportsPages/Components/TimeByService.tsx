import React, {useEffect} from "react";
import {
    Bar,
    BarChart,
    CartesianGrid,
    Pie,
    PieChart,
    ResponsiveContainer,
    Tooltip,
    XAxis,
    YAxis
} from "recharts";
import axios from "axios";
import {useAuth} from "../../../../security/Components/AuthProvider";
import {getOneYearAgo, stringToColour} from "../../../../Services/GenerateReportsService";


export default function TimeByService() {

    const auth = useAuth();

    const [data, setData] = React.useState([])

    const diagramTypes = ["Bar Chart", "Pie Chart"]

    const [diagramType, setDiagramType] = React.useState(0)



    const fetchData = async () => {

        let start = (document.getElementById("start") as HTMLInputElement).value;
        let end = (document.getElementById("end") as HTMLInputElement).value;

        let url = `${process.env.REACT_APP_BE_HOST}api/v1/corso/services/time-by-service`;
        if (start && end) {
            url = `${url}?date_start=${start}&date_end=${end}`
        }
        axios.get(url)
            .then(res => {
                //add ramdom color to each service
                res.data.forEach((service: any) => {
                    service.fill = stringToColour(service.serviceName)
                })

                setData(res.data);
            })
            .catch(e => {
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
                        <XAxis dataKey="serviceName" name={"Service"} interval={0} angle={-45}
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
                            formatter={(value, name) => [value + " total hours",name]}
                        />
                        <Pie
                            data={data}
                            dataKey="hours_worked"
                            nameKey="serviceName"
                            cx="50%" cy="50%" outerRadius={200}
                            fill="#8884d8"
                            label={(entry) => {
                               if(entry.value > 0)
                                   return entry.name
                               else
                                   return
                            }}
                        >
                        </Pie>
                    </PieChart>
                </ResponsiveContainer>
            }
        </div>
    )
}