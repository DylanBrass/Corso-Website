import OurPreviousProjects from "../Main Page Components/OurPreviousProjects";
import NavigationBar from "../Components/NavigationBar";
import axios from "axios";
import {useAuth} from "../security/Components/AuthProvider";


function Dashboard() {
    const auth = useAuth();

    return (
        <div>
            <NavigationBar />
            <h1>Welcome Back </h1>



        </div>
    );
}

export default Dashboard;
