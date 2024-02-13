import ReactPDFChart from "react-pdf-charts";
import {Bar, BarChart, CartesianGrid, Pie, PieChart, XAxis, YAxis} from "recharts";
import React from "react";
import {totalOrdersWithoutZero} from "../OrdersPerService";
import {getValuesFromJSON} from "../../../../../Services/TranslationTools";
import i18n from "i18next";

export default function TotalOrdersPerServicePDF({totalOrdersPerService, choice}: any) {

    console.log("TotalOrdersPerServicePDF");
    console.log(totalOrdersPerService);

    return <ReactPDFChart>
        <div>
            {choice.includes(1) &&
                <BarChart data={totalOrdersPerService} barSize={10}
                          height={300}
                          width={500}
                >
                    <CartesianGrid strokeDasharray="3 3"/>
                    <XAxis dataKey="serviceName"
                           tickFormatter={(value) => {
                                 return ""
                           }}
                    />
                    <YAxis
                        dataKey={"totalOrderRequest"}
                        name={"Total Orders"}
                        label={{
                            value: 'Total Orders',
                            angle: -90,
                            position: 'insideLeft'
                        }}
                        domain={[0, (dataMax: number) => (Math.ceil(dataMax / 10) * 10)]}
                    />
                    <Bar dataKey={"totalOrderRequest"} fill={"#064bec"}
                         name={"Total Orders"}
                         isAnimationActive={false}
                    />

                </BarChart>
            }
            {choice.includes(2) &&
                <PieChart
                    width={550}
                    height={300}
                >
                    <Pie dataKey={"totalOrderRequest"} data={totalOrdersWithoutZero(totalOrdersPerService)}
                         nameKey={"serviceName"} cx="50%" cy="50%"
                         fill="#8884d8"
                         isAnimationActive={false}
                         label={(entry) => {
                             return <text x={entry.x} y={entry.y} fill="black"
                                          style={{fontSize: "10px",overflow: "wrap"}}
                                          textAnchor={entry.x > entry.cx ? "start" : "end"}
                                          dominantBaseline="central">
                                 {getValuesFromJSON(i18n.language,entry.name)}
                             </text>
                         }}
                    />
                </PieChart>

            }
        </div>
    </ReactPDFChart>

}
