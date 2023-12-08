import './App.css';
import {BrowserRouter, Route, Routes} from "react-router-dom";
import MainPage from "./Pages/MainPage";
import LogoutRedirect from "./security/logoutRedirect";
import VerifyRedirect from "./security/verifyRedirect";
import {AuthProvider} from "./security/Components/AuthProvider";
import ExternalHandling from "./security/externalProvidersHandling/externalHandling";


function App() {

  return (
    <>
        <AuthProvider>
            <BrowserRouter>
                <Routes>
                    <Route path="/" element={<MainPage />} />
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
