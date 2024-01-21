import React from 'react';
import "./CurrentOrdersProgress.css"
import {ProgressBar, Step} from "react-step-progress-bar";
import check from "../../../images/check.svg";
import Order from "../../../Models/Order";

function CurrentOrdersProgress({order}: { order: Order}) {
    return(
        <div className={"p-4 w-75 m-auto order-box"} onClick={()=>{
            window.location.href = `/orders/${order.orderId}`
        }}>
            <h4 className={`${order.estimatedDuration > 0 ? "mb-3" : "mb-0"}`}>Order #{order.orderTrackingNumber}</h4>

            {order.estimatedDuration > 0 &&

                <ProgressBar
                hasStepZero={true}
                percent={(order.estimatedDuration - order.daysLeft) / order.daysLeft * 100}
                filledBackground="#47B2FF"
                height={3}
            >


                {
                    Array.from({length: order.estimatedDuration + 1}, (_, index) => (
                        <Step transition="scale" key={index}>
                            {({accomplished, position}) => (
                                position !== 0 ?
                                    <div
                                        className={`w-100 progress-bar-order-step indexedStep ${accomplished ? "accomplished" : null}`}
                                    >
                                        {
                                            accomplished ?
                                                <img src={check} alt={"accomplished"}/>
                                                :
                                                <span className={`step-num rounded-circle p-1`}>{index}</span>
                                        }
                                    </div> :
                                    <div
                                        className={`start-step`}
                                    >
                                    </div>

                            )}


                        </Step>
                    ))
                }

            </ProgressBar>
            }

            {order.estimatedDuration <= 0 &&
                <div className={"w-100 text-center mb-0 p-0"}>
                    No Timeline
                </div>
            }
        </div>
    )
}

export default CurrentOrdersProgress;
