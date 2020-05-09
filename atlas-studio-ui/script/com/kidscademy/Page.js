$package("com.kidscademy");

com.kidscademy.Page = class extends js.ua.Page {
    constructor() {
        super();

        this.ERRORS = ["", // SUCCESS
            "Picture file name already used.", // UNIQUE_PICTURE_FILE_NAME
            "Featured picture should have transparent background.", // TRANSPARENT_FEATURED_PICTURE
            "Collection name already used.", // UNIQUE_COLLECTION_NAME
            "Object name already used.", // UNIQUE_OBJECT_NAME
            "Collection should be empty.", // EMPTY_COLLECTION
            "Release should be empty.", // EMPTY_RELEASE
            "Not registered link domain.", // REGISTERED_LINK_DOMAIN
            "Image dimensions too small." // IMAGE_DIMENSIONS
        ];

        window.onscroll = () => {
            WinMain.doc.getByTag("body").addCssClass("scroll", window.pageYOffset > 40);
        }

        this.getByCss("header .back.action").on("click", this._onBack, this);

        WinMain.on("unload", this._onUnload, this);
        WinMain.page = this;

        this._sidebar = this.getByClass(com.kidscademy.Sidebar);
    }

    _onUnload() {
    }

    onServerFail(er) {
        $error(`com.kidscademy.page.Page#onServerFail ${er.cause}: ${er.message}`);
        js.ua.System.error(`${er.cause}: ${er.message}`);
    }

    onAuthenticationRequired(url) {
        $error(`com.kidscademy.page.Page#onAuthenticationRequired: Authentication required for ${url}`);
        js.ua.System.error(`Authentication required for ${url}`);
    }

    onBusinessFail(er) {
        if (er.errorCode > this.ERRORS.length) {
            super.onBusinessFail(er);
            return;
        }
        js.ua.System.error(this.ERRORS[er.errorCode]);
    }

    // --------------------------------------------------------------------------------------------

    setPageAttr(name, object) {
        this.setContextAttr(this._pageRelativeName(name), object);
    }

    getPageAttr(name) {
        return this.getContextAttr(this._pageRelativeName(name));
    }

    removePageAttr(name) {
        this.removeContextAttr(this._pageRelativeName(name));
    }

    _pageRelativeName(name) {
        return `${this.toString()}.${name}`;
    }

    setContextAttr(name, object) {
        localStorage.setItem(name, js.lang.JSON.stringify(object));
    }

    getContextAttr(name) {
        var value = localStorage.getItem(name);
        if (value == null) {
            return null;
        }
        return js.lang.JSON.parse(value);
    }

    removeContextAttr(name) {
        localStorage.removeItem(name);
    }

    findContextAttr(rex, callback) {
        for (let name in localStorage) {
            if (name.match(rex) != null) {
                callback(this.getContextAttr(name));
            }
        }
    }

    // --------------------------------------------------------------------------------------------

    _onBack() {
        WinMain.back();
    }

    toString() {
        return "com.kidscademy.Page";
    }
};

// this page class is designed to be extended ; to not create it explicitly!
// next commented line is a reminder and need leave it commented
// WinMain.createPage(com.kidscademy.Page)
