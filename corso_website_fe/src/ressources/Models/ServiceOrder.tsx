class ServiceOrder {
    public serviceId: string;
    public serviceName: string;
    public serviceDescription: string;

    constructor(serviceId: string, serviceName: string, serviceDescription: string) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.serviceDescription = serviceDescription;
    }
}



export default ServiceOrder;
