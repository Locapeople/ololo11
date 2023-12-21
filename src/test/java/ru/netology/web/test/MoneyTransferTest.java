package ru.netology.web.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;

class MoneyTransferTest {
    @Test
    @DisplayName("should successfully transfer legal amount of money from card 2 to card 1")
    void transferMoneyWithLegalAmountAndLegalTo1From2() {
      open("http://localhost:9999");
      var dashboardPage = doAuth();
      dashboardPage.transferMoney(1, 2, 1000);
    }

    @Test
    @DisplayName("should successfully transfer legal amount of money from card 1 to card 2")
    void transferMoneyWithLegalAmountAndLegalTo2From1() {
        open("http://localhost:9999");
        var dashboardPage = doAuth();
        dashboardPage.transferMoney(2, 1, 1000);
    }

    @Test
    @DisplayName("should successfully transfer legal amount of money from card 1 to card 1")
    void transferMoneyWithLegalAmountAndLegalTo1From1() {
        open("http://localhost:9999");
        var dashboardPage = doAuth();
        dashboardPage.transferMoney(1, 1, 6666);
    }

    private DashboardPage doAuth() {
      var loginPage = new LoginPage();
      var authInfo = DataHelper.getAuthInfo();
      var verificationPage = loginPage.validLogin(authInfo);
      var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
      return verificationPage.validVerify(verificationCode);
    }
}

