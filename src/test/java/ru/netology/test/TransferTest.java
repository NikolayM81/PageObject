package ru.netology.test;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.LoginPage;
import static org.junit.jupiter.api.Assertions.assertEquals;


class TransferTest {






    @Test
        void shouldTransferMoneyToSecondCardFromFirst() {
        Selenide.open("http://localhost:9999", LoginPage.class);
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);

        var firstCardData = DataHelper.getFirstCardData();
        var secondCardData = DataHelper.getSecondCardData();

        var firstCardBalance = dashboardPage.getCardBalance(firstCardData);
        var secondCardBalance = dashboardPage.getCardBalance(secondCardData);

        var sum = DataHelper.generateValidSum(firstCardBalance);

        var expectedFirstCardBalance = firstCardBalance - sum;
        var expectedSecondCardBalance = secondCardBalance + sum;

        var transferPage = dashboardPage.getTransferPage(secondCardData);
        dashboardPage = transferPage.validTransfer(String.valueOf(sum),firstCardData);

        var actualFirstCardBalance = dashboardPage.getCardBalance(firstCardData);
        var actualSecondCardBalance = dashboardPage.getCardBalance(secondCardData);

        assertEquals(expectedFirstCardBalance, actualFirstCardBalance);
        assertEquals(expectedSecondCardBalance,actualSecondCardBalance);
    }


    @Test
        void shouldNotTransferMoneyToFirstCardFromSecondInsufficientSum() {
        Selenide.open("http://localhost:9999", LoginPage.class);
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);

        var firstCardData = DataHelper.getFirstCardData();
        var secondCardData = DataHelper.getSecondCardData();

        var secondCardBalance = dashboardPage.getCardBalance(secondCardData);

        var sum = DataHelper.generateInvalidSum(secondCardBalance);

        var transferPage = dashboardPage.getTransferPage(firstCardData);
        transferPage.topUpCard(String.valueOf(sum), secondCardData);
        transferPage.errorWithInsufficientSum("Ошибка! Недостаточно средств на карте!");


    }

}
