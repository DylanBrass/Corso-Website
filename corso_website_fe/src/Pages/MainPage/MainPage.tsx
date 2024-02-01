import OurPreviousProjects from "./Main Page Components/Gallery/OurPreviousProjects";
import NavigationBar from "../../Components/NavBar/NavigationBar";
import BannerMainPage from "./Main Page Components/Banner/BannerMainPage";
import {useAuth} from "../../security/Components/AuthProvider";
import BannerMainPageAnonymous from "./Main Page Components/Banner/BannerMainPageAnonymous";
import AllServices from "./Main Page Components/Services/AllServices";
import Footer from "../../Components/Footer/Footer";
import FaqPinnedMainPage from "./Main Page Components/FAQ/FaqPinnedMainPage";
import ReviewPinned from "./Main Page Components/Reviews/ReviewPinned";
import React from "react";


function MainPage() {

    const auth = useAuth();


    return (
        <div className={"remove-gutter"}>
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
                <div id={"carousel"} className={"mt-5 mb-5"}>
                    <OurPreviousProjects/>
                </div>
                <AllServices/>

                <div id={"testimonies"} className={"mt-5 mb-5"}>
                    <ReviewPinned/>
                </div>

                <div id={"faq"} className={"mt-5 mb-5"}>
                    <FaqPinnedMainPage/>
                </div>
                <Footer/>
            </div>

        </div>
    )
        ;
}

export default MainPage;
