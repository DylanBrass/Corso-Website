// Really cool function found here : https://stackoverflow.com/questions/3426404/create-a-hexadecimal-colour-based-on-a-string-with-javascript
import axios from "axios";


const stringToColour = (str: string) => {
    let hash = 0;
    str.split('').forEach((char) => {
        hash = char.charCodeAt(0) + ((hash << 5) - hash);
    });

    const minComponentValue = 50;
    const maxComponentValue = 200;

    let colour = '#';
    for (let i = 0; i < 3; i++) {
        const value = (hash >> (i * 8)) & 0xff;
        const adjustedValue = Math.max(
            minComponentValue,
            Math.min(maxComponentValue, value)
        );

        colour += adjustedValue.toString(16).padStart(2, '0');
    }

    return colour;
};


const getOneYearAgo = (): string => {
    let date = new Date();
    date.setFullYear(date.getFullYear() - 1);
    return date.toISOString().split('T')[0];
}

const getOrdersPerMonth = async (selectedYear: number | string) => {


    return await axios.get(`${process.env.REACT_APP_BE_HOST}api/v1/corso/orders/orders-per-month?year=${selectedYear}`)
        .then(res => {
            console.log(res.data);
            return res.data;
        })
        .catch(e => {
            console.error(e);
            throw e;
        });


}


const getOrdersPerService = async (start: string, end: string, allServices: any) => {

    let url = `${process.env.REACT_APP_BE_HOST}api/v1/corso/services/orders-per-service`;
    if (start && end) {
        url = `${url}?date_start=${start}&date_end=${end}`
    }
    return axios.get(url)
        .then(res => {


            let services: any[] = allServices;
            Object.keys(res.data[0]).forEach(key => {
                if (key !== "date" && !allServices.includes(key)) {
                    services.push(key)
                }
            })


            return {res: res.data, services: services}
        })
        .catch(err => {
            console.error(err);
            throw err;
        })
}


const fetchTimeByServiceReport = async (start: string | Date, end: string | Date) => {


    let url = `${process.env.REACT_APP_BE_HOST}api/v1/corso/services/time-by-service`;
    return await fetch(start, end, url);
}


const fetchTotalOrdersPerService = async (start: string | Date, end: string | Date) => {

    let url = `${process.env.REACT_APP_BE_HOST}api/v1/corso/services/total-orders-per-service`;
    return await fetch(start, end, url);
}

async function fetch(start: string | Date, end: string | Date, url: string) {
    if (start && end) {
        url = `${url}?date_start=${start}&date_end=${end}`
    }
    try {
        const res = await axios.get(url);
        res.data.forEach((service: any) => {
                service.fill = stringToColour(service.serviceName);
            }
        );
        return res.data;
    } catch (e) {
        console.error(e);
        throw e;
    }
}

export {
    getOneYearAgo,
    stringToColour,
    getOrdersPerMonth,
    getOrdersPerService,
    fetchTimeByServiceReport,
    fetchTotalOrdersPerService
}