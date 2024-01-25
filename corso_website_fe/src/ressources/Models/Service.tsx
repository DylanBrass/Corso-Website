class Service {
    public serviceId: string;
    public serviceName: string;
    public serviceDescription: string;
    public serviceImage: string;
    public serviceIcon: string;

    constructor(serviceId: string, serviceName: string, serviceDescription: string, serviceImage: string, serviceIcon: string) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.serviceDescription = serviceDescription;
        this.serviceImage = serviceImage;
        this.serviceIcon = serviceIcon;
    }
}



export default Service;
