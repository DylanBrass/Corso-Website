import React from "react";
import { Image, Page, Text } from "@react-pdf/renderer";
import logo from "../../../../../ressources/images/logo.png";
import { useTranslation } from "react-i18next";

export default function PresentationPage() {
    const { t, i18n } = useTranslation();
    const locale = i18n.language === "fr" ? "fr-FR" : "en-US";

    return (
        <Page size="A4" style={{ backgroundColor: '#f0f0f0' }}>

            <Image src={logo} style={{ width: 500, marginVertical: 10, marginHorizontal: 45 }} />
            <Text style={{ textAlign: "center", fontSize: 28, fontWeight: 'bold', marginVertical: 20, color: '#333' }}>
                {t("reports.pdfPages.insightsReport")}
            </Text>
            <Text style={{ textAlign: "center", fontSize: 28, fontWeight: 'bold', color: '#333' }}>
                Corso Electrique Inc.
            </Text>

            <Text style={{ textAlign: "center", fontSize: 14, marginVertical: 20, color: '#666' }}>
                {t("reports.pdfPages.reportsGeneratedBy")}
            </Text>
            <Text style={{ textAlign: "center", fontSize: 16, marginVertical: 20, color: '#444', padding: 50 }}>
                {t("reports.pdfPages.reportHighlights")}
            </Text>
            <Text style={{textAlign: "center", fontSize: 14, marginVertical: 20, color: '#666'}}>
                {t("reports.pdfPages.producedOn")} {new Date().toLocaleDateString(locale, {year: 'numeric', month: 'long', day: 'numeric'})}.
            </Text>
        </Page>

    );

}
