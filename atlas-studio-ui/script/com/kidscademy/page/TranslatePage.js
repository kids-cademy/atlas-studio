$package("com.kidscademy.page");

com.kidscademy.page.TranslatePage = class extends com.kidscademy.Page {
    constructor() {
        super();

        this._sidebar.on("taxonomy-translate", this._onTaxonomyTranslate, this);
        this._sidebar.on("features-translate", this._onFeaturesTranslate, this);
    }

    _onTaxonomyTranslate() {
        WinMain.assign("@link/taxonomy-translator");
    }

    _onFeaturesTranslate() {
        WinMain.assign("@link/features-translator");
    }

    toString() {
        return "com.kidscademy.page.TranslatePage";
    }
};

WinMain.createPage(com.kidscademy.page.TranslatePage);
