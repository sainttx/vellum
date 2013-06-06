
var server = googleServer;

$(document).ready(function() {
    if (window.location.protocol == "http:") {
        server = mockServer;
    }
    documentReady();
});

function documentReady() {
    console.log("documentReady");
    initLib();
    personaReady();
    googleLoginReady();
    contactsReady();
    contactAddReady();
    $('.home-clickable').click(homeClick);
    $('.reload-clickable').click(reloadClick);
    $('.about-clickable').click(aboutClick);
    $('.contact-clickable').click(contactClick);
    $('.logout-clickable').click(logoutClick);
    initServer();
}

function initLib() {
    if (!String.prototype.format) {
        String.prototype.format = function() {
            var args = arguments;
            return this.replace(/{(\d+)}/g, function(match, number) {
                return args[number];
            });
        };
    }
}

function removeCookies() {
    $.removeCookie('googleAuth');
}

function initServer() {
    console.log("initServer");
    if (false) {
        removeCookies();
    }
}

function documentReadyRedirect() {
    console.log("documentReadyRedirect");
    if (!redirectDocument()) {
        documentReady();
    }
}
function redirectDocument() {
    console.log("redirectDocument " + window.location.protocol);
    if (window.location.protocol == "http:") {
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

function showLanding() {
    showLoggedOut();
}

function showLoggedOut() {
    $('.page-container').hide();
    $('.loggedin-viewable').hide();
    $('.logout-clickable').hide();
    $('.loggedout-viewable').show();
    $("#landing-container").show();
}

function showLoggedInEmail(email) {
    notify('Welcome, ' + email);
    $('#loggedin-username-clickable').text(email);
    $('#loggedin-username-clickable').show();
    showLoggedIn();    
}

function showLoggedIn() {
    $('.login-clickable').hide();
    $('.page-container').hide();
    $('.loggedout-viewable').hide();
    $('.loggedin-viewable').show();
    $('.logout-clickable').show();
    $('#loggedin-username').show();
    $('#loggedin-info').show();
    $('#welcome-container').show();
}

function notify(message) {
    console.log(message);
}

function buildTr(handler, index) {
    var object = handler.list[index];
    var array = handler.columnArray(object);
    var html = "<tr onclick='" + handler.name + "ListRowClick(" + handler.id(object) + ")'>";
    for (var i = 0; i < array.length; i++) {
        html += '<td>' + array[i] + '</td>';
    }
    html += '</tr>';
    console.log(html);
    return html;
}

function buildTable(tbody, handler) {
    tbody.empty();
    for (var i = 0; i < handler.list.length; i++) {
        tbody.append(buildTr(handler, i));
    }
}

function logoutClick(event) {
    if (personaEmail !== null) {
        personaLogoutClick();
    } else if (googleLoginEmail !== null) {
        googleLogoutClick();
    } else {
        console.warn("not logged in");
    }
}

function logoutReq() {
    server.ajax({
        type: 'POST',
        url: '/logout',
        data: null,
        success: logoutRes,
        error: logoutError
    });
}

function logoutRes(res) {
    console.log("logoutRes");
    console.log(res);
    if (res.email !== null) {
    }
    showLoggedOut();
}

function logoutError() {
    console.log("logoutError");
    showLoggedOut();
}

function aboutClick() {
    console.log("aboutClick");
    $('.nav-item').removeClass("active");
    $('.page-container').hide();
    $("#about-container").show();
}

function homeClick() {
    $('.nav-item').removeClass("active");
    $('.page-container').hide();
    if (!googleLoginEmail) {
        $("#landing-container").show();        
    } else {
        $("#home-container").show();
    }
}

function reloadClick() {
    console.log('reload');
    console.log(window.location);
    if (true) {
        removeCookies();
    }
    window.location = window.location.origin;
}

function contactClick() {
    $('.nav-item').removeClass("active");
    $('.page-container').hide();
    $("#contact-container").show();
}
