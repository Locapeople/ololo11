package ru.netology.web.data;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.Value;

public class DataHelper {
  private DataHelper() {}

  @Value
  public static class AuthInfo {
    private String login;
    private String password;
  }

  public static AuthInfo getAuthInfo() {
    return new AuthInfo("vasya", "qwerty123");
  }

  @Value
  public static class VerificationCode {
    private String code;
  }

  public static VerificationCode getVerificationCodeFor(AuthInfo authInfo) {
    return new VerificationCode("12345");
  }

  @Value
  public static class CardInfo {
    private String cardID;
    private String cardNumber;
    private SelenideElement depositButton;
  }

  public static CardInfo[] getCardsInfo(ElementsCollection cards) {
    CardInfo[] cardsInfo = new CardInfo[2];
    cardsInfo[0] = new CardInfo(
            cards.get(0).getAttribute("data-test-id"),
            "5559 0000 0000 0001",
            cards.get(0).$("[data-test-id=action-deposit]"));
    cardsInfo[1] = new CardInfo(
            cards.get(1).getAttribute("data-test-id"),
            "5559 0000 0000 0002",
            cards.get(1).$("[data-test-id=action-deposit]"));
    return cardsInfo;
  }
}
