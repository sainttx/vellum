
function utilInit() {
    String.prototype.startsWith = function(string) {
        return this.indexOf(string) === 0;
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
    $.validator.addMethod("sanitary", validateSanitary, "Please enter valid characters only.");    
}

function validateSanitary(value, element){
    return !/[<>]/.test(value);
}

function sanitize(text) {
    return text.replace(/[<>]/gi, '');
}

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

function arrayIndexOf(array, data, matcher) {
    for (var i = 0 ; i < array.length; i++) {
        if (matcher(array[i], data)) {
            return i;
        }
    }
    return null;
}

function validatorHighlight(element) {
    $(element).closest('.control-group').removeClass('success').addClass('error');
}

function validatorSuccess(element) {
    $(element).addClass('valid');
    $(element).closest('.control-group').removeClass('error').addClass('success');
}

function formatDate(date) {
    return moment(date).format('MMM Do, h:mm:ss a');
}

function foreach(array, handler) {
    for (var i = 0; i < array.length; i++) {
        handler(i, array[i]);
    }
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