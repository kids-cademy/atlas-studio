$package("com.kidscademy");

com.kidscademy.View = class extends js.dom.Element {
    constructor(ownerDoc, node) {
        super(ownerDoc, node);
    }

    onCreate(page) {
        this._page = page;
    }

    onDestroy() {

    }

    onPause() {

    }

    onResume() {

    }
    
    toString() {
        return "com.kidscademy.View";
    }
};
