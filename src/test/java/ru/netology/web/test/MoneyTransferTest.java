package ru.netology.web.test;

import org.junit.jupiter.api.*;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.DepositPage;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.netology.web.page.DashboardPage.getCardBalance;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MoneyTransferTest {
    @Test
    @DisplayName("should successfully transfer legal amount of money from card 2 to card 1")
    @Order(0)
    void transferMoneyWithLegalAmountAndLegalTo1From2() {
        open("http://localhost:9999");
        var dashboardPage = doAuth();
        int toCardIndex = 0;
        int fromCardIndex = 1;
        DataHelper.CardInfo[] cardsInfo = DataHelper.getCardsInfo();
        int amount = 1000;
        int[] oldBalances = getBalances(cardsInfo);
        // нажать "Пополнить" напротив нужной карты
        DepositPage depositPage = dashboardPage.depositCardAction(cardsInfo[toCardIndex].getCardID());
        depositPage.transferMoney(cardsInfo[fromCardIndex].getCardNumber(), amount);
        depositPage.checkNoErrorMessage();
        dashboardPage.checkIfActive();
        // проверить обновление баланса на выбранных картах
        int[] expected = new int[] {
                oldBalances[0] + amount,
                oldBalances[1] - amount
        };
        int[] actual = getBalances(cardsInfo);
        // проверка изменения баланса на обоих картах на указанную в переводе сумму
        assertEquals(expected[0], actual[0]);
        assertEquals(expected[1], actual[1]);
        assertBalancesArePositive(cardsInfo);
    }

    @Test
    @DisplayName("should successfully transfer legal amount of money from card 1 to card 2")
    @Order(1)
    void transferMoneyWithLegalAmountAndLegalTo2From1() {
        open("http://localhost:9999");
        var dashboardPage = doAuth();
        int toCardIndex = 1;
        int fromCardIndex = 0;
        DataHelper.CardInfo[] cardsInfo = DataHelper.getCardsInfo();
        int amount = 1000;
        int[] oldBalances = getBalances(cardsInfo);
        // нажать "Пополнить" напротив нужной карты
        DepositPage depositPage = dashboardPage.depositCardAction(cardsInfo[toCardIndex].getCardID());
        depositPage.transferMoney(cardsInfo[fromCardIndex].getCardNumber(), amount);
        depositPage.checkNoErrorMessage();
        dashboardPage.checkIfActive();
        // проверить обновление баланса на выбранных картах
        int[] expected = new int[] {
                oldBalances[0] - amount,
                oldBalances[1] + amount
        };
        int[] actual = getBalances(cardsInfo);
        // проверка изменения баланса на обоих картах на указанную в переводе сумму
        assertEquals(expected[0], actual[0]);
        assertEquals(expected[1], actual[1]);
        assertBalancesArePositive(cardsInfo);
    }

    @Test
    @DisplayName("should successfully transfer legal amount of money from card 1 to card 1")
    @Order(2)
    void transferMoneyWithLegalAmountAndLegalTo1From1() {
        open("http://localhost:9999");
        var dashboardPage = doAuth();
        int toCardIndex = 0;
        int fromCardIndex = 0;
        DataHelper.CardInfo[] cardsInfo = DataHelper.getCardsInfo();
        int amount = 1000;
        int[] oldBalances = getBalances(cardsInfo);
        // нажать "Пополнить" напротив нужной карты
        DepositPage depositPage = dashboardPage.depositCardAction(cardsInfo[toCardIndex].getCardID());
        depositPage.transferMoney(cardsInfo[fromCardIndex].getCardNumber(), amount);
        depositPage.checkNoErrorMessage();
        dashboardPage.checkIfActive();
        // проверить обновление баланса на выбранных картах
        int[] expected = new int[] {
                oldBalances[0],
                oldBalances[1]
        };
        int[] actual = getBalances(cardsInfo);
        // проверка отсутствия изменения баланса на обоих картах
        assertEquals(expected[0], actual[0]);
        assertEquals(expected[1], actual[1]);
        assertBalancesArePositive(cardsInfo);
    }

    @Test
    @DisplayName("should FAIL with transfer ILLEGAL amount of money from card 2 to card 1")
    @Order(3)
    void transferMoneyWithIllegalAmountAndLegalTo1From2() {
        open("http://localhost:9999");
        var dashboardPage = doAuth();
        int toCardIndex = 0;
        int fromCardIndex = 1;
        DataHelper.CardInfo[] cardsInfo = DataHelper.getCardsInfo();
        int amount = 10001;
        int[] oldBalances = getBalances(cardsInfo);
        // нажать "Пополнить" напротив нужной карты
        DepositPage depositPage = dashboardPage.depositCardAction(cardsInfo[toCardIndex].getCardID());
        depositPage.transferMoney(cardsInfo[fromCardIndex].getCardNumber(), amount);
        depositPage.checkNoErrorMessage();
        dashboardPage.checkIfActive();
        // проверить обновление баланса на выбранных картах
        int[] expected = new int[] {
                oldBalances[0] + amount,
                oldBalances[1] - amount
        };
        int[] actual = getBalances(cardsInfo);
        // проверка изменения баланса на обоих картах на указанную в переводе сумму
        assertEquals(expected[0], actual[0]);
        assertEquals(expected[1], actual[1]);
        assertBalancesArePositive(cardsInfo);
    }

    void assertBalancesArePositive(DataHelper.CardInfo[] cardsInfo) {
        int[] newBalances = getBalances(cardsInfo);
        // доп. проверка "от дурака" - балансы на картах должны быть неотрицательными
        assertTrue(newBalances[0] >= 0);
        assertTrue(newBalances[1] >= 0);
    }

    private DashboardPage doAuth() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        return verificationPage.validVerify(verificationCode);
    }

    private int[] getBalances(DataHelper.CardInfo[] cardsInfo) {
        return new int[]{
                getCardBalance(cardsInfo[0].getCardID()),
                getCardBalance(cardsInfo[1].getCardID())
        };
    }
}

