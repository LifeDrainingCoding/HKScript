package com.lifedrained.libs;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class UpgradeItem {
    private WebElement upgrade ;
    private String rawPrice;
    private double multiplier= 1;
    private double price;
    public UpgradeItem(WebElement upgrade) {
        this.upgrade = upgrade;
        rawPrice =  upgrade.findElement(By.className("price-value")).getText();
    }
    private void normalizePrice() {
        if(rawPrice.contains("K")){
            rawPrice = rawPrice.replaceAll("K","");
            multiplier = 1000;
        }else if(rawPrice.contains("M")){
            rawPrice = rawPrice.replaceAll("M","");
            multiplier =  Math.pow(10, 6);
        }else if(rawPrice.contains("T")){
            rawPrice = rawPrice.replaceAll("T","");
            multiplier = Math.pow(10, 12);
        }else if(rawPrice.contains("B")){
            rawPrice = rawPrice.replaceAll("B","");
            multiplier = Math.pow(10, 9);
        }else if(rawPrice.contains("Q")){
            rawPrice = rawPrice.replaceAll("Q","");
            multiplier = Math.pow(10, 15);
        }
        if(rawPrice.contains(",")){
          rawPrice = rawPrice.replace(",",".");
          price = multiplier*Double.parseDouble(rawPrice);
        }else{
            price = Double.parseDouble(rawPrice);
        }

    }

    public double getPrice() {
        return price;
    }
}
