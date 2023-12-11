import OurPreviousProjects from "../Main Page Components/OurPreviousProjects";
import NavigationBar from "../Components/NavigationBar";
import BannerMainPage from "../Main Page Components/BannerMainPage";


function MainPage() {


    return (
        <div>
            <NavigationBar />
            <BannerMainPage
            loginTitle={""}
            loginText={""}
            loginButton={() => {}}
            isLogin={false}
            defaultTitle={"Find your next favorite thing"}
            defaultText={"Discover and buy amazing things"}
            defaultButton={() => {}}
            image={"https://res.cloudinary.com/dszhbawv7/image/upload/v1702332049/banner_cqmadc.png"}
            />
            <OurPreviousProjects />
        </div>
    );
}

export default MainPage;
