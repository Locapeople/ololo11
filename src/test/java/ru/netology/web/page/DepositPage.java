package ru.netology.web.page;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static ru.netology.web.page.DashboardPage.*;

public class DepositPage {
    public void transferMoney(int fromCardIndex, int amount) {
        // ввести данные
        $("[data-test-id=amount] input").setValue(String.valueOf(amount));
        $("[data-test-id=from] input").setValue(getCardNumberByListIndex(fromCardIndex));
        $("[data-test-id=action-transfer]").click();
        // проверка на успех перевода - смена вида формы и отсутствие ошибки
        $("[data-test-id=error-notification]").shouldBe(hidden);
        $("h1").shouldBe(visible, exactText("Ваши карты"));
    }
}
