$package("com.kidscademy.format");

com.kidscademy.format.ImageFormat = class {
    constructor() {
    }

    format(path) {
        return "/media/" + path;
    }

    toString() {
        return "com.kidscademy.format.ImageFormat";
    }
};
