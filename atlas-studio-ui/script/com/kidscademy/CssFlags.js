$package("com.kidscademy");

com.kidscademy.CssFlags = class {
    constructor(element, ...flags) {
        this._element = element;
        this._flags = flags;

        this._currentFlag = null;
        this._flags.forEach(flag => this._element.removeCssClass(flag));
    }

    set(flag) {
        if (flag == null) {
            flag = this._flags[0];
        }

        if (this._flags.indexOf(flag) === -1) {
            throw `Invalid flag |${flag}| for element |${this._element.toString()}|.`;
        }
        if (this._currentFlag != null) {
            this._element.removeCssClass(this._currentFlag);
        }
        this._currentFlag = flag;
        this._element.addCssClass(flag);
    }

    get() {
        return this._currentFlag;
    }

    toString() {
        return "com.kidscademy.CssFlags";
    }
}
