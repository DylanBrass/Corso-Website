import React from "react";
import {Bar, BarChart, CartesianGrid, XAxis, YAxis} from "recharts";
import ReactPDFChart from "react-pdf-charts";
import {getMonthName} from "../OrdersPerMonth";

export default function OrdersPerMonthPDF({ordersPerMonth, choice}: any) {

    console.log(ordersPerMonth);

    return <ReactPDFChart>
        <div>
            {choice.includes(1) &&
                <BarChart data={ordersPerMonth}
                          height={300}
                          width={500}
                >
                    <XAxis
                        dataKey="month"
                        name={"Month"}
                        tickFormatter={(tickItem: string, index: number) => getMonthName(tickItem)}
                    />
                    <YAxis
                        name={"Total Orders"}
                        label={{
                            value: 'Total Orders',
                            angle: -90,
                            position: 'insideLeft'
                        }}
                        domain={[0, (dataMax: number) => (Math.ceil(dataMax / 10) * 10)]}
                    />
                    <Bar dataKey="totalOrders"
                         fill="#064bec"
                         isAnimationActive={false}
                         name={"Total Orders"}

                    />
                    <CartesianGrid strokeDasharray="5 5"/>
                </BarChart>
            }
        </div>
    </ReactPDFChart>
}