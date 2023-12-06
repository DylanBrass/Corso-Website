import OurPreviousProjects from "../Main Page Components/OurPreviousProjects";
import NavigationBar from "../Components/NavigationBar";
import axios from "axios";
import {useState} from "react";


function MainPage() {
    const csrfToken = document.cookie.replace(/(?:(?:^|.*;\s*)XSRF-TOKEN\s*\=\s*([^;]*).*$)|^.*$/, '$1');
    const [data, setData] = useState("")

    const postTest = async (event: any) => {
        event.preventDefault();
        const response = await axios.post("http://localhost:8080/api/v1/corso/security/test", {
            "name": "test",
            "surname": "test"
        }, {
            headers: {
                'X-XSRF-TOKEN': csrfToken
            }
        })
            .then(r => console.log('Response:', r))
            .catch(e => console.error('Error fetching test:', e));
    }

    return (
        <div>
            <NavigationBar />
            <h1>Main Page</h1>
            <OurPreviousProjects />
            <form onSubmit={postTest}>
                <button type="submit">Get test</button>
            </form>


        </div>
    );
}

export default MainPage;
