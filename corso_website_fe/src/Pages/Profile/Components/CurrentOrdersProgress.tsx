import React from 'react';
import "./CurrentOrdersProgress.css"
import {ProgressBar, Step} from "react-step-progress-bar";
import check from "../../../ressources/images/check_done.png";
import Order from "../../../ressources/Models/Order";
import checkToDo from "../../../ressources/images/check_notDone.png";
import {useTranslation} from "react-i18next";

function CurrentOrdersProgress({order}: { order: Order}) {
    const [t] = useTranslation();
    return(
        <div className={"p-4 w-75 m-auto order-box"} onClick={()=>{
            window.location.href = `/orders/${order.orderId}`
        }}>
            <h4 className={`${order.estimatedDuration > 0 ? "mb-3" : "mb-0"}`}>{t("order.order")} #{order.orderTrackingNumber}</h4>

            {order.estimatedDuration > 0 &&

                <ProgressBar
                    hasStepZero={true}
                    percent={order.hoursWorked / order.estimatedDuration * 100}
                    filledBackground="#47B2FF"
                >

                    {Array.from({length: 4}, (_, index) => (
                        <Step transition="scale" key={index}>
                            {({accomplished, position}) => (
                                <div
                                    className={`${accomplished ? "accomplished" : null}`}>
                                    {accomplished ? (
                                        <img src={check} alt={"accomplished"}/>
                                    ) : (
                                        <img src={checkToDo} alt={"to be done"}/>
                                    )}
                                </div>

                            )}
                        </Step>
                    ))
                    }

                </ProgressBar>
            }

            {order.estimatedDuration <= 0 &&
                <div className={"w-100 text-center mb-0 p-0"}>
                    {t("order.noTimeline")}
                </div>
            }
        </div>
    )
}

export default CurrentOrdersProgress;
