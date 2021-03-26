package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.github.javafaker.DateAndTime;
import com.github.javafaker.Faker;
import jdk.jfr.DataAmount;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatter.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static java.time.Duration.*;
import static java.time.format.DateTimeFormatter.*;

public class PlanMeetingTest {
    private Faker faker;
    private RegCardInfo regCardInfo;


    @BeforeEach
    void setUpAll() {
        faker = new Faker(new Locale("ru"));

        regCardInfo = new RegCardInfo();

        regCardInfo.name = faker.name().fullName();
        regCardInfo.phone = faker.phoneNumber().phoneNumber();
        regCardInfo.city = faker.address().cityName();

        regCardInfo.date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(regCardInfo.date);
        c.add(Calendar.DATE, 1);
        regCardInfo.date1 = c.getTime();
    }

    @Data
    public class RegCardInfo{
        private String name;
        private String phone;
        private String city;
        private Date date;
        private Date date1;
    }

    @Test
    void shouldPlanMeeting() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        open("http://localhost:9999");
        SelenideElement form = $(".form");
        form.$("[data-test-id=city] .input__control").setValue(regCardInfo.city);
        $$("[data-test-id=date] .input__control").last().setValue(dateFormat.format(regCardInfo.date));
        form.$("[data-test-id=name] .input__control").setValue(regCardInfo.name);
        form.$("[data-test-id=phone] .input__control").setValue(regCardInfo.phone);
        form.$("[data-test-id=agreement]").click();
        $(byText("Запланировать")).click();
        form.$("[data-test-id=date] .input__control").setValue(dateFormat.format(regCardInfo.date1));
        $(byText("Запланировать")).click();
        $(byText("Перепланировать")).click();
        $(byText("Успешно!")).shouldBe(visible);

        $("[data-test-id=success-notification] .notification__title").shouldHave(exactText(
                "Успешно!"));
    }
}
