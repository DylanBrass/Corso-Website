import Service from "./Service";


class Order {
    public orderTrackingNumber: string;
    public serviceId: string;
    public service: Service;
    public userId: string;
    public orderDate: string;
    public dueDate: string;
    public orderStatus: string;
    public orderDescription: string;
    public customerFullName: string;
    public customerEmail: string;
    public customerPhone: string;
    public customerAddress: string;
    public customerCity: string;
    public customerPostalCode: string;
    public customerApartmentNumber: string;


    constructor(orderTrackingNumber: string, serviceId: string, service: Service, userId: string, orderDate: string, dueDate: string, orderStatus: string, orderDescription: string, customerFullName: string, customerEmail: string, customerPhone: string, customerAddress: string, customerCity: string, customerPostalCode: string, customerApartmentNumber: string) {
        this.orderTrackingNumber = orderTrackingNumber;
        this.serviceId = serviceId;
        this.service = service;
        this.userId = userId;
        this.orderDate = orderDate;
        this.dueDate = dueDate;
        this.orderStatus = orderStatus;
        this.orderDescription = orderDescription;
        this.customerFullName = customerFullName;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.customerAddress = customerAddress;
        this.customerCity = customerCity;
        this.customerPostalCode = customerPostalCode;
        this.customerApartmentNumber = customerApartmentNumber;
    }
}

export default Order;