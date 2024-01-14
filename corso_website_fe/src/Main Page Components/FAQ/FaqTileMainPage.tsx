import FAQ from "../../Models/FAQ";
function FaqTileMainPage(faq: FAQ) {

    return (
        <div className={"faq-tile-box"}>

            <div className={"row"}>
                <div className={"col-1"}/>
                <div className={"col-10"}>
                    <div className={"row"}>
                        <h4>
                            {faq.question}
                        </h4>
                        <div className={"col-1 answer-box d-none d-lg-block"}>
                            <img alt={"Lamp Icon"}
                                 src={"https://res.cloudinary.com/dszhbawv7/image/upload/v1702412228/Lamp_light_qmpkdi.svg"}/>
                        </div>
                        <div className={"col-11 answer-box"}>
                            <p className={""}>
                                <strong>Answer: </strong>{faq.answer}
                            </p>
                        </div>

                    </div>
                </div>
                <div className={"col-1"}/>

            </div>
        </div>
    )

}


export {FaqTileMainPage, FAQ}