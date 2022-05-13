package com.kitchenFiller;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

import java.net.URISyntaxException;

public class StorePurchases {
        /*
        * Make a hardcoded list of websites to check prices
        * Make the user check the top 3 places to check
         */
        public StorePurchases() throws URISyntaxException, IOException {
            Desktop d = Desktop.getDesktop();
            d.browse(new URI("https://www.google.com/"));
        }

}