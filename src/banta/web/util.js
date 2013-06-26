
var u = {
    init: function() {
        u.string.init();
        u.date.init();
        u.array.init();
        u.validate.init();
    },
    object: {
        containsKey: function(object, key) {
            return u.number.ge(u.array.indexOf(Object.keys(object), key), 0);
        },
    },
    string: {
        init: function() {
            String.prototype.startsWith = function(string) {
                return this.indexOf(string) === 0;
            };
            String.prototype.endsWith = function(string) {
                return this.indexOf(string) === (this.length - string.length);
            };
            String.prototype.contains = function(string) {
                return this.indexOf(string) === 0 || this.indexOf(string) > 0;
            };
            if (!String.prototype.format) {
                String.prototype.format = function() {
                    var args = arguments;
                    return this.replace(/{(\d+)}/g, function(match, number) {
                        return args[number];
                    });
                };
            }
            if (!String.prototype.isEmpty) {
                String.prototype.isEmpty = function() {
                    return isEmpty(this);
                };
            }
        },
        sanitize: function(text) {
            return text.replace(/[<>]/gi, '');
        },
    },
    array: {
        init: function() {
        },
        indexOf: function(array, data, matcher) {
            if (matcher) {
                return u.array.matchIndexOf(array, data, matcher);
            }
            for (var i = 0; i < array.length; i++) {
                if (array[i] === data) {
                    return i;
                }
            }
            return null;
        },
        matchIndexOf: function(array, data, matcher) {
            for (var i = 0; i < array.length; i++) {
                if (matcher) {
                    if (matcher(array[i], data)) {
                        return i;
                    }
                } else {

                }
            }
            return null;
        },
        contains: function(array, data, matcher) {
            return u.number.ge(u.array.indexOf(array, data, matcher), 0);
        }
    },
    date: {
        init: function() {
            if (!Date.prototype.formatPretty) {
                Date.prototype.format = function() {
                    return u.format.format(this);
                }
            }
        },
        format: function(date) {
            if (moment().format('MMM Do') === moment(date).format('MMM Do')) {
                return moment(date).format('h:mm a');
            } else {
                return moment(date).format('h:mm a MMM Do');
            }
        },
    },
    number: {
        init: function() {
        },
        ge: function(number, value) {
            return number && number !== null && number >= value;
        },
    },
    validate: {
        init: function() {
            $.validator.addMethod("sanitary", u.validate.sanitary, "Please enter valid characters only.");
        },
        sanitary: function(value, element) {
            return !/[<>]/.test(value);
        },
        digits: function(value) {
            return /^\d+$/.test(value);
        },
        digitsLength: function(value, minLength, maxLength) {
            return value && u.validate.digits(value) && value.length >= minLength && value.length <= maxLength;
        },
        phoneNumber: function(value) {
            if (state.env === 'test' && value === '111') {
                return true;
            }
            if (value && value.length >= 10) {
                if (value.charAt(0) === '+') {
                }
                return /^\+?\d+$/.test(value);
            }
            return false;
        },
        highlight: function(element) {
            $(element).closest('.control-group').removeClass('success').addClass('error');
        },
        success: function(element) {
            $(element).addClass('valid');
            $(element).closest('.control-group').removeClass('error').addClass('success');
        },
    }
};


function redirectDocument() {
    console.log("redirectDocument " + window.location.protocol);
    if (window.location.protocol === "http:") {
        var host = location.host;
        var index = location.host.indexOf(':');
        if (index > 0) {
            host = location.host.substring(0, index) + ':8443';
        }
        window.location = "https://" + host + location.pathname + location.search + location.hash;
        console.log(window.location);
        return true;
    }
    return false;
}

function notify(message) {
    console.log(message);
}

function buildTr(array) {
    var html = "<tr>";
    for (var i = 0; i < array.length; i++) {
        html += '<td>' + array[i] + '</td>';
    }
    html += '</tr>';
    if (false) {
        console.log(html);
    }
    return html;
}

function buildTable(tbody, arrayer, list) {
    tbody.empty();
    for (var i = 0; i < list.length; i++) {
        tbody.append(buildTr(arrayer(list[i])));
    }
}

function foreach(array, handler) {
    console.log('foreach', handler.length);
    for (var i = 0; i < array.length; i++) {
        handler(array[i], i);
    }
}

function foreachKey(object, handler) {
    var keys = Object.keys(object);
    for (var i = 0; i < keys.length; i++) {
        var value = object[keys[i]];
        handler(keys[i], value, i);
    }
}

function arrayLast(array) {
    return array[array.length - 1];
}

function isEmpty(object) {
    return (object === undefined || object === null || object.length === 0);
}

function isTrue(object) {
    return (object !== undefined && object !== null && object === true);
}

function assertTrue(message, value) {
    if (!isTrue(value)) {
        alert('assert failed: ' + message);
        return false;
    } else {
        return true;
    }
}
