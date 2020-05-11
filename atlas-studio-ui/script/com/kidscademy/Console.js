$package("com.kidscademy");

com.kidscademy.Console = class extends js.dom.Element {
    constructor(ownerDoc, node) {
        super(ownerDoc, node);

        this._screen = this.getByCssClass("screen");
        
        this._serverEvents = new EventSource("server.event");
        this._serverEvents.addEventListener("ConsoleMessage", this._onConsoleMessage.bind(this));

        this._actions = this.getByClass(com.kidscademy.Actions);
        this._actions.bind(this);
    }

    println(text) {
        var p = this._ownerDoc.createElement("p").setText(text);
        this._screen.addChildFirst(p);
        if (this._screen.getChildrenCount() > 100) {
            this._screen.getLastChild().remove();
        }
    }

    _onConsoleMessage(ev) {
        var message = JSON.parse(ev.data);
        this.println(message.text);
    }

    _onClear() {
        this._screen.removeChildren();
    }

    _onClose() {
        this.hide();
    }

    toString() {
        return "com.kidscademy.Console";
    }
};
