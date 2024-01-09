import './App.css';
import {BrowserRouter, Route, Routes} from "react-router-dom";
import MainPage from "./Pages/MainPage";
import LogoutRedirect from "./security/logoutRedirect";
import VerifyRedirect from "./security/verifyRedirect";
import {AuthProvider} from "./security/Components/AuthProvider";
import ExternalHandling from "./security/externalProvidersHandling/externalHandling";
import Dashboard from "./Pages/Dashboard/Dashboard";
import CreateAdminPage from "./Pages/CreateAdmin/CreateAdminPage";
import CurrentOrdersPage from "./Pages/OrdersPages/CurrentOrdersPage";
import ProfilePage from "./Pages/Profile/ProfilePage";
import ViewAllFaq from "./Pages/AllFAQs/ViewAllFaq";

import CompletedOrdersPage from "./Pages/OrdersPages/CompleteOrdersPage";
import ServiceDetailsPage from "./Pages/ViewServices/ServiceDetailsPage";
import PendingOrdersPage from "./Pages/OrdersPages/PendingOrdersPage";
import CreateExternalOrderPage from "./Pages/CreateExternalOrder/CreateExternalOrderPage";
import ServicesThroughDashboard from "./Pages/ViewServices/ServicesThroughDashboard";
import AddServicePage from "./Pages/AddService/AddServicePage";
import ViewSpecificOrderAdmin from "./Pages/OrdersDetailsPages/ViewSpecificOrderAdmin";
import OverdueOrdersPage from "./Pages/OrdersPages/OverdueOrdersPage";
import AllOrdersPage from "./Pages/OrdersPages/AllOrdersPage";
import ViewSpecificOrder from "./Pages/OrdersDetailsPages/Customer/ViewSpecificOrder";
import AddCustomerInformation from "./Pages/Profile/ProfileLinkedPages/AddCustomerInformation";
import AllCustomerOrders from "./Pages/Profile/ProfileLinkedPages/AllCustomerOrders";
import {useEffect} from "react";

import ManageReviews from "./Pages/AllReviews/ManageReviews";


function App() {
    const checkServerConnection = () => {
        fetch("http://localhost:8080/api/v1/corso/server/status",
            {
                method: 'HEAD'
            }).then(
            function (response) {
                if (response.status !== 200) {
                    console.log('Looks like there was a problem. Status Code: ' +
                        response.status);
                    // @ts-ignore
                    swal("Error", "Server is down !", "error")
                        .then(() => {
                            window.location.href = "http://localhost:3000/"
                        })
                }
            }
        ).catch(function (err) {
            console.log('Fetch Error :-S', err);
            // @ts-ignore
            swal("Error", "Server is down !", "error")
                .then(() => {
                    window.location.href = "http://localhost:3000/"
                })
            if (err.response === undefined) {
                // @ts-ignore
               return;
            }

            if(err.response.status === 401){
                // @ts-ignore
                return
            }

        })

    }

    useEffect(() => {
        checkServerConnection()
    }, []);
  return (
    <>
        <AuthProvider>
            <BrowserRouter>
                <Routes>
                    <Route path="/" element={<MainPage />} />
                    <Route path="/faq" element={<ViewAllFaq/>} />
                    <Route path="/dashboard" element={<Dashboard />} />
                    <Route path="/create/orders/external" element={<CreateExternalOrderPage/>} />
                    <Route path="/orders/all" element={<AllOrdersPage/>} />
                    <Route path="/orders/current" element={<CurrentOrdersPage/>} />
                    <Route path="/orders/completed" element={<CompletedOrdersPage/>} />
                    <Route path="/orders/pending" element={<PendingOrdersPage/>} />
                    <Route path="/orders/overdue" element={<OverdueOrdersPage/>} />
                    <Route path="/manage/testimonies" element={<ManageReviews/>} />
                        <Route path="/profile" element={<ProfilePage/>}/>
                        <Route path="/profile/edit" element={<AddCustomerInformation/>}/>
                        <Route path="/profile/orders" element={<AllCustomerOrders/>}/>


                        <Route path="/create/admin" element={<CreateAdminPage/>}/>
                        <Route path="/manage/orders/:orderId" element={<ViewSpecificOrderAdmin/>}/>
                        <Route path="/orders/:orderId" element={<ViewSpecificOrder/>}/>
                        <Route path="/logout" element={<LogoutRedirect/>}/>
                        <Route path="/verify" element={<VerifyRedirect/>}/>
                        <Route path="/external" element={<ExternalHandling/>}/>
                        <Route path={"/services"} element={<ServicesThroughDashboard/>}/>
                        <Route path={"/add-service"} element={<AddServicePage/>}/>
                        <Route path="/viewServiceDetails/:serviceId" element={<ServiceDetailsPage/>}/>
                    </Routes>
                </BrowserRouter>
            </AuthProvider>
        </>
    )
}

export default App;
