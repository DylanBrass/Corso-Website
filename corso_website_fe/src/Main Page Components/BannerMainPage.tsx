class Banner{
    image: string;
    defaultTitle: string;
    defaultText: string;
    defaultButton: () => void;
    loginTitle: string;
    loginText: string;
    loginButton: () => void;
    isLogin: boolean;
    constructor(isLogin: boolean,image: string, defaultTitle: string, defaultText: string, defaultButton: () => void, loginTitle: string, loginText: string, loginButton: () => void) {
        this.image = image;
        this.defaultTitle = defaultTitle;
        this.defaultText = defaultText;
        this.defaultButton = defaultButton;
        this.loginTitle = loginTitle;
        this.loginText = loginText;
        this.loginButton = loginButton;
        this.isLogin = isLogin;
    }

}

function BannerMainPage(banner:Banner){
    return(
        <div style={{
            width: "100%",
            backgroundImage: "url('https://res.cloudinary.com/dszhbawv7/image/upload/v1702332049/banner_cqmadc.png')",
            backgroundRepeat: "no-repeat",
            backgroundSize: "cover",
            backgroundPosition: "center",
            height: "500px",
        }}>
            <div>
                <h1>{banner.defaultTitle}</h1>
                <p>{banner.defaultText}</p>
                <button onClick={banner.defaultButton}>Default Button</button>
            </div>
        </div>
    )
}

export default BannerMainPage;