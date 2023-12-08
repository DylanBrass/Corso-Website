import OurPreviousProjects from "../Main Page Components/OurPreviousProjects";
import NavigationBar from "../Components/NavigationBar";
import axios from "axios";
import {useAuth} from "../security/Components/AuthProvider";


function MainPage() {
    const auth = useAuth();


    const postTest = async (event: any) => {
        event.preventDefault();
        await axios.post("http://localhost:8080/api/v1/corso/security/test", {
            "name": "test",
            "surname": "test"
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

    const adminPostTest = async (event: any) => {
        event.preventDefault();
        // @ts-ignore
        await axios.post("http://localhost:8080/api/v1/corso/security/test/admin", {
            "name": "test",
            "surname": "test"
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
            <form onSubmit={postTest}>
                <button type="submit">Post test</button>
            </form>

            <form onSubmit={adminPostTest}>
                <button type={"submit"}>Admin test</button>
            </form>
                </>
}

        </div>
    );
}

export default MainPage;
