package ru.netology.web.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;
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
    // разработчики выдали всего 2 конкретных карты
    cardsInfo = DataHelper.getCardsInfo(cards);
  }


  public static int getCardBalance(String id) {
    for (var card : cards) {
        if (Objects.requireNonNull(card.getAttribute("data-test-id")).equals(id)) {
            return extractBalance(Objects.requireNonNull(card.text()));
        }
    }
    // такого не должно быть
    assert false;
    return -1;
  }

  private static int extractBalance(String text) {
    final String balanceStart = "баланс: ";
    val start = text.indexOf(balanceStart);
    val finish = text.indexOf(" р.");
    val value = text.substring(start + balanceStart.length(), finish);
    return Integer.parseInt(value);
  }

  public static String getCardIDByListIndex(int index) {
    return cardsInfo[index-1].getCardID();
  }

  public static String getCardNumberByListIndex(int index) {
    return cardsInfo[index-1].getCardNumber();
  }

  public DepositPage depositCardAction(int cardIndex) {
    // всего 2 карты
    assert cardIndex >= 1 && cardIndex <= 2;
    cardsInfo[cardIndex-1].getDepositButton().click();
    return new DepositPage();
  }
}
