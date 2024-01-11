import "./BannerMain.css";


// @ts-ignore
function BannerMainPageAnonymous({image}) {


    return (
        <div className={"banner-box"} style={{
            backgroundImage: `url(${image})`
        }}>
            <div className={"row row-banner"}>
                <div className={"row d-none d-lg-flex"}>
                    <div className={"col-12 text-center d-none d-lg-block"}>
                        <h1 className={"mt-5 anonymous-banner-title"}>Register today to have us as
                            your trusted electrician</h1>
                    </div>
                </div>

                <div className={"col-12 text-center d-block d-lg-none"}>
                        <h1 className={"mt-5"}>Register Now!</h1>
                    </div>
                <div className={"row"}>
                    <div className={"col-12 text-center"}>

                        <button className={"banner-btn  mt-5 mb-5"}
                                onClick={() => {
                                    window.location.href = process.env.REACT_APP_BE_HOST+"oauth2/authorization/okta"
                                }}>
                            Register Now!
                        </button>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default BannerMainPageAnonymous;