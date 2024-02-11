import React, {useState} from "react";
import NavigationBar from "../../../Components/NavBar/NavigationBar";
import {Document, Page, PDFViewer, Text, View} from '@react-pdf/renderer';
import OrdersPerMonthPDF from "./Components/PDF/OrdersPerMonthPDF";
import {getMonthName} from "./Components/OrdersPerMonth";
import {
    fetchTimeByServiceReport,
    fetchTotalOrdersPerService,
    getOneYearAgo,
    getOrdersPerMonth,
    getOrdersPerService,
    stringToColour
} from "../../../Services/GenerateReportsService";
import OrdersPerServicePDF from "./Components/PDF/OrdersPerServicePDF";
import {checkIfDatesAreMoreOrEqualThanTwoMonths} from "./Components/OrdersPerService";
import TimeByServicePDF from "./Components/PDF/TimeByServicePDF";
import {useAuth} from "../../../security/Components/AuthProvider";
import PresentationPage from "./Components/PDF/PresentationPage";
import TotalOrdersPerServicePDF from "./Components/PDF/TotalOrdersPerServicePDF";
import {Link} from "react-router-dom";

const currentYear = new Date().getFullYear();


export default function MakePdfPage() {

    const auth = useAuth();

    const [ordersPerMonth, setOrdersPerMonth] = React.useState([])
    const [displayOrdersPerMonth, setDisplayOrdersPerMonth] = React.useState(true)
    const [displayOrdersPerMonthChoice, setDisplayOrdersPerMonthChoice] = React.useState<number[]>([1])
    const [showTextForOrdersPerMonth, setShowTextForOrdersPerMonth] = useState(true)


    const [orderPerService, setOrderPerService] = useState([]);
    const [allServices, setAllServices] = useState<any[]>([])
    const [displayOrdersPerService, setDisplayOrdersPerService] = useState(true)
    const [showTextForOrdersPerService, setShowTextForOrdersPerService] = useState(true)

    const [page2Choice, setPage2Choice] = useState<number[]>([1])


    const [timeByService, setTimeByService] = useState([])
    const [displayTimeByService, setDisplayTimeByService] = useState(true)
    const [timeByServiceChoice, setTimeByServiceChoice] = useState<number[]>([1])


    const [totalOrdersPerService, setTotalOrdersPerService] = useState([])
    const [displayTotalOrdersPerService, setDisplayTotalOrdersPerService] = useState(true)
    const [totalOrdersPerServiceChoice, setTotalOrdersPerServiceChoice] = useState<number[]>([1])

    async function fetchData() {
        setTimeByService([])
        setTotalOrdersPerService([])

        setOrdersPerMonth(await getOrdersPerMonth((document.getElementById("year") as HTMLInputElement)?.value)
            .catch((e: any) => {
                console.error(e);
                // @ts-ignore
                swal("Error", "Error while fetching data : " + e.response.data.message, "error");
                // @ts-ignore
                auth.authError(e.response.status)
            }))

        let start = (document.getElementById("start") as HTMLInputElement)?.value;
        let end = (document.getElementById("end") as HTMLInputElement)?.value;
        await getOrdersPerService(start, end, allServices).then(
            res => {

                console.log("res.services", res.services);

                setAllServices(res.services);
                setOrderPerService(res.res);

            }
        ).catch((e: any) => {
            console.error(e);
            // @ts-ignore
            swal("Error", "Error while fetching data : " + e.response.data.message, "error");
            // @ts-ignore
            auth.authError(e.response.status);
        })

        let startTotal = (document.getElementById("startTotal") as HTMLInputElement)?.value;
        let endTotal = (document.getElementById("endTotal") as HTMLInputElement)?.value;

        await fetchTotalOrdersPerService(startTotal, endTotal).then(
            res => {
                console.log("res for total orders per service", res);
                setTotalOrdersPerService(res)
            }
        ).catch((e: any) => {
            console.error(e);
            // @ts-ignore
            swal("Error", "Error while fetching data : " + e.response.data.message, "error");
            // @ts-ignore
            auth.authError(e.response.status);
        })

        let startTimeBy = (document.getElementById("startTimeBy") as HTMLInputElement)?.value;
        let endTimeBy = (document.getElementById("endTimeBy") as HTMLInputElement)?.value;
        await fetchTimeByServiceReport(startTimeBy, endTimeBy)
            .then(res => {
                console.log("res for time by ", res);
                setTimeByService(res)
            })
            .catch((e: any) => {
                console.error(e);
                // @ts-ignore
                swal("Error", "Error while fetching data : " + e.response.data.message, "error");

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

    const addOrRemoveTotalOrdersPerService = () => {
        setDisplayTotalOrdersPerService(!displayTotalOrdersPerService)
    }

    const addOrRemoveTextForOrdersPerMonth = () => {
        setShowTextForOrdersPerMonth(!showTextForOrdersPerMonth)
    }

    const addOrRemoveTextForOrdersPerService = () => {
        setShowTextForOrdersPerService(!showTextForOrdersPerService)
    }

    React.useEffect(() => {


        fetchData();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);


    return (
        <div>
            <NavigationBar/>
            <center className={"mt-4"}>
                <Link to={"/reports"}>Back to Reports</Link>

                <div className={"text-center container"}>
                    <div className={"row"}>
                        <section className={"col-12 col-md-5 shadow-lg p-3 rounded-5 mt-5 mt-md-0"}>
                            <div className={"w-100"}>

                                <h1>Reports for Orders per Month</h1>
                                <h2>Display Orders Per Month</h2>
                                <button onClick={addOrRemoveOrdersPerMonth}
                                        className={displayOrdersPerMonth ? "btn btn-danger m-2" : "btn btn-primary m-2"}
                                >
                                    {displayOrdersPerMonth ? "Remove" : "Add"} Orders Per Month
                                </button>
                                <div>

                                    <h2>Choose Diagrams</h2>
                                    <button onClick={() => {
                                        if (displayOrdersPerMonthChoice.includes(1)) {
                                            setDisplayOrdersPerMonthChoice(displayOrdersPerMonthChoice.filter((choice) => choice !== 1))
                                        } else {
                                            setDisplayOrdersPerMonthChoice([...displayOrdersPerMonthChoice, 1])
                                        }
                                    }}
                                            className={displayOrdersPerMonthChoice.includes(1) ? "btn btn-danger m-2" : "btn btn-primary m-2"}
                                    >
                                        {displayOrdersPerMonthChoice.includes(1) ? "Remove" : "Add"} Bar Chart
                                    </button>
                                    <h2>Choose Year</h2>
                                    <input type="number" min="1900" max={currentYear} step="1"
                                           defaultValue={currentYear}
                                           id="year"/>
                                    <h2>Display Text</h2>
                                    <button onClick={addOrRemoveTextForOrdersPerMonth}
                                            className={showTextForOrdersPerMonth ? "btn btn-danger m-2" : "btn btn-primary m-2"}
                                    >
                                        {showTextForOrdersPerMonth ? "Remove" : "Add"} Text
                                    </button>
                                </div>
                            </div>
                        </section>
                        <div className={"col-2"}></div>
                        <section className={"col-12 col-md-5 shadow-lg p-3 rounded-5 mt-5 mt-md-0"}>
                            <div className={"w-100"}>
                                <h1>Reports for Orders per Service</h1>
                                <h2>Display Orders Per Service</h2>
                                <button onClick={addOrRemoveOrdersPerService}
                                        className={displayOrdersPerService ? "btn btn-danger m-2" : "btn btn-primary m-2"}
                                >
                                    {displayOrdersPerService ? "Remove" : "Add"} Orders Per Service
                                </button>
                                <div>

                                    <h2>Choose Dates</h2>
                                    <input type="date" id="start" defaultValue={getOneYearAgo()}/>
                                    {" "}to{" "}
                                    <input type="date" id="end" defaultValue={new Date().toISOString().split('T')[0]}/>

                                    <h2>Choose Diagrams</h2>

                                    <button onClick={() => {
                                        if (page2Choice.includes(1)) {
                                            setPage2Choice(page2Choice.filter((choice) => choice !== 1))
                                        } else {
                                            setPage2Choice([...page2Choice, 1])
                                        }
                                    }}
                                            className={page2Choice.includes(1) ? "btn btn-danger m-2" : "btn btn-primary m-2"}
                                    >
                                        {page2Choice.includes(1) ? "Remove" : "Add"} Line Chart
                                    </button>
                                    <button onClick={() => {
                                        if (page2Choice.includes(2)) {
                                            setPage2Choice(page2Choice.filter((choice) => choice !== 2))
                                        } else {
                                            setPage2Choice([...page2Choice, 2])
                                        }
                                    }}
                                            className={page2Choice.includes(2) ? "btn btn-danger m-2" : "btn btn-primary m-2"}
                                    >
                                        {page2Choice.includes(2) ? "Remove" : "Add"} Line Chart
                                    </button>

                                    <button onClick={() => {
                                        if (page2Choice.includes(3)) {
                                            setPage2Choice(page2Choice.filter((choice) => choice !== 3))
                                        } else {
                                            setPage2Choice([...page2Choice, 3])
                                        }
                                    }
                                    }
                                            className={page2Choice.includes(3) ? "btn btn-danger m-2" : "btn btn-primary m-2"}
                                    >
                                        {page2Choice.includes(3) ? "Remove" : "Add"} Bar Chart
                                    </button>
                                    <h2>Display Text</h2>
                                    <button onClick={addOrRemoveTextForOrdersPerService}
                                            className={showTextForOrdersPerService ? "btn btn-danger m-2" : "btn btn-primary m-2"}
                                    >
                                        {showTextForOrdersPerService ? "Remove" : "Add"} Text
                                    </button>
                                </div>

                            </div>
                        </section>
                    </div>
                    <div className={"row mt-5"}
                    >
                        <section className={"col-12 col-md-5 shadow-lg p-3 rounded-5"}>

                            <div className={"w-100"}>
                                <h3>Reports for Total Orders Per Service</h3>
                                <h4>Display Total Orders Per Service</h4>
                                <h4>Choose Dates</h4>
                                <button onClick={addOrRemoveTotalOrdersPerService}
                                        className={displayTotalOrdersPerService ? "btn btn-danger m-2" : "btn btn-primary m-2"}
                                >
                                    {displayTotalOrdersPerService ? "Remove" : "Add"} Total Orders Per Service
                                </button>
                                <br/>
                                <input type="date" id="startTotal" defaultValue={getOneYearAgo()}/>
                                {" "}to{" "}
                                <input type="date" id="endTotal" defaultValue={new Date().toISOString().split('T')[0]}/>

                                <h3>Choose Diagrams</h3>
                                <button onClick={() => {
                                    if (totalOrdersPerServiceChoice.includes(1)) {
                                        setTotalOrdersPerServiceChoice(totalOrdersPerServiceChoice.filter((choice) => choice !== 1))
                                    } else {
                                        setTotalOrdersPerServiceChoice([...totalOrdersPerServiceChoice, 1])
                                    }
                                }}
                                        className={totalOrdersPerServiceChoice.includes(1) ? "btn btn-danger m-2" : "btn btn-primary m-2"}
                                >
                                    {totalOrdersPerServiceChoice.includes(1) ? "Remove" : "Add"} Bar Chart
                                </button>
                                <button onClick={() => {
                                    if (totalOrdersPerServiceChoice.includes(2)) {
                                        setTotalOrdersPerServiceChoice(totalOrdersPerServiceChoice.filter((choice) => choice !== 2))
                                    } else {
                                        setTotalOrdersPerServiceChoice([...totalOrdersPerServiceChoice, 2])
                                    }
                                }}
                                        className={totalOrdersPerServiceChoice.includes(2) ? "btn btn-danger m-2" : "btn btn-primary m-2"}
                                >
                                    {totalOrdersPerServiceChoice.includes(2) ? "Remove" : "Add"} Pie Chart
                                </button>
                            </div>
                        </section>
                        <div className={"col-2"}></div>
                        <section className={"col-12 col-md-5 shadow-lg p-3 rounded-5 mt-5 mt-md-0"}>
                            <div className={"w-100"}>

                                <h1>Reports for Time by Service</h1>
                                <h2>Display Time By Service</h2>
                                <button onClick={addOrRemoveTimeByService}
                                        className={displayTimeByService ? "btn btn-danger m-2" : "btn btn-primary m-2"}
                                >
                                    {displayTimeByService ? "Remove" : "Add"} Time By Service
                                </button>
                                <h2>Choose Dates</h2>
                                <input type="date" id="startTimeBy" defaultValue={getOneYearAgo()}/>
                                {" "}to{" "}
                                <input type="date" id="endTimeBy"
                                       defaultValue={new Date().toISOString().split('T')[0]}/>
                                <h2>Choose Diagrams</h2>
                                <button
                                    onClick={() => {
                                        if (timeByServiceChoice.includes(1)) {
                                            setTimeByServiceChoice(timeByServiceChoice.filter((choice) => choice !== 1))
                                        } else {
                                            setTimeByServiceChoice([...timeByServiceChoice, 1])
                                        }
                                    }}
                                    className={timeByServiceChoice.includes(1) ? "btn btn-danger m-2" : "btn btn-primary m-2"}
                                >
                                    {timeByServiceChoice.includes(1) ? "Remove" : "Add"} Line Chart
                                </button>
                                <button onClick={() => {
                                    if (timeByServiceChoice.includes(2)) {
                                        setTimeByServiceChoice(timeByServiceChoice.filter((choice) => choice !== 2))
                                    } else {
                                        setTimeByServiceChoice([...timeByServiceChoice, 2])
                                    }
                                }}
                                        className={timeByServiceChoice.includes(2) ? "btn btn-danger m-2" : "btn btn-primary m-2"}
                                >
                                    {timeByServiceChoice.includes(2) ? "Remove" : "Add"} Pie Chart
                                </button>
                            </div>
                        </section>
                    </div>
                </div>
                <button onClick={fetchData} className={"m-5 btn btn-primary"}>
                    Refresh PDF
                </button>
            </center>
            <PDFViewer width={"100%"} height={"900px"}>
                <Document
                    title={"Corso Reports"}
                    author="Corso"
                    subject="Corso Reports"
                    keywords="corso, reports, pdf"

                >

                    <PresentationPage/>
                    {displayOrdersPerMonth &&
                        <Page size="A4">

                            <div>

                                <Text style={{
                                    fontSize: 20,
                                    margin: 12
                                }}>
                                    Orders Per Month
                                    (In {(document.getElementById("year") as HTMLInputElement)?.value || currentYear})
                                </Text>

                                <OrdersPerMonthPDF ordersPerMonth={ordersPerMonth}
                                                   choice={displayOrdersPerMonthChoice}/>

                                {showTextForOrdersPerMonth &&

                                    ordersPerMonth.map((month: any) => {
                                        return <Text style={{
                                            fontSize: 12,
                                            margin: 12
                                        }} key={month.month}>{getMonthName(month.month)}: {month.totalOrders}</Text>
                                    })

                                }
                            </div>
                        </Page>

                    }

                    {displayOrdersPerService &&
                        <Page size="A4">

                            <div>
                                <Text style={{
                                    fontSize: 20,
                                    margin: 12
                                }}>
                                    Orders Per Service
                                    (Between {(document.getElementById("start") as HTMLInputElement)?.value || "choose a date"} and {(document.getElementById("end") as HTMLInputElement)?.value})
                                </Text>
                                <OrdersPerServicePDF ordersPerService={orderPerService} allServices={allServices}
                                                     choice={page2Choice}/>

                                {showTextForOrdersPerService &&

                                    <View style={{flexDirection: 'row', flexWrap: 'wrap'}}>
                                        {orderPerService.map((service: any) => {
                                            console.log("service", service);
                                            return (
                                                <Text
                                                    wrap={false}
                                                    style={{
                                                        fontSize: 12,
                                                        margin: 12,
                                                    }} key={service.date}>
                                                    <Text style={{
                                                        fontSize: 20,
                                                        margin: 12,
                                                    }}>
                                                        {checkIfDatesAreMoreOrEqualThanTwoMonths(
                                                            (document.getElementById("start") as HTMLInputElement)?.value || new Date().toISOString(),
                                                            (document.getElementById("end") as HTMLInputElement)?.value || new Date().toISOString()
                                                        ) ? service.date.substring(0, 7) : service.date}
                                                    </Text>
                                                    {"\n"}
                                                    {Object.keys(service)
                                                        .filter((key) => key !== "date")
                                                        .map((key) => (
                                                            <Text key={key}>
                                                                {key}: {service[key]}
                                                                {"\n"}
                                                            </Text>
                                                        ))}
                                                </Text>
                                            );
                                        })}
                                    </View>
                                }
                            </div>
                        </Page>

                    }
                    {displayTotalOrdersPerService &&
                        <Page size="A4">

                            <div>
                                <Text style={{
                                    fontSize: 20,
                                    margin: 12
                                }}>
                                    Total Orders Per Service
                                    (Between {(document.getElementById("startTotal") as HTMLInputElement)?.value || "choose date"} and {(document.getElementById("endTotal") as HTMLInputElement)?.value || "choose date"})
                                </Text>
                                <TotalOrdersPerServicePDF totalOrdersPerService={totalOrdersPerService}
                                                          choice={totalOrdersPerServiceChoice}/>

                                <View style={{flexDirection: 'row', flexWrap: 'wrap'}} wrap={false}>
                                    {totalOrdersPerService.map((service: any) => (
                                        <Text
                                            style={{
                                                fontSize: 12,
                                                margin: 12,
                                            }} key={service.serviceName}>
                                            <Text
                                                wrap={false}
                                                style={{
                                                    color: stringToColour(service.serviceName),
                                                    backgroundColor: stringToColour(service.serviceName),
                                                    padding: 5,
                                                    margin: 5,
                                                    borderRadius: 5,
                                                }}>
                                                M
                                            </Text>
                                            {"    "}
                                            {service.serviceName}: {service.totalOrderRequest} Orders
                                        </Text>
                                    ))}
                                </View>
                            </div>
                        </Page>
                    }
                    {displayTimeByService &&
                        <Page size="A4">

                            <div>

                                <Text style={{
                                    fontSize: 20,
                                    margin: 12
                                }}>
                                    Time By Service
                                    (Between {(document.getElementById("startTimeBy") as HTMLInputElement)?.value || "choose date"} and {(document.getElementById("endTimeBy") as HTMLInputElement)?.value || "choose date"})
                                </Text>
                                <TimeByServicePDF data={timeByService} choice={timeByServiceChoice}/>
                                <View style={{flexDirection: 'row', flexWrap: 'wrap'}} wrap={false}>
                                    {timeByService.map((service: any) => (
                                        <Text
                                            style={{
                                                fontSize: 12,
                                                margin: 12,
                                            }} key={service.serviceName}>
                                            <Text
                                                wrap={false}
                                                style={{
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
                                    ))}
                                </View>
                            </div>
                        </Page>
                    }


                </Document>
            </PDFViewer>
        </div>
    )
}