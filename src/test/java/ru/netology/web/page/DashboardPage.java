package ru.netology.web.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;
import org.openqa.selenium.support.FindBy;
import ru.netology.web.data.DataHelper;

import java.util.Objects;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
  private SelenideElement heading = $("[data-test-id=dashboard]");
  // к сожалению, разработчики не дали нам удобного селектора, поэтому так
  private static final ElementsCollection cards = $$(".list__item div");
  private static DataHelper.CardInfo[] cardsInfo;


  public DashboardPage() {
    heading.shouldBe(visible);
    collectCards();
  }

  private static void collectCards() {
    // разработчики выдали всего 2 конкретных карты
    cardsInfo = new DataHelper.CardInfo[2];
    cardsInfo[0] = new DataHelper.CardInfo(
            cards.get(0).getAttribute("data-test-id"),
            "5559 0000 0000 0001",
            cards.get(0).$("[data-test-id=action-deposit]"));
    cardsInfo[1] = new DataHelper.CardInfo(
            cards.get(1).getAttribute("data-test-id"),
            "5559 0000 0000 0002",
            cards.get(1).$("[data-test-id=action-deposit]"));
  }

  public int getCardBalance(String id) {
    String text = null;
    for (var card : cards) {
        if (Objects.requireNonNull(card.getAttribute("data-test-id")).equals(id)) {
            return extractBalance(Objects.requireNonNull(card.text()));
        }
    }
    // такого не должно быть
    assert false;
    return -1;
  }

  private int extractBalance(String text) {
    final String balanceStart = "баланс: ";
    val start = text.indexOf(balanceStart);
    val finish = text.indexOf(" р.");
    val value = text.substring(start + balanceStart.length(), finish);
    return Integer.parseInt(value);
  }

  public String getCardIDByListIndex(int index) {
    return cardsInfo[index-1].getCardID();
  }

  public String getCardNumberByListIndex(int index) {
    return cardsInfo[index-1].getCardNumber();
  }

  public void transferMoney(int toCardIndex, int fromCardIndex, int amount) {
    // получить текущие балансы на выбранных картах
    int[] oldBalances = new int[2];
    oldBalances[0] = getCardBalance(getCardIDByListIndex(toCardIndex));
    oldBalances[1] = getCardBalance(getCardIDByListIndex(fromCardIndex));
    // нажать "Пополнить" напротив нужной карты
    depositCardAction(toCardIndex);
    // ввести данные
    $("[data-test-id=amount] input").setValue(String.valueOf(amount));
    $("[data-test-id=from] input").setValue(getCardNumberByListIndex(fromCardIndex));
    $("[data-test-id=action-transfer]").click();
    // проверка на успех перевода - смена вида формы и отсутствие ошибки
    $("[data-test-id=error-notification]").shouldBe(hidden);
    $("h1").shouldBe(visible, exactText("Ваши карты"));
    // проверить обновление баланса на выбранных картах
    checkUpdatedBalance(toCardIndex, fromCardIndex, oldBalances, amount);
  }

  private void checkUpdatedBalance(int toCardIndex, int fromCardIndex, int[] oldBalances, int amount) {
    int[] newBalances = new int[2];
    newBalances[0] = getCardBalance(getCardIDByListIndex(toCardIndex));
    newBalances[1] = getCardBalance(getCardIDByListIndex(fromCardIndex));
    // проверка изменения баланса на обоих картах на указанную в переводе сумму
    assert newBalances[0] == oldBalances[0] + amount;
    assert newBalances[1] == oldBalances[1] - amount;
    // доп. проверка "от дурака" - балансы на картах должны быть неотрицательными
    assert newBalances[toCardIndex-1] >= 0;
    assert newBalances[fromCardIndex-1] >= 0;
  }

  public void depositCardAction(int cardIndex) {
    // всего 2 карты
    assert cardIndex >= 1 && cardIndex <= 2;
    cardsInfo[cardIndex-1].getDepositButton().click();
  }
}
