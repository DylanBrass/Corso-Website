import React from "react";
import FAQ from "../../../../ressources/Models/FAQ";
import {useTranslation} from "react-i18next";
import "./FaqTile.css"
function FaqTileMainPage(faq: FAQ) {

    const {t} = useTranslation();

    return (
        <div className={"faq-tile-box faq-mainPage-container"}>

            <div className={"row  ms-lg-4"}>
                <div className={"col-10"}>
                    <div className={"row"}>
                        <h4>
                            {faq.question}
                        </h4>
                        <div className={"col-11 answer-box"}>
                            <p className={""}>
                                <span className={"faq-answer-prefix"}>{t("answer")}: </span>{faq.answer}
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )

}


export {FaqTileMainPage, FAQ}