import OurPreviousProjects from "../Main Page Components/OurPreviousProjects";
import NavigationBar from "../Components/NavigationBar";

function MainPage() {



    const fetchTest = async (event: any) => {
        event.preventDefault();
        const response = await fetch("http://localhost:8080/api/v1/corso/galleries");
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
        </div>
    );
}

export default MainPage;
