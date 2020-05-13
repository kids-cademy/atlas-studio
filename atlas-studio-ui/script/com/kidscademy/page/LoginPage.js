$package("com.kidscademy.page");

com.kidscademy.page.LoginPage = class extends js.ua.Page {
    constructor() {
        super();

        const loginSection = this.getByCssClass("login-section");
        this._loginForm = loginSection.getByTag("form");
        loginSection.getByTag("button").on("click", this._onLoginButton, this);
    }

    _onLoginButton(ev) {
        if (this._loginForm.isValid()) {
            AdminService.login(this._loginForm, ok => {
                if (ok) {
                    WinMain.assign("@link/collections");
                }
                else {
                    this._loginForm.reset();
                }
            });
        }
    }

    toString() {
        return "com.kidscademy.page.LoginPage";
    }
};

WinMain.createPage(com.kidscademy.page.LoginPage);
