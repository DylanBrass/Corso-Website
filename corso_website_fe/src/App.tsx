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

import ManageReviews from "./Pages/AllReviews/ManageReviews";
import EditGallery from "./Pages/Gallery/EditGallery";
import ViewSpecificServiceAdmin from "./Pages/ViewASpecificServiceFromTheAdminPanel/ViewSpecificServiceAdmin";
import CustomerReviews from "./Pages/CustomerReviews/CustomerReviews";


export default function App() {
    
    return (
        <>
            <AuthProvider>
                <BrowserRouter>
                    <Routes>
                        <Route path="/" element={<MainPage/>}/>
                        <Route path="/faq" element={<ViewAllFaq/>}/>
                        <Route path="/dashboard" element={<Dashboard/>}/>
                        <Route path="/create/orders/external" element={<CreateExternalOrderPage/>}/>
                        <Route path="/orders/all" element={<AllOrdersPage/>}/>
                        <Route path="/orders/current" element={<CurrentOrdersPage/>}/>
                        <Route path="/orders/completed" element={<CompletedOrdersPage/>}/>
                        <Route path="/orders/pending" element={<PendingOrdersPage/>}/>
                        <Route path="/orders/overdue" element={<OverdueOrdersPage/>}/>
                        <Route path="/manage/testimonies" element={<ManageReviews/>}/>
                        <Route path="/carousel/order" element={<EditGallery/>}/>
                        <Route path="/profile" element={<ProfilePage/>}/>
                        <Route path="/profile/edit" element={<AddCustomerInformation/>}/>
                        <Route path="/profile/orders" element={<AllCustomerOrders/>}/>
                        <Route path="/adminServices/:serviceId" element={<ViewSpecificServiceAdmin/>}/>
                        <Route path="/profile/reviews" element={<CustomerReviews/>}/>

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

