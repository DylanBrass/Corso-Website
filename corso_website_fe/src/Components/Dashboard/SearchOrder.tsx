import React from "react";
import axios from "axios";
import {useTranslation} from "react-i18next";

export default function SearchOrder() {

    const {t} = useTranslation();

    const searchOrder = (event: any) => {
        event.preventDefault();

        let orderNumber = event.target.orderNumber.value;

        axios.get(process.env.REACT_APP_BE_HOST + `api/v1/corso/orders/manage/${orderNumber}`).then(
            res => {
                if (res.status === 200) {
                    if (res.data.orderStatus.toLowerCase() === "pending") {
                        window.location.href = "/manage/orders/" + res.data.orderId
                    } else if (res.data.orderStatus.toLowerCase() === "in_progress") {
                        window.location.href = "/manage/currentOrder/" + res.data.orderId
                    } else if (res.data.orderStatus.toLowerCase() === "declined"
                        || res.data.orderStatus.toLowerCase() === "cancelled"
                        || res.data.orderStatus.toLowerCase() === "completed"
                    ) {
                        window.location.href = "/manage/orders/inactive/" + res.data.orderId
                    }
                }
            }
        ).catch(err => {
            if (err.response.status === 404) {
                // @ts-ignore
                swal(t("orderNotFound"), "", "error");
            }
        }
        )

    }



    return (
        <div>
            <form onSubmit={searchOrder}>
                <div className="form-group">
                    <label htmlFor="orderNumber">
                        <input type="text" className="form-control" id="orderNumber"
                               placeholder={t("enterOrderNumber")}/>
                    </label>
                    <button type="submit" className="mx-3 btn-style">{t("search")}</button>
                </div>
            </form>

        </div>
    )
}