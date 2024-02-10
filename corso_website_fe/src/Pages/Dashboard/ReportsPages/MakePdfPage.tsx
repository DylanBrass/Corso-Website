import React, {useState} from "react";
import NavigationBar from "../../../Components/NavBar/NavigationBar";
import {Document, Page, PDFViewer, Text} from '@react-pdf/renderer';
import OrdersPerMonthPDF from "./Components/PDF/OrdersPerMonthPDF";
import {getMonthName} from "./Components/OrdersPerMonth";
import {
    fetchTimeByServiceReport,
    getOneYearAgo,
    getOrdersPerMonth,
    getOrdersPerService,
    stringToColour
} from "../../../Services/GenerateReportsService";
import OrdersPerServicePDF from "./Components/PDF/OrdersPerServicePDF";
import {checkIfDatesAreMoreOrEqualThanTwoMonths} from "./Components/OrdersPerService";
import TimeByServicePDF from "./Components/PDF/TimeByServicePDF";
import {useAuth} from "../../../security/Components/AuthProvider";

const currentYear = new Date().getFullYear();


export default function MakePdfPage() {

    const auth = useAuth();

    const [ordersPerMonth, setOrdersPerMonth] = React.useState([])
    const [displayOrdersPerMonth, setDisplayOrdersPerMonth] = React.useState(true)

    const [orderPerService, setOrderPerService] = useState([]);
    const [allServices, setAllServices] = useState<any[]>([])
    const [displayOrdersPerService, setDisplayOrdersPerService] = useState(true)

    const [page2Choice, setPage2Choice] = useState<number[]>([1])


    const [timeByService, setTimeByService] = useState([])
    const [displayTimeByService, setDisplayTimeByService] = useState(true)
    const [timeByServiceChoice, setTimeByServiceChoice] = useState<number[]>([1])

    async function fetchData() {
        setTimeByService([])
        setOrdersPerMonth(await getOrdersPerMonth((document.getElementById("year") as HTMLInputElement).value)
            .catch((e: any) => {
                console.error(e);
                // @ts-ignore
                auth.authError(e.response.status);
            }))

        let start = (document.getElementById("start") as HTMLInputElement).value;
        let end = (document.getElementById("end") as HTMLInputElement).value;
        await getOrdersPerService(start, end, allServices).then(
            res => {

                console.log("res.services", res.services);

                setAllServices(res.services);
                setOrderPerService(res.res);

            }
        ).catch((e: any) => {
            console.error(e);
            // @ts-ignore
            auth.authError(e.response.status);
        })

        let startTimeBy = (document.getElementById("startTimeBy") as HTMLInputElement).value;
        let endTimeBy = (document.getElementById("endTimeBy") as HTMLInputElement).value;
        await fetchTimeByServiceReport(startTimeBy, endTimeBy)
            .then(res => {
                console.log("res for time by ", res);
                setTimeByService(res)
            })
            .catch((e: any) => {
                console.error(e);
                // @ts-ignore
                auth.authError(e.response.status);
            })

    }


    const addOrRemoveOrdersPerMonth = () => {
        setDisplayOrdersPerMonth(!displayOrdersPerMonth)
    }

    const addOrRemoveOrdersPerService = () => {
        setDisplayOrdersPerService(!displayOrdersPerService)
    }

    const addOrRemoveTimeByService = () => {
        setDisplayTimeByService(!displayTimeByService)
    }


    React.useEffect(() => {


        fetchData();        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);


    return (
        <div>
            <NavigationBar/>
            <center>
                <section>
                    <h1>Reports for Orders per Month</h1>
                    <h2>Display Orders Per Month</h2>
                    <button onClick={addOrRemoveOrdersPerMonth}>
                        {displayOrdersPerMonth ? "Remove" : "Add"} Orders Per Month
                    </button>
                    <div>
                        <h2>Choose Year</h2>
                        <input type="number" min="1900" max={currentYear} step="1" defaultValue={currentYear}
                               id="year"/>
                        <button onClick={async () => {
                            setOrdersPerMonth(await getOrdersPerMonth((document.getElementById("year") as HTMLInputElement).value)
                                .catch((e: any) => {
                                    console.error(e);
                                    // @ts-ignore
                                    auth.authError(e.response.status);
                                }))
                        }}>Get Orders Per Month
                        </button>
                    </div>

                </section>
                <section>
                    <h1>Reports for Orders per Service</h1>
                    <h2>Display Orders Per Service</h2>
                    <button onClick={addOrRemoveOrdersPerService}>
                        {displayOrdersPerService ? "Remove" : "Add"} Orders Per Service
                    </button>
                    <div>

                        <h2>Choose Dates</h2>
                        <input type="date" id="start" defaultValue={getOneYearAgo()}/>
                        <input type="date" id="end" defaultValue={new Date().toISOString().split('T')[0]}/>
                        <button onClick={fetchData}>Get Orders Per Service
                        </button>

                        <h2>Choose Diagrams</h2>

                        <button onClick={() => {
                            if (page2Choice.includes(1)) {
                                setPage2Choice(page2Choice.filter((choice) => choice !== 1))
                            } else {
                                setPage2Choice([...page2Choice, 1])
                            }
                        }}>
                            {page2Choice.includes(1) ? "Remove" : "Add"} Line Chart
                        </button>
                        <button onClick={() => {
                            if (page2Choice.includes(2)) {
                                setPage2Choice(page2Choice.filter((choice) => choice !== 2))
                            } else {
                                setPage2Choice([...page2Choice, 2])
                            }
                        }}>
                            {page2Choice.includes(2) ? "Remove" : "Add"} Line Chart
                        </button>

                        <button onClick={() => {
                            if (page2Choice.includes(3)) {
                                setPage2Choice(page2Choice.filter((choice) => choice !== 3))
                            } else {
                                setPage2Choice([...page2Choice, 3])
                            }
                        }
                        }>
                            {page2Choice.includes(3) ? "Remove" : "Add"} Bar Chart
                        </button>

                    </div>

                </section>
                <section>
                    <h1>Reports for Time by Service</h1>
                    <h2>Display Time By Service</h2>
                    <button onClick={addOrRemoveTimeByService}>
                        {displayTimeByService ? "Remove" : "Add"} Time By Service
                    </button>
                    <h2>Choose Dates</h2>
                    <input type="date" id="startTimeBy" defaultValue={getOneYearAgo()}/>
                    <input type="date" id="endTimeBy" defaultValue={new Date().toISOString().split('T')[0]}/>
                    <button onClick={fetchData}>Get Time By Service
                    </button>
                    <h2>Choose Diagrams</h2>
                    <button onClick={() => {
                        if (timeByServiceChoice.includes(1)) {
                            setTimeByServiceChoice(timeByServiceChoice.filter((choice) => choice !== 1))
                        } else {
                            setTimeByServiceChoice([...timeByServiceChoice, 1])
                        }
                    }}>
                        {timeByServiceChoice.includes(1) ? "Remove" : "Add"} Line Chart
                    </button>
                    <button onClick={() => {
                        if (timeByServiceChoice.includes(2)) {
                            setTimeByServiceChoice(timeByServiceChoice.filter((choice) => choice !== 2))
                        } else {
                            setTimeByServiceChoice([...timeByServiceChoice, 2])
                        }
                    }}>
                        {timeByServiceChoice.includes(2) ? "Remove" : "Add"} Pie Chart
                    </button>
                </section>
            </center>
            <PDFViewer width={"100%"} height={"1000px"}

            >
                <Document
                    title={"Corso Reports"}
                    author="Corso"
                    subject="Corso Reports"
                    keywords="corso, reports, pdf"

                >
                    <Page size="A4">

                        {displayOrdersPerMonth &&
                            <div>
                                <Text style={{
                                    fontSize: 20,
                                    margin: 12
                                }}>Orders Per Month</Text>

                                <OrdersPerMonthPDF ordersPerMonth={ordersPerMonth}/>

                                {
                                    ordersPerMonth.map((month: any) => {
                                        return <Text style={{
                                            fontSize: 12,
                                            margin: 12
                                        }} key={month.month}>{getMonthName(month.month)}: {month.totalOrders}</Text>
                                    })
                                }
                            </div>
                        }

                        {displayOrdersPerService &&
                            <div>
                                <Text style={{
                                    fontSize: 20,
                                    margin: 12
                                }}>Orders Per Service</Text>
                                <OrdersPerServicePDF ordersPerService={orderPerService} allServices={allServices}
                                                     choice={page2Choice}/>

                                {
                                    orderPerService.map((service: any) => {
                                        console.log("service", service);
                                        return <Text style={{
                                            fontSize: 12,
                                            margin: 12
                                        }} key={service.date}>
                                            <Text style={{
                                                fontSize: 20,
                                                margin: 12
                                            }}>
                                                {checkIfDatesAreMoreOrEqualThanTwoMonths((document.getElementById("start") as HTMLInputElement).value,
                                                    (document.getElementById("end") as HTMLInputElement).value) ?
                                                    service.date.substring(0, 7) :
                                                    service.date}
                                            </Text>
                                            {"\n"}{
                                            Object.keys(service)
                                                .filter((key) => key !== "date")
                                                .map((key) => {
                                                    return <Text key={key}>
                                                        {key}: {service[key]}
                                                        {"\n"}
                                                    </Text>
                                                })
                                        }</Text>
                                    })
                                }
                            </div>
                        }
                        {displayTimeByService &&
                            <div>
                                <Text style={{
                                    fontSize: 20,
                                    margin: 12
                                }}>Time By Service</Text>
                                <TimeByServicePDF data={timeByService} choice={timeByServiceChoice}/>
                                {
                                    timeByService.map((service: any) => {
                                        return <Text style={{
                                            fontSize: 12,
                                            margin: 12,
                                        }} key={service.serviceName}>

                                            <Text style={{
                                                color: stringToColour(service.serviceName),
                                                backgroundColor: stringToColour(service.serviceName),
                                                padding: 5,
                                                margin: 5,
                                                borderRadius: 5,
                                            }}>
                                                M
                                            </Text>
                                            {"    "}
                                            {service.serviceName}: {service.hours_worked} hours
                                        </Text>
                                    })
                                }


                            </div>


                        }
                    </Page>
                </Document>
            </PDFViewer>
        </div>
    )
        ;
}