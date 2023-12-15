import './App.css';
import {BrowserRouter, Route, Routes} from "react-router-dom";
import MainPage from "./Pages/MainPage";
import LogoutRedirect from "./security/logoutRedirect";
import VerifyRedirect from "./security/verifyRedirect";
import {AuthProvider} from "./security/Components/AuthProvider";
import ExternalHandling from "./security/externalProvidersHandling/externalHandling";
import Dashboard from "./Pages/Dashboard/Dashboard";
import CreateAdminPage from "./Pages/CreateAdmin/CreateAdminPage";
import CurrentOrdersPage from "./Pages/CurrentOrdersPage";
import ProfilePage from "./Pages/Profile/ProfilePage";
import ViewAllFaq from "./Pages/AllFAQs/ViewAllFaq";

import CompletedOrdersPage from "./Pages/CompleteOrdersPage";
import ServiceDetailsPage from "./Pages/ServiceDetailsPage";
import PendingOrdersPage from "./PendingOrdersPage";


function App() {

  return (
    <>
        <AuthProvider>
            <BrowserRouter>
                <Routes>
                    <Route path="/" element={<MainPage />} />
                    <Route path="/faq" element={<ViewAllFaq/>} />
                    <Route path="/dashboard" element={<Dashboard />} />
                    <Route path="/orders/current" element={<CurrentOrdersPage/>} />
                    <Route path="/orders/completed" element={<CompletedOrdersPage/>} />
                    <Route path="/orders/pending" element={<PendingOrdersPage/>} />
                    <Route path={"/profile"} element={<ProfilePage />} />
                    <Route path="/profile" element={<ProfilePage />} />
                    <Route path="/create/admin" element={<CreateAdminPage />} />
                    <Route path={"/profile"} element={<ProfilePage/>} />
                    <Route path="/create/admin" element={<CreateAdminPage/>} />
                    <Route path="/logout" element={<LogoutRedirect />} />
                    <Route path="/verify" element={<VerifyRedirect />} />
                    <Route path="/external" element={<ExternalHandling />} />
                    <Route path="/viewServiceDetails/:serviceId" element={<ServiceDetailsPage />} />
                </Routes>
            </BrowserRouter>
        </AuthProvider>
    </>
  )
}

export default App;
