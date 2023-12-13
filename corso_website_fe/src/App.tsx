import './App.css';
import {BrowserRouter, Route, Routes} from "react-router-dom";
import MainPage from "./Pages/MainPage";
import LogoutRedirect from "./security/logoutRedirect";
import VerifyRedirect from "./security/verifyRedirect";
import {AuthProvider} from "./security/Components/AuthProvider";
import ExternalHandling from "./security/externalProvidersHandling/externalHandling";
import Dashboard from "./Pages/Dashboard";
import CreateAdminPage from "./Pages/CreateAdminPage";
import CurrentOrdersPage from "./Pages/CurrentOrdersPage";


function App() {

  return (
    <>
        <AuthProvider>
            <BrowserRouter>
                <Routes>
                    <Route path="/" element={<MainPage />} />
                    <Route path="/dashboard" element={<Dashboard />} />
                    <Route path="/orders/current" element={<CurrentOrdersPage/>} />
                    <Route path="/create/admin" element={<CreateAdminPage />} />
                    <Route path="/logout" element={<LogoutRedirect />} />
                    <Route path="/verify" element={<VerifyRedirect />} />
                    <Route path="/external" element={<ExternalHandling />} />
                </Routes>
            </BrowserRouter>
        </AuthProvider>
    </>
  );
}

export default App;
