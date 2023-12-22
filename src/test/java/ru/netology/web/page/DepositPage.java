package ru.netology.web.page;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class DepositPage {
    public void transferMoney(String fromCardNumber, int amount) {
        // ввести данные
        $("[data-test-id=amount] input").setValue(String.valueOf(amount));
        $("[data-test-id=from] input").setValue(fromCardNumber);
        // нажать кнопку
        $("[data-test-id=action-transfer]").click();
    }

    public void checkNoErrorMessage() {
        // проверка на успех перевода - отсутствие ошибки на текущей форме
        $("[data-test-id=error-notification]").shouldBe(hidden);
    }
}
