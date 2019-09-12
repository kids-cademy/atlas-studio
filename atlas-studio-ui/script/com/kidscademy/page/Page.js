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
            "Featured picture should have transparent background." // NOT_TRANSPARENT_FEATURED_PICTURE
        ];

        window.onscroll = () => {
            WinMain.doc.getByTag("body").addCssClass("scroll", window.pageYOffset > 40);
        }

        this.getByCss("header .back.action").on("click", this._onBack, this);
    }

    onServerFail(er) {
        $error(`com.kidscademy.page.Page#onServerFail ${er.cause}: ${er.message}`);
        js.ua.System.error(`${er.cause}: ${er.message}`);
    }

    onBusinessFail(er) {
        if (er.errorCode > this.ERRORS.length) {
            super.onBusinessFail(er);
            return;
        }
        js.ua.System.error(this.ERRORS[er.errorCode]);
    }

    setContextAttr(name, object) {
        localStorage.setItem(name, js.lang.JSON.stringify(object));
    }

    getContextAttr(name) {
        var value = localStorage.getItem(name);
        if (value == null) {
            // value can be missing on development in preview mode
            switch (name) {
                case "objectId":
                    value = "1";
                    break;

                case "collection":
                    value = '{"id":1}';
                    break;
            }
        }
        return js.lang.JSON.parse(value);
    }

    _onBack() {
        WinMain.back();
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
