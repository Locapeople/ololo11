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
import static ru.netology.web.page.DashboardPage.getCardIDByListIndex;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MoneyTransferTest {
    @Test
    @DisplayName("should successfully transfer legal amount of money from card 2 to card 1")
    @Order(0)
    void transferMoneyWithLegalAmountAndLegalTo1From2() {
        open("http://localhost:9999");
        var dashboardPage = doAuth();
        int toCardIndex = 1;
        int fromCardIndex = 2;
        int amount = 1000;
        int[] oldBalances = getBalances(toCardIndex, fromCardIndex);
        // нажать "Пополнить" напротив нужной карты
        DepositPage depositPage = dashboardPage.depositCardAction(toCardIndex);
        depositPage.transferMoney(fromCardIndex, amount);
        // проверить обновление баланса на выбранных картах
        int[] expected = new int[]{
                oldBalances[0] + amount,
                oldBalances[1] - amount
        };
        int[] actual = getBalances(toCardIndex, fromCardIndex);
        // проверка изменения баланса на обоих картах на указанную в переводе сумму
        assertEquals(expected[0], actual[0]);
        assertEquals(expected[1], actual[1]);
    }

    @Test
    @DisplayName("should successfully transfer legal amount of money from card 1 to card 2")
    @Order(1)
    void transferMoneyWithLegalAmountAndLegalTo2From1() {
        open("http://localhost:9999");
        var dashboardPage = doAuth();
        int toCardIndex = 2;
        int fromCardIndex = 1;
        int amount = 1000;
        int[] oldBalances = getBalances(toCardIndex, fromCardIndex);
        // нажать "Пополнить" напротив нужной карты
        DepositPage depositPage = dashboardPage.depositCardAction(toCardIndex);
        depositPage.transferMoney(fromCardIndex, amount);
        // проверить обновление баланса на выбранных картах
        int[] expected = new int[]{
                oldBalances[0] + amount,
                oldBalances[1] - amount
        };
        int[] actual = getBalances(toCardIndex, fromCardIndex);
        // проверка изменения баланса на обоих картах на указанную в переводе сумму
        assertEquals(expected[0], actual[0]);
        assertEquals(expected[1], actual[1]);
    }

    @Test
    @DisplayName("should successfully transfer legal amount of money from card 1 to card 1")
    @Order(2)
    void transferMoneyWithLegalAmountAndLegalTo1From1() {
        open("http://localhost:9999");
        var dashboardPage = doAuth();
        int toCardIndex = 1;
        int fromCardIndex = 1;
        int amount = 6666;
        int[] oldBalances = getBalances(toCardIndex, fromCardIndex);
        // нажать "Пополнить" напротив нужной карты
        DepositPage depositPage = dashboardPage.depositCardAction(toCardIndex);
        depositPage.transferMoney(fromCardIndex, amount);
        // проверить обновление баланса
        int[] expected = new int[]{
                oldBalances[0],
                oldBalances[1]
        };
        int[] actual = getBalances(toCardIndex, fromCardIndex);
        // проверка изменения баланса на обоих картах на указанную в переводе сумму
        assertEquals(expected[0], actual[0]);
        assertEquals(expected[1], actual[1]);
    }

    @Test
    @DisplayName("should FAIL with transfer ILLEGAL amount of money from card 2 to card 1")
    @Order(3)
    void transferMoneyWithIllegalAmountAndLegalTo1From2() {
        open("http://localhost:9999");
        var dashboardPage = doAuth();
        int toCardIndex = 1;
        int fromCardIndex = 2;
        int amount = 10001;
        int[] oldBalances = getBalances(toCardIndex, fromCardIndex);
        // нажать "Пополнить" напротив нужной карты
        DepositPage depositPage = dashboardPage.depositCardAction(toCardIndex);
        depositPage.transferMoney(fromCardIndex, amount);
        // проверить обновление баланса на выбранных картах
        int[] expected = new int[]{
                oldBalances[0] + amount,
                oldBalances[1] - amount
        };
        int[] actual = getBalances(toCardIndex, fromCardIndex);
        // проверка изменения баланса на обоих картах на указанную в переводе сумму
        assertEquals(expected[0], actual[0]);
        assertEquals(expected[1], actual[1]);
    }

    @AfterEach
    void assertBalancesArePositive() {
        int[] newBalances = getBalances(1, 2);
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

    private int[] getBalances(int toCardIndex, int fromCardIndex) {
        return new int[]{
                getCardBalance(getCardIDByListIndex(toCardIndex)),
                getCardBalance(getCardIDByListIndex(fromCardIndex))
        };
    }
}

