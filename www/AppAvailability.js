var appAvailability = {
    
    id: function(successCallback, errorCallback) {
        cordova.exec(
            successCallback,
            errorCallback,
            "AppAvailability",
            "getId",
            []
        );
    },

    apps: function(successCallback, errorCallback) {
        cordova.exec(
            successCallback,
            errorCallback,
            "AppAvailability",
            "getApps",
            []
        );
    },

    check: function(urlScheme, successCallback, errorCallback) {
        cordova.exec(
            successCallback,
            errorCallback,
            "AppAvailability",
            "checkAvailability",
            [urlScheme]
        );
    },
    
    checkBool: function(urlScheme, callback) {
        cordova.exec(
            function(success) { callback(success); },
            function(error) { callback(error); },
            "AppAvailability",
            "checkAvailability",
            [urlScheme]
        );
    },

    sendCommand: function(command, successCallback, errorCallback){
        cordova.exec(
            successCallback,
            errorCallback,
            "AppAvailability",
            "sendCommand",
            [command]
        );
    }
    
};

module.exports = appAvailability;
