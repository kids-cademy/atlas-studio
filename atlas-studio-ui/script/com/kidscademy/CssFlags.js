$package("com.kidscademy");

com.kidscademy.CssFlags = class {
    constructor(flagName, element, ...flags) {

        this._storageName = `com.kidscademy.CssFlags.${flagName}`;

        this._element = element;
        this._flags = flags;

        flags.forEach(flag => element.removeCssClass(flag));

        this._currentFlag = localStorage.getItem(this._storageName);
        if (!this._currentFlag) {
            this._currentFlag = flags[0];
        }
        this._element.addCssClass(this._currentFlag);
    }

    set(flag) {
        if (this._flags.indexOf(flag) === -1) {
            throw `Invalid flag |${flag}| for element |${this._element.toString()}|.`;
        }
        if (this._currentFlag) {
            this._element.removeCssClass(this._currentFlag);
        }
        localStorage.setItem(this._storageName, flag);
        this._currentFlag = flag;
        this._element.addCssClass(flag);
    }
}
