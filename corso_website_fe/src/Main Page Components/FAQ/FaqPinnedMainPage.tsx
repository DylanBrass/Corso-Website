import "./FaqPinned.css"
import {useEffect, useState} from "react";
import axios from "axios";
import {FAQ, FaqTileMainPage} from "./FaqTileMainPage";
import {useAuth} from "../../security/Components/AuthProvider";

function FaqPinnedMainPage() {

    const auth = useAuth()

    const [preferredFaq, setPreferredFaq] = useState([FAQ])
    const getPreferredFaqs = () => {
        axios.get(process.env.REACT_APP_BE_HOST+"api/v1/corso/faqs/preferred",
            {
                headers: {
                    // @ts-ignore
                    'X-XSRF-TOKEN': auth.getXsrfToken()
                }
            })
            .then(r => {
                setPreferredFaq(r.data)
            })
            .catch(e => {
                
            })
    }


    useEffect(() => {
        getPreferredFaqs()
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    return (
        <div className={"faq-container"}>

            <div className={"faq-main-box"}>
                <div className={"faq-title-box d-none d-md-flex"}>
                    <img alt={"three dots"} src={"https://res.cloudinary.com/dszhbawv7/image/upload/v1702413426/Meatballs_menu_bcolze.svg\n"}/>
                    <span className={"faq-title text-sm-center m-sm-0"}>
                        FAQ
                    </span>
                </div>
                <div className={"text-center d-block d-md-none"}>
                    <span className={"faq-title"}>
                        FAQ
                    </span>
                </div>
                {preferredFaq.map((value) => (
                    <FaqTileMainPage
                        //@ts-ignore
                        key={value.id}
                        //@ts-ignore
                        id={value.id}
                        //@ts-ignore
                        question={value.question}
                        //@ts-ignore
                        answer={value.answer}
                    />
                ))}
                <div className={"link-all-faqs"}>
                    <a href={"/faq"}>View More FAQs</a>
                </div>
            </div>

        </div>
    )
}

export default FaqPinnedMainPage