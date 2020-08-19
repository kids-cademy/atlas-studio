$package("com.kidscademy.page");

com.kidscademy.page.TranslatePage = class extends com.kidscademy.Page {
    constructor() {
        super();
    }

    toString() {
        return "com.kidscademy.page.TranslatePage";
    }
};

WinMain.createPage(com.kidscademy.page.TranslatePage);
