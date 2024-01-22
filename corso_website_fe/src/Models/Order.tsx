import ServiceOrder from "./ServiceOrder";


class Order {
    public orderId: string;
    public orderTrackingNumber: string;
    public serviceId: string;
    public service: ServiceOrder;
    public userId: string;
    public progressInformation: string;
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
    public estimatedDuration: number;
    public hoursWorked: number;


    constructor(orderId: string, orderTrackingNumber: string, serviceId: string, service: ServiceOrder, userId: string, progressInformation: string, orderDate: string, dueDate: string, orderStatus: string, orderDescription: string, customerFullName: string, customerEmail: string, customerPhone: string, customerAddress: string, customerCity: string, customerPostalCode: string, customerApartmentNumber: string, estimatedDuration: number, hoursWorked: number) {
        this.orderId = orderId;
        this.orderTrackingNumber = orderTrackingNumber;
        this.serviceId = serviceId;
        this.service = service;
        this.userId = userId;
        this.progressInformation = progressInformation;
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
        this.estimatedDuration = estimatedDuration;
        this.hoursWorked = hoursWorked;
    }
}

export default Order;