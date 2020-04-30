$package("com.kidscademy.format");

com.kidscademy.format.RoundFormat = class {
    constructor() {
    }

    format(number) {
        if (number == null) {
            return null;
        }
        return Number(number.toFixed(9));
    }

    parse(value) {
        if (value === '') {
            return null;
        }
        return Number(value);
    }

    test(number) {
        return !isNaN(number);
    }

    toString() {
        return "com.kidscademy.format.RoundFormat";
    }
};
