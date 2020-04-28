$package("com.kidscademy");

com.kidscademy.ImageView = class extends js.dom.Image {
    constructor(ownerDoc, node) {
        super(ownerDoc, node);
        this._image = null;
    }

    setImage(image) {
        this._image = image;
        super.reload(image.src);
    }

    getImage() {
        return this._image;
    }

    reset() {
        this._image = null;
    }

    toString() {
        return "com.kidscademy.ImageView";
    }
};
