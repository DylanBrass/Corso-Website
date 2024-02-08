import React, {useEffect} from "react";
import {Bar, BarChart, CartesianGrid, ResponsiveContainer, Tooltip, XAxis, YAxis} from "recharts";
import axios from "axios";
import {useAuth} from "../../../../security/Components/AuthProvider";


export default function TimeByService() {

    const auth = useAuth();

    const [data, setData] = React.useState([])


    // Really cool function found here : https://stackoverflow.com/questions/3426404/create-a-hexadecimal-colour-based-on-a-string-with-javascript
    const stringToColour = (str: string) => {
        console.log("start")
        let hash = 0;
        str.split('').forEach(char => {
            hash = char.charCodeAt(0) + ((hash << 5) - hash)
            console.log("Hash :" + hash)
        })
        let colour = '#'
        for (let i = 0; i < 3; i++) {
            const value = (hash >> (i * 8)) & 0xff
            console.log("Value: " + value)
            colour += value.toString(16).padStart(2, '0')
            console.log("Colour: " + colour)
        }
        console.log(colour)
        console.log("end")
        return colour
    }

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

    const getOneYearAgo = (): string => {
        let date = new Date();
        date.setFullYear(date.getFullYear() - 1);
        return date.toISOString().split('T')[0];
    }



    return (
        <div className={"text-center"}>
            <h1>Time By Service</h1>
            <input type="date" id="start" defaultValue={getOneYearAgo()}/>
            <input type="date" id="end" defaultValue={new Date().toISOString().split('T')[0]}/>

            <button onClick={fetchData}>Submit</button>
            <ResponsiveContainer width="95%" height={600}>
                <BarChart barGap={10}
                          barSize={20}
                          data={data} style={
                    {
                        overflow: "show",
                        margin: "auto",
                    }}

                >
                    <Bar dataKey="hours_worked" name={"Hours Worked"} />
                    <CartesianGrid stroke="#ccc"/>
                    <XAxis dataKey="serviceName" name={"Service"} interval={0} angle={-45}
                           textAnchor="end" height={200}/>
                    <YAxis  name={"Hours Worked"}
                           dataKey={"hours_worked"}
                            domain={[0, (dataMax: number) => (Math.ceil(dataMax / 10) * 10)]}
                    />
                    <Tooltip cursor={{fill: 'transparent'}}/>
                </BarChart>
            </ResponsiveContainer>

        </div>
    )
}