import OurPreviousProjects from "../Main Page Components/OurPreviousProjects";
import NavigationBar from "../Components/NavigationBar";
import axios from "axios";
import {useState} from "react";


function MainPage() {

    const [data, setData] = useState("")

    const fetchTest = async (event: any) => {
        event.preventDefault();
        const response = await axios.get("http://localhost:8080/api/v1/corso/galleries/test")
            .then(r => setData(r.data))
            .catch(e => console.error('Error fetching test:', e));
        console.log('Response:', response)
        
    };

    return (
        <div>
            <NavigationBar />
            <h1>Main Page</h1>
            <OurPreviousProjects />
            <form onSubmit={fetchTest}>
                <button type="submit">Get test</button>
            </form>
            <p>{data}</p>
        </div>
    );
}

export default MainPage;
