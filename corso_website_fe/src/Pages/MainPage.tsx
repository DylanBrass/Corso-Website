import OurPreviousProjects from "../Main Page Components/Gallery/OurPreviousProjects";
import NavigationBar from "../Components/NavigationBar";
import BannerMainPage from "../Main Page Components/Banner/BannerMainPage";
import {useAuth} from "../security/Components/AuthProvider";
import AllServices from "../Main Page Components/AllServices";
import Footer from "../Components/Footer";
import BannerMainPageAnonymous from "../Main Page Components/Banner/BannerMainPageAnonymous";
import FaqPinnedMainPage from "../Main Page Components/FAQ/FaqPinnedMainPage";


function MainPage() {

    const auth = useAuth();

    return (
        <div>
            <NavigationBar/>
            <div id={"banner"}>
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
                <div id={"carousel"}>
                    <OurPreviousProjects/>
                </div>
                <div id={"faq"}>
                    <FaqPinnedMainPage/>
                </div>
                <AllServices />
            <Footer />
            </div>

        </div>
    );
}

export default MainPage;
