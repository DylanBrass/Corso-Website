import ReactPDFChart from "react-pdf-charts";
import React from "react";
import {Bar, BarChart, CartesianGrid, Pie, PieChart, XAxis, YAxis} from "recharts";
import {dataWithoutZero} from "../TimeByService";
import {getValuesFromJSON} from "../../../../../Services/TranslationTools";
import i18n from "i18next";


export default function TimeByServicePDF({data, choice}: any) {

    return <ReactPDFChart>
        <div>
            {choice.includes(1) &&
                <BarChart
                    height={300}
                    width={500}
                    data={data}
                >
                    <Bar isAnimationActive={false} dataKey="hours_worked" name={"Hours Worked"}/>

                    <CartesianGrid stroke="#ccc"/>

                    <XAxis
                        dataKey="serviceName"
                        // hide it with a scuffed way bc react
                        angle={90}
                        label={{value: "Services", angle: 0, position: "insideBottom"}}
                    />
                    <YAxis name={"Hours Worked"}
                           dataKey={"hours_worked"}
                           domain={[0, (dataMax: number) => (Math.ceil(dataMax / 10) * 10)]}
                    />

                </BarChart>
            }

            {choice.includes(2) &&
                <PieChart
                    width={500}
                    height={300}
                >
                    <Pie
                        data={dataWithoutZero(data)}
                        dataKey="hours_worked"
                        nameKey="serviceName"
                        cx="50%" cy="50%"
                        fill="#8884d8"
                        isAnimationActive={false}
                        label={(entry) => {
                            return <text x={entry.x} y={entry.y} fill="black"
                                         style={{fontSize: "10px"}}
                                         textAnchor={entry.x > entry.cx ? "start" : "end"}
                                         dominantBaseline="central">
                                {getValuesFromJSON(i18n.language,entry.name)}
                            </text>
                        }}
                    >
                    </Pie>
                </PieChart>

            }
        </div>
    </ReactPDFChart>
}