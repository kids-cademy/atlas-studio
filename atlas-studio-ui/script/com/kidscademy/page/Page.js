$package("com.kidscademy.page");

/**
 * Base page class provides functionality common to all pages. It is designed to be extended by concrete subclasses.
 * 
 * @author Iulian Rotaru
 */
com.kidscademy.page.Page = class extends js.ua.Page {
    /**
	 * Construct an instance of Page class.
	 */
    constructor() {
        super();

        this.ERRORS = ["", // SUCCESS
            "Picture file name already used.", // NOT_UNIQUE_PICTURE_FILE_NAME
            "Featured picture should have transparent background.", // NOT_TRANSPARENT_FEATURED_PICTURE
            "Collection name already used.", // NOT_UNIQUE_COLLECTION_NAME
            "Collection should be empty." // NOT_EMPTY_COLLECTION
        ];

        window.onscroll = () => {
            WinMain.doc.getByTag("body").addCssClass("scroll", window.pageYOffset > 40);
        }

        this.getByCss("header .back.action").on("click", this._onBack, this);

        WinMain.on("unload", this._onUnload, this);
        WinMain.page = this;
    }

    _onUnload() { }

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

    setContextAttr(name, object) {
        this._setStorageItem(name, object);
    }

    getContextAttr(name) {
        return this._getStorageItem(name, true);
    }

    removeContextAttr(name) {
        localStorage.removeItem(name);
    }

    hasContextAttr(name) {
        return localStorage.getItem(name) != null;
    }

    setPageAttr(name, object) {
        this._setStorageItem(this._pageRelativeName(name), object);
    }

    getPageAttr(name) {
        return this._getStorageItem(this._pageRelativeName(name), false);
    }

    removePageAttr(name) {
        localStorage.removeItem(this._pageRelativeName(name));
    }

    _setStorageItem(name, object) {
        localStorage.setItem(name, js.lang.JSON.stringify(object));
    }

    _getStorageItem(name, strict) {
        var value = localStorage.getItem(name);
        if (value == null) {
            if (!strict) {
                return null;
            }
            if (!this._isPreviewMode()) {
                $debug("com.kidscademy.page.Page#getContextAttr", "Invalid global context. Missing attribute |%s|. Go to home page.", name);
                return;
            }
            // is legal for value to be missing on development in preview mode
            switch (name) {
                case "objectId":
                    value = "1";
                    break;

                case "collection":
                    value = '{"id":1,"taxonomyClass":"MUSICAL_INSTRUMENT"}';
                    break;
            }
        }
        return js.lang.JSON.parse(value);
    }

    _onBack() {
        WinMain.back();
    }

    _isPreviewMode() {
        // by convention all preview contexts contains -preview suffix
        return WinMain.url.host.includes('-preview');
    }

    _pageRelativeName(name) {
        return `${this.toString()}.${name}`;
    }

    /**
	 * Class string representation.
	 * 
	 * @return this class string representation.
	 */
    toString() {
        return "com.kidscademy.page.Page";
    }
};

// this page class is designed to be extended ; to not create it explicitly!
// next commented line is a reminder and need leave it commented
// WinMain.createPage(com.kidscademy.page.Page)
