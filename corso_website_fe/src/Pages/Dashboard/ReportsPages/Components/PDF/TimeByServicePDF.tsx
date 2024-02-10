import ReactPDFChart from "react-pdf-charts";
import React from "react";
import {Bar, BarChart, CartesianGrid, XAxis, YAxis} from "recharts";


export default function TimeByServicePDF({data, choice}: any) {

    return <ReactPDFChart>
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

    </ReactPDFChart>
}