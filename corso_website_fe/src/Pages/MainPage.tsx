import OurPreviousProjects from "../Main Page Components/OurPreviousProjects";
import NavigationBar from "../Components/NavigationBar";
import axios from "axios";
import {useAuth} from "../security/Components/AuthProvider";


function MainPage() {
    const auth = useAuth();





    const addAdmin = async (event: any) => {
        event.preventDefault();
        // @ts-ignore
        await axios.post("http://localhost:8080/api/v1/corso/auth0/manage/add-admin", {
            username: event.target[0].value,
            email: event.target[1].value,
            password: event.target[2].value
        }, {
            headers: {
                // @ts-ignore
                'X-XSRF-TOKEN': auth.getXsrfToken()
            }
        })
            .then(r => console.log('Response:', r))
            .catch(e => {
                // @ts-ignore
                auth.authError(e.response.status);
            })
    }


    return (
        <div>
            <NavigationBar />
            <h1>Main Page</h1>
            <OurPreviousProjects />
            {
                // @ts-ignore
                //suppress when u get red error on isAuthenticated
                auth.isAuthenticated &&
                <>
                    <form onSubmit={addAdmin}>
                        <input type={"text"} placeholder={"username"} />
                        <input type={"text"} placeholder={"email"} />
                        <input type={"password"} placeholder={"password"} />
                        <button type={"submit"}>Add Admin</button>
                    </form>
                </>
}

        </div>
    );
}

export default MainPage;
