import {useAuth} from "../../security/Components/AuthProvider";
import NavigationBar from "../../Components/NavBar/NavigationBar";
import AddCustomerInformation from "./Components/AddCustomerInformation";
import AllCustomerOrders from "./Components/AllCustomerOrders";
import Footer from "../../Components/Footer/Footer";
import React from "react";




function ProfilePage() {
    const auth = useAuth();


    return (
        <div>
            <NavigationBar/>
            <AddCustomerInformation/>
            <AllCustomerOrders/>
            <Footer/>
        </div>
    );
}

export default ProfilePage;
