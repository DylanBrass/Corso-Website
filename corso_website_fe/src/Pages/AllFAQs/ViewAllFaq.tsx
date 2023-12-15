import NavigationBar from "../../Components/NavigationBar";
import {useEffect, useState} from "react";
import axios from "axios";
import {FAQ, FaqTileFaqPage} from "./FaqTileFaqPage";
import {useAuth} from "../../security/Components/AuthProvider";
import "./ViewAllFaq.css"
function ViewAllFaq() {
    const auth = useAuth()

    const [faqs, setFaqs] = useState([FAQ])

    const getFaqs = () => {
        axios.get("http://localhost:8080/api/v1/corso/faqs",
            {
                headers: {
                    // @ts-ignore
                    'X-XSRF-TOKEN': auth.getXsrfToken()
                }
            })
            .then(r => {
                setFaqs(r.data)
            })
            .catch(e => {
                console.log(e)
            })
    }

    useEffect(() => {
        getFaqs()

    }, []);

    return (

        <div>
            <NavigationBar/>
            <div className={"text-center"}>
                <div className={"row"}>
                    <span className={"faq-title "}>Faq</span>
                </div>
                <div className={"text-center text-md-end me-0 me-md-5"}>
                    <a href={"/#faq"}>
                        Back To Main Page
                    </a>
                </div>
                <div className={"tiles-container"}>
                    {faqs.map((value) => (
                        <FaqTileFaqPage
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
                </div>

            </div>
        </div>
    )

}

export default ViewAllFaq