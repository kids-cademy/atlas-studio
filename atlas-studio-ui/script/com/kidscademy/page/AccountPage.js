$package("com.kidscademy.page");

com.kidscademy.page.AccountPage = class extends com.kidscademy.Page {
	constructor() {
		super();
	}

	toString() {
		return "com.kidscademy.page.AccountPage";
	}
};

WinMain.createPage(com.kidscademy.page.AccountPage);
