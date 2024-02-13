package com.corso.springboot.selenium_tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.springframework.boot.test.context.SpringBootTest;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

@SpringBootTest
public class LocalizationTest {

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        open("http://localhost:3000/");
    }

    @Test
    public void localizationTest() {
        if($(By.linkText("ENGLISH")).isDisplayed()) {
            $(By.linkText("ENGLISH")).click();
            $("h1").shouldHave(text("WELCOME TO CORSO"));
            $("p").shouldHave(text("Register today to have us as your trusted electrician"));
            $$("h1").get(1).shouldHave(text("OUR PREVIOUS PROJECTS"));
            $$("p").get(1).shouldHave(text("We specialize in diverse electrical projects, from smart home installations to modernizing historic buildings, with a focus on sustainable energy solutions like solar panels. Our passion is ensuring safety, efficiency, and sustainability in our ever-evolving world."));
            $$("h1").get(2).shouldHave(text("ALL SERVICES"));
            $("p.subtitle-services").shouldHave(text("We offer a wide range of electrical services, from simple repairs to complex installations. Our team of experts is dedicated to providing the best solutions for your electrical needs. We are committed to delivering high-quality work and exceptional customer service. Our goal is to exceed your expectations and ensure your satisfaction."));
            $(By.linkText("LOGIN")).click();
            $("#username").setValue("Admin");
            $("[name='action']").click();
            $("#password").setValue("Password!!").pressEnter();
            $(By.linkText("DASHBOARD")).click();
            $("p").shouldHave(text("Completed Orders"));
            $("p").click();
            sleep(5000);
            $("h1").shouldHave(text("COMPLETED ORDERS"));
            $("p").shouldHave(text("ORDER NUMBER"));
        }
        else {
            $(By.linkText("FRENCH (FRANÇAIS)")).click();
            $("h1").shouldHave(text("BIENVENUE À CORSO"));
            $("p").shouldHave(text("Inscrivez-vous aujourd'hui pour nous avoir comme électricien de confiance"));
            $$("h1").get(1).shouldHave(text("PROJETS RÉALISÉS"));
            $$("p").get(1).shouldHave(text("Nous sommes spécialisés dans divers projets électriques, des installations domestiques intelligentes à la modernisation de bâtiments historiques, en mettant l'accent sur les solutions énergétiques durables telles que les panneaux solaires. Notre passion est d'assurer sécurité, efficacité et durabilité."));
            $$("h1").get(2).shouldHave(text("NOS SERVICES"));
            $("p.subtitle-services").shouldHave(text("Nous proposons une large gamme de services électriques, allant de simples réparations à des installations complexes. Notre équipe d'experts se consacre à fournir les meilleures solutions pour vos besoins en électricité. Nous nous engageons à fournir un travail de haute qualité et un service client exceptionnel. Notre objectif est de dépasser vos attentes et d'assurer votre satisfaction."));
            $(By.linkText("CONNEXION")).click();
            $("#username").setValue("Admin");
            $("[name='action']").click();
            $("#password").setValue("Password!!").pressEnter();
            $(By.linkText("TABLEAU DE BORD")).click();
            $("p").shouldHave(text("Commandes terminées"));
            $("p").click();
            sleep(5000);
            $("h1").shouldHave(text("COMMANDES TERMINÉES"));
            $("p").shouldHave(text("NUMÉRO DE COMMANDE"));
        }
    }
}
