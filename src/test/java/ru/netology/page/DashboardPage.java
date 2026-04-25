package ru.netology.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;
import org.checkerframework.checker.nullness.qual.NonNull;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private SelenideElement heading = $("[data-test-id=dashboard]");
    private ElementsCollection cards = $$(".list__item div");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";

    public DashboardPage() {
        heading.shouldBe(visible);
    }

    public int getCardBalance(DataHelper.@NonNull CardData cardData) {
        String text = $("[data-test-id =" +"'" + cardData.getTestIdInCss() + "']").getText();
        return extractBalance(text);
    }

    private int extractBalance(@NonNull String text) {
        val start = text.indexOf(balanceStart);
        val finish = text.indexOf(balanceFinish);
        val value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }


    public TransferPage getTransferPage(DataHelper.@NonNull CardData cardData) {
        cards.findBy(attribute("data-test-id", cardData.getTestIdInCss())).$("button").click();
        return new TransferPage();
    }
}
