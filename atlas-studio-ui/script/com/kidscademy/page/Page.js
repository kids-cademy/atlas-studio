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
    }

    onServerFail(er) {
        $error(`com.kidscademy.page.Page#onServerFail ${er.cause}: ${er.message}`);
        js.ua.System.error("Server error. Please contact administrator.");
    }

    onBusinessFail(er) {
        if (er.errorCode > this.ERRORS.length) {
            super.onBusinessFail(er);
            return;
        }
        js.ua.System.error(this.ERRORS[er.errorCode]);
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
