package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import lombok.val;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
  private SelenideElement heading = $("[data-test-id=dashboard]");
  // к сожалению, разработчики не дали нам удобного селектора, поэтому так
  @Getter
  private static final ElementsCollection cards = $$(".list__item div");

  public DashboardPage() {
    heading.shouldBe(visible);
  }

  public static int getCardBalance(String id) {
    var card  = cards.findBy(Condition.attribute("data-test-id", id));
    return extractBalance(card.text());
  }

  private static int extractBalance(String text) {
    final String balanceStart = "баланс: ";
    val start = text.indexOf(balanceStart);
    val finish = text.indexOf(" р.");
    val value = text.substring(start + balanceStart.length(), finish);
    return Integer.parseInt(value);
  }

  public DepositPage depositCardAction(String id) {
    var card = cards.findBy(Condition.attribute("data-test-id", id));
    card.$("[data-test-id=action-deposit]").click();
    return new DepositPage();
  }

  public void checkIfActive() {
    $("h1").shouldBe(visible, exactText("Ваши карты"));
  }
}
