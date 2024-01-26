import {initReactI18next} from "react-i18next";
import i18next from "i18next";

import english from "./Translation/English/translation.json"

import french from "./Translation/French/translation.json"


const resources = {
    en: {
        translation: english
    },
    fr: {
        translation: french
    }
}


i18next.use(initReactI18next).init({
    resources,
    lng: "en"
})

export default i18next;
