import "./FaqTileFaqPage.css"
import {useState} from "react";
import expand from "../../ressources/images/expand-plus.svg"
import expanded from "../../ressources/images/expanded-minus.svg"
import React from "react";
import {useTranslation} from "react-i18next";


class FAQ {
    id: String
    question: String;
    answer: String;


    constructor(id: String, question: String, answer: String) {
        this.id = id
        this.question = question;
        this.answer = answer;
    }
}


function FaqTileFaqPage(faq: FAQ) {

    const [isExpanded, setExpandedState] = useState(false)
    const [t] = useTranslation()

    return (
        <div className={"tile-box mt-5"}>
            <div className={"container w-75 border-bottom"}>
                <div className={"row mb-5"}>
                    <div className={"col-6 text-start"}>
                        <span className={"question"}>
                            {faq.question}
                        </span>
                    </div>
                    <div className={"col-6 text-end"}>
                <span className={"faq-btn"} onClick={() => {
                    setExpandedState(!isExpanded)
                }}>
                    {
                        isExpanded ?
                            <img src={expanded} alt={"Click to hide"} />
                            : <img src={expand} alt={"Click to expand"}/>
                    }
                </span>
                    </div>
                </div>
                {isExpanded &&
                    <div className={"row " + (isExpanded ? 'show' : '')}>
                        <div className={"col-1 answer-box d-none d-lg-block"}>
                            <img alt={"Lamp Icon"}
                                 src={"https://res.cloudinary.com/dszhbawv7/image/upload/v1702412228/Lamp_light_qmpkdi.svg"}/>
                        </div>
                        <div className={"col-11 mb-3 answer-box"}>
                            <p style={{
                                textAlign: "justify",
                                textJustify: "inter-word"
                            }}>
                                <strong>{t("answer")}: </strong>{faq.answer}
                            </p>
                        </div>
                    </div>
                }
            </div>
        </div>
    )
}

export {FaqTileFaqPage, FAQ}