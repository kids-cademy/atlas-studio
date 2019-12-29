$package("com.kidscademy.format");

com.kidscademy.format.ImageFormat = function () {
};

com.kidscademy.format.ImageFormat.prototype = {
    format: function (path) {
        return "/media/" + path;
    },

    toString: function () {
        return "com.kidscademy.format.ImageFormat";
    }
};
$extends(com.kidscademy.format.ImageFormat, Object);
