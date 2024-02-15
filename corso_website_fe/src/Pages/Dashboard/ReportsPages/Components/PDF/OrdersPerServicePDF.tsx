import {Bar, BarChart, CartesianGrid, Legend, Line, LineChart, XAxis, YAxis} from "recharts";
import ReactPDFChart from "react-pdf-charts";
import React from "react";
import {stringToColour} from "../../../../../Services/GenerateReportsService";
import {customTickFormatter} from "../OrdersPerService";
import {getValuesFromJSON} from "../../../../../Services/TranslationTools";
import i18n from "i18next";

export default function OrdersPerServicePDF({ordersPerService, allServices, choice}: any) {

    console.log("OrdersPerServicePDF");
    console.log(ordersPerService);
    console.log(choice);

    console.log(allServices);

    return <ReactPDFChart>
        <div>
            {choice.includes(1) &&
                <BarChart data={ordersPerService}
                          height={300}
                          width={500}
                >
                    <CartesianGrid strokeDasharray="3 3"/>
                    <XAxis dataKey="date"
                           tickFormatter={customTickFormatter}
                    />
                    <YAxis
                        domain={[0, (dataMax: number) => (Math.ceil(dataMax / 10) * 10)]}

                    />
                    <Legend
                        formatter={(value, entry) => {
                            return <span style={{
                                color: "black",
                                fontWeight: "bold"
                            }}>{getValuesFromJSON(i18n.language,value)}</span>
                        }}
                    />
                    {allServices.map((service: any, index: number) => (
                        <Bar key={index} dataKey={service} stackId="a" fill={stringToColour(service)}
                             isAnimationActive={false}
                             name={service}
                        />
                    ))}
                </BarChart>
            }
            {choice.includes(2) &&
                <LineChart data={ordersPerService}
                           height={300}
                           width={500}
                >
                    <CartesianGrid strokeDasharray="3 3"/>
                    <XAxis dataKey="date" tickFormatter={customTickFormatter}

                    />
                    <YAxis
                        domain={[0, (dataMax: number) => (Math.ceil(dataMax / 10) * 10)]}
                    />
                    <Legend
                        formatter={(value, entry) => {
                            return <span style={{
                                color: "black",
                                fontWeight: "bold"
                            }}>{getValuesFromJSON(i18n.language,value)}</span>
                        }}
                    />
                    {allServices.map((service: any, index: number) => (
                        <Line key={index} type="monotone" dataKey={service}
                              strokeWidth={3}
                              isAnimationActive={false}
                              stroke={stringToColour(service)}/>
                    ))}
                </LineChart>
            }
            {choice.includes(3) &&
                <BarChart data={ordersPerService}
                            height={300}
                            width={500}
                >
                    <CartesianGrid strokeDasharray="3 3"/>
                    <XAxis dataKey="date" tickFormatter={customTickFormatter}

                    />
                    <YAxis
                        domain={[0, (dataMax: number) => (Math.ceil(dataMax / 10) * 10)]}
                    />
                    <Legend verticalAlign={"top"}
                            formatter={(value, entry) => {
                                return <span style={{
                                    color: "black",
                                    fontWeight: "bold"
                                }}>{getValuesFromJSON(i18n.language,value)}</span>
                            }}
                    /> {allServices.map((service: any, index: number) => (
                    <Bar key={index} dataKey={service} fill={stringToColour(service)}
                         isAnimationActive={false}
                    />
                ))}
                </BarChart>
            }
        </div>

    </ReactPDFChart>
}
