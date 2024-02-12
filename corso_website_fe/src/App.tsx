import './App.css';
import {BrowserRouter, Route, Routes} from "react-router-dom";
import MainPage from "./Pages/MainPage/MainPage";
import LogoutRedirect from "./security/logoutRedirect";
import VerifyRedirect from "./security/verifyRedirect";
import {AuthProvider} from "./security/Components/AuthProvider";
import ExternalHandling from "./security/externalProvidersHandling/externalHandling";
import Dashboard from "./Pages/Dashboard/Dashboard";
import CreateAdminPage from "./Pages/Dashboard/CreateAdmin/CreateAdminPage";
import CurrentOrdersPage from "./Pages/Dashboard/OrdersPages/CurrentOrdersPage";
import ProfilePage from "./Pages/Profile/ProfilePage";
import ViewAllFaq from "./Pages/MainPage/AllFAQs/ViewAllFaq";
import AddAFaq from "./Pages/Dashboard/ManageFaq/AddAFaq";

import CompletedOrdersPage from "./Pages/Dashboard/OrdersPages/CompleteOrdersPage";
import ServiceDetailsPage from "./Pages/Dashboard/ManageServices/ServiceDetailsPage";
import PendingOrdersPage from "./Pages/Dashboard/OrdersPages/PendingOrdersPage";
import CreateExternalOrderPage from "./Pages/Dashboard/CreateExternalOrder/CreateExternalOrderPage";
import ServicesThroughDashboard from "./Pages/Dashboard/ManageServices/ServicesThroughDashboard";
import AddServicePage from "./Pages/Dashboard/AddService/AddServicePage";
import ViewSpecificOrderAdmin from "./Pages/Dashboard/OrdersDetailsPages/ViewSpecificOrderAdmin";
import OverdueOrdersPage from "./Pages/Dashboard/OrdersPages/OverdueOrdersPage";
import AllOrdersPage from "./Pages/Dashboard/OrdersPages/AllOrdersPage";
import ViewSpecificOrder from "./Pages/Profile/ViewOrder/ViewSpecificOrder";
import AddCustomerInformation from "./Pages/Profile/ProfileLinkedPages/AddCustomerInformation";
import AllCustomerOrders from "./Pages/Profile/ProfileLinkedPages/AllCustomerOrders";

import ManageReviews from "./Pages/Dashboard/ManageReviews/ManageReviews";
import EditGallery from "./Pages/Dashboard/Gallery/EditGallery";
import ViewSpecificServiceAdmin from "./Pages/Dashboard/ViewASpecificService/ViewSpecificServiceAdmin";
import CustomerReviews from "./Pages/Profile/ManageReviews/CustomerReviews";
import ViewOnGoingOrderDetailsPage from "./Pages/Dashboard/OrdersPages/ViewOnGoingOrderDetailsPage";
import CreateReview from "./Pages/Profile/CreateReview/CreateReview";
import ViewAllFaqThroughDashboard from "./Pages/Dashboard/ManageFaq/ViewAllFaqThroughDashboard";
import UpdateOrder from "./Pages/Profile/UpdateOrderCustomer/UpdateOrder";
import ViewSpecificFaq from "./Pages/Dashboard/ManageFaq/ViewSpecificFaq";
// @ts-ignore
import ModifyServicePage from "./Pages/Dashboard/UpdateService/ModifyServicePage";
import React from 'react';
import CustomerViewsSpecificReview from "./Pages/Profile/ManageReviews/CustomerViewsSpecificReview";
import ViewSpecificGallery from "./Pages/Dashboard/Gallery/UpdateGallery/ViewSpecificGallery";
import VerifyEmail from "./security/VerifyEmail/VerifyEmail";
import ReportsPage from "./Pages/Dashboard/ReportsPages/ReportsPage";
import MakePdfPage from "./Pages/Dashboard/ReportsPages/MakePdfPage";
import RequestOrder from "./Pages/Profile/RequestAnOrder/RequestOrderCustomer";
import ViewSpecificInactiveOrder from "./Pages/Dashboard/OrdersDetailsPages/ViewSpecificInactiveOrder";
import OrderProgressionPage from "./Pages/Dashboard/OrdersPages/OrderProgressionPage";


export default function App() {



    return (
        <>
            <AuthProvider>
                <BrowserRouter>
                    <Routes>
                        <Route path="/" element={<MainPage/>}/>
                        <Route path="/faq" element={<ViewAllFaq/>}/>
                        <Route path="/manage/faqs" element={<ViewAllFaqThroughDashboard/>}/>
                        <Route path={"/manage/faqs/create"} element={<AddAFaq/>}/>
                        <Route path="/faqs/:faqId" element={<ViewSpecificFaq/>}/>
                        <Route path="/dashboard" element={<Dashboard/>}/>
                        <Route path="/create/orders/external" element={<CreateExternalOrderPage/>}/>
                        <Route path="/orders/all" element={<AllOrdersPage/>}/>
                        <Route path="/orders/current" element={<CurrentOrdersPage/>}/>
                        <Route path="/orders/completed" element={<CompletedOrdersPage/>}/>
                        <Route path="/orders/pending" element={<PendingOrdersPage/>}/>
                        <Route path="/orders/overdue" element={<OverdueOrdersPage/>}/>
                        <Route path="/profile/manage/orders/:orderId" element={<UpdateOrder/>}/>
                        <Route path="/requestOrder" element={<RequestOrder/>}/>
                        <Route path="/manage/testimonies" element={<ManageReviews/>}/>
                        <Route path="/carousel/order" element={<EditGallery/>}/>
                        <Route path="/profile" element={<ProfilePage/>}/>
                        <Route path="/profile/edit" element={<AddCustomerInformation/>}/>
                        <Route path="/profile/orders" element={<AllCustomerOrders/>}/>
                        <Route path="/adminServices/:serviceId" element={<ViewSpecificServiceAdmin/>}/>
                        <Route path="/manage/currentOrder/:orderId" element={<ViewOnGoingOrderDetailsPage/>}/>
                        <Route path="/profile/reviews" element={<CustomerReviews/>}/>
                        <Route path="/profile/reviews/:reviewId" element={<CustomerViewsSpecificReview/>}/>
                        <Route path="/profile/reviews/create" element={<CreateReview/>}/>
                        <Route path="/create/admin" element={<CreateAdminPage/>}/>

                        <Route path={"/reports"} element={<ReportsPage/>}/>
                        <Route path={"/reports/make-pdf"} element={<MakePdfPage/>}/>



                        <Route path="/manage/gallery/:galleryId" element={<ViewSpecificGallery/>}/>
                        <Route path="/manage/orders/:orderId" element={<ViewSpecificOrderAdmin/>}/>
                        <Route path="/manage/orders/inactive/:orderId" element={<ViewSpecificInactiveOrder/>}/>

                        <Route path="/orders/:orderId" element={<ViewSpecificOrder/>}/>
                        <Route path="/manage/updateProgression/:orderId" element={<OrderProgressionPage/>}/>
                        <Route path="/logout" element={<LogoutRedirect/>}/>
                        <Route path="/verify" element={<VerifyRedirect/>}/>

                        <Route path={"/verify/email/:email/:token"} element={<VerifyEmail/>}/>

                        <Route path="/external" element={<ExternalHandling/>}/>
                        <Route path={"/services"} element={<ServicesThroughDashboard/>}/>
                        <Route path={"/add-service"} element={<AddServicePage/>}/>
                        <Route path="/modify-service/:serviceId" element={<ModifyServicePage/>}/>
                        <Route path="/viewServiceDetails/:serviceId" element={<ServiceDetailsPage/>}/>
                    </Routes>
                </BrowserRouter>
            </AuthProvider>
        </>
    )
}

