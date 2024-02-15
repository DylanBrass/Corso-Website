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
import {useAuth} from "../../../../security/Components/AuthProvider";
import {fetchTimeByServiceReport, getOneYearAgo} from "../../../../Services/GenerateReportsService";
import {useTranslation} from "react-i18next";
import "./Reports.css";
import {getValuesFromJSON} from "../../../../Services/TranslationTools";
import i18n from "i18next";

const dataWithoutZero = (data:any) => {
    return data.filter((service: any) => service.hours_worked !== 0)
}


export default function TimeByService() {

    const auth = useAuth();

    const {t} = useTranslation();

    const [data, setData] = React.useState([])

    const diagramTypes = [t("reports.barChart"), t("reports.pieChart")]

    const [diagramType, setDiagramType] = React.useState(0)


    async function fetchData() {
        const start = (document.getElementById("start") as HTMLInputElement).value;
        const end = (document.getElementById("end") as HTMLInputElement).value;
        if(new Date(start) > new Date(end)){
            // @ts-ignore
            swal((t("error")), (t("invalidDateRange")) + " " + start + " " + (t("to")) + " " + end, "error");
            return;
        }
        fetchTimeByServiceReport(start, end).then(
            res => {
                setData(res)


            }
        ).catch((e: any) => {
            console.error(e);
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
            <h1>{t("reports.timeByService")}</h1>
            <div className={"text-center"}>
                <label className="diagram-label" htmlFor="start">{t("reports.startDateLabel")}</label>
                <input className="diagram-input" type="date" id="start" defaultValue={getOneYearAgo()}/>
                <label className="diagram-label" htmlFor="end">{t("reports.endDateLabel")}</label>
                <input className="diagram-input" type="date" id="end" defaultValue={new Date().toISOString().split('T')[0]}/>
            </div>
            {diagramTypes.map((type, index) => (
                <button className="diagram-btn" key={index} onClick={() => setDiagramType(index)}>{type}</button>
            ))}
            <button className="diagram-btn" onClick={fetchData}>{t("submitButton")}</button>

            <h3>{data.reduce((acc: number, service: any) => acc + service.hours_worked, 0)}
            {" " + t("reports.totalHoursWorked") + " "}
                {(document.getElementById("start") as HTMLInputElement)?.value} {t("to")} {(document.getElementById("end") as HTMLInputElement)?.value}</h3>
            {diagramType === 0 &&
                <ResponsiveContainer width="90%" height={1000}>
                    <BarChart barGap={10}
                              barSize={20}
                              data={data} style={
                        {
                            overflow: "show",
                            margin: "auto",
                        }}

                    >
                        <Bar dataKey="hours_worked" name={t("reports.hoursWorked")}/>
                        <CartesianGrid stroke="#ccc"/>

                        <XAxis dataKey="serviceName" name={"Service"}
                               interval={0} angle={-70}
                               tickFormatter={value => {
                                   return getValuesFromJSON(i18n.language,value)
                               }}
                               textAnchor="end" height={400}/>
                        <YAxis name={t("reports.hoursWorked")}
                               dataKey={"hours_worked"}
                               domain={[0, (dataMax: number) => (Math.ceil(dataMax / 10) * 10)]}
                        />
                        <Tooltip
                            labelFormatter={(value) => getValuesFromJSON(i18n.language,value)}
                            formatter={(value, name) => [value + t("reports.totalHours"), name]}
                            cursor={{fill: 'transparent'}}/>
                    </BarChart>
                </ResponsiveContainer>
            }

            {diagramType === 1 &&
                <div>
                    {dataWithoutZero(data).length === 0 &&
                        <div>
                            <h2>No data to display</h2>
                        </div>
                    }
                    <ResponsiveContainer width="95%" height={600}>
                    <PieChart>
                        <Tooltip
                            formatter={(value, name) => [value + t("reports.totalHours"), getValuesFromJSON(i18n.language,name)]}
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
                                    {getValuesFromJSON(i18n.language,entry.name)}
                                </text>
                            }}
                        >
                        </Pie>
                    </PieChart>
                </ResponsiveContainer>

                </div>

            }

        </div>
    )

}

export {dataWithoutZero}