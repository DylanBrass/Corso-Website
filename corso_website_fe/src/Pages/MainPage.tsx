import OurPreviousProjects from "../Main Page Components/OurPreviousProjects";
import NavigationBar from "../Components/NavigationBar";
import BannerMainPage from "../Main Page Components/BannerMainPage";
import {useAuth} from "../security/Components/AuthProvider";
import BannerMainPageAnonymous from "../Main Page Components/BannerMainPageAnonymous";
import AllServices from "../Main Page Components/AllServices";


function MainPage() {

    const auth = useAuth();

    return (
        <div>
            <NavigationBar />
            {
                // @ts-ignore
                auth.isAuthenticated ?
            <BannerMainPage
                image={"https://res.cloudinary.com/dszhbawv7/image/upload/v1702345020/banner-img-2_bgodwz.png"}
            />
                :
                <BannerMainPageAnonymous
                    image={"https://res.cloudinary.com/dszhbawv7/image/upload/v1702332049/banner_cqmadc.png"}/>
            }
            <OurPreviousProjects />
            <AllServices />
        </div>
    );
}

export default MainPage;
